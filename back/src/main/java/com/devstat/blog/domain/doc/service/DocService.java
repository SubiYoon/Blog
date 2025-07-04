package com.devstat.blog.domain.doc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devstat.blog.core.ParamMap;
import com.devstat.blog.core.annotation.Git;
import com.devstat.blog.core.annotation.InjectAccountInfo;
import com.devstat.blog.core.aspect.AccountDto;
import com.devstat.blog.core.code.StatusCode;
import com.devstat.blog.core.exception.CmmnException;
import com.devstat.blog.domain.doc.dto.DocDto;
import com.devstat.blog.domain.doc.dto.RequestDocDto;
import com.devstat.blog.domain.doc.entity.DocHistory;
import com.devstat.blog.core.code.FileStatusCode;
import com.devstat.blog.domain.doc.repository.DocJpa;
import com.devstat.blog.domain.doc.repository.DocQuery;
import com.devstat.blog.domain.member.entity.Member;
import com.devstat.blog.domain.member.repository.MemberJpa;
import com.devstat.blog.utility.GitUtil;
import com.devstat.blog.utility.StringUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DocService {

    @Value("${blog.file.path}")
    private String blogFilePath;

    private final DocJpa docJpa;
    private final MemberJpa memberJpa;

    private final DocQuery docQuery;

    // 프로젝트 단위 Map: key = 프로젝트명, value = 그 안의 폴더 및 md 파일 경로 리스트
    private Map<String, List<String>> folderTree;

    @Git(task = "pull --rebase origin main")
    @InjectAccountInfo
    public String getDocsTree(AccountDto accountDto) {
        String json = "";
        folderTree = new HashMap<>();

        try (Stream<Path> projectDirs = Files.list(Paths.get(blogFilePath + "/" + accountDto.getAccountId()))) {
            projectDirs
                    .filter(Files::isDirectory)
                    .filter(path -> {
                        String folderName = path.getFileName().toString();
                        return !folderName.contains(".git") &&
                                !folderName.contains(".obsidian") &&
                                !folderName.contains("Attached File");
                    })
                    .forEach(this::scanAndCollectPaths);

            folderTree.values().forEach(list -> list.sort(Comparator.naturalOrder()));

            log.info("폴더 트리 JSON 변환 완료");

            json = StringUtil.convertToJsonTree(folderTree);
        } catch (Exception e) {
            log.error("블로그 파일 경로를 탐색하는 중 오류 발생", e);
        }

        return json;
    }

    // 프로젝트 디렉토리 내의 모든 .md 및 폴더 경로를 수집
    private void scanAndCollectPaths(Path projectRoot) {
        String projectName = projectRoot.getFileName().toString();
        List<String> pathList = folderTree.computeIfAbsent(projectName, k -> new ArrayList<>());

        try (Stream<Path> paths = Files.walk(projectRoot)) {
            paths
                    .filter(path -> Files.isRegularFile(path)
                            && (path.toString().endsWith(".md") || path.toString().endsWith(".json")
                                    || path.toString().endsWith(".js") || path.toString().endsWith(".ts"))
                            || Files.isDirectory(path))
                    .forEach(path -> {
                        Path relative = projectRoot.relativize(path);
                        if (relative.toString().isEmpty())
                            return;

                        String entryPath = "/" + relative.toString().replace("\\", "/");

                        if (Files.isDirectory(path) && !entryPath.endsWith("/")) {
                            entryPath += "/";
                        }

                        if (!pathList.contains(entryPath)) {
                            pathList.add(entryPath);
                        }
                    });

        } catch (IOException e) {
            log.error("프로젝트 내부 탐색 중 오류 발생", e);
        }
    }

    @InjectAccountInfo
    public DocDto selectDoc(AccountDto accountDto, RequestDocDto requestDocDto) {

        Path docPath = Paths.get(blogFilePath + "/" + accountDto.getAccountId() + requestDocDto.getFilePath());

        try (InputStream is = Files.newInputStream(docPath)) {

            byte[] bytes = is.readAllBytes();

            String content = new String(bytes);

            return DocDto.builder()
                    .title(requestDocDto.getTitle())
                    .content(content)
                    .build();

        } catch (Exception e) {
            throw new CmmnException(StatusCode.DOC_SELECT_FAIL, e);
        }
    }

    @Transactional
    @InjectAccountInfo
    @SuppressWarnings("unchecked")
    public void saveDocsTree(AccountDto accountDto, ParamMap params) {
        List<Map<String, Object>> docsTree = (List<Map<String, Object>>) params.get("docsTree");
        String commitMessage = (String) params.get("commitMessage");
        List<Path> createdFiles = new ArrayList<>(); // 생성된 파일들 추적용

        try {
            saveDocRecursive(accountDto.getAccountId(), docsTree, createdFiles);

            GitUtil.gitTask(accountDto, blogFilePath, "add", ".");
            GitUtil.gitTask(accountDto, blogFilePath, "commit", "-m", commitMessage);
            GitUtil.gitTask(accountDto, blogFilePath, "push", "origin", "main");
        } catch (Exception e) {
            // 생성된 파일 삭제
            for (Path path : createdFiles) {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException ex) {
                    // 로그만 남기고 무시 (필요시)
                    log.error("파일 삭제 실패: " + path);
                    throw new CmmnException(StatusCode.DOC_SAVE_FAIL, ex);
                }
            }
            throw e; // 예외 다시 던져서 트랜잭션 롤백
        }
    }

    @SuppressWarnings("unchecked")
    private void saveDocRecursive(String id, List<Map<String, Object>> docsTree, List<Path> createdFiles) {
        for (Map<String, Object> docs : docsTree) {
            String filePath = blogFilePath + "/" + id + docs.get("filePath");
            File file = new File(filePath);

            if ("folder".equals(docs.get("type"))) {
                if (!file.exists()) {
                    file.mkdirs(); // 폴더는 삭제할 필요 없음 (보통 비워지면 자동 삭제)
                }
                if (docs.get("children") != null) {
                    saveDocRecursive(id, (List<Map<String, Object>>) docs.get("children"), createdFiles);
                }

            } else if ("md".equals(docs.get("type"))) {
                if (docs.get("content") != null && !"".equals(docs.get("content"))) {
                    try (FileWriter fw = new FileWriter(file)) {
                        Member member = memberJpa.findById(id)
                                .orElseThrow(() -> new CmmnException(StatusCode.MEMBER_NOT_FOUND));
                        DocHistory docHistory;

                        if (file.exists()) {
                            docHistory = DocHistory.of(member, filePath, FileStatusCode.PATCH);
                        } else {
                            docHistory = DocHistory.of(member, filePath, FileStatusCode.CREATE);
                        }

                        fw.write(docs.get("content").toString());
                        createdFiles.add(file.toPath()); // 생성된 파일 리스트에 추가

                        docJpa.save(docHistory);
                    } catch (Exception e) {
                        throw new CmmnException(StatusCode.DOC_SAVE_FAIL, e);
                    }
                }
            } else {
                throw new CmmnException(StatusCode.INCLUDE_NOT_FILE_AND_FOLDER);
            }
        }
    }

    @InjectAccountInfo
    public void deleteDoc(AccountDto accountDto, RequestDocDto requestDocDto) {
        Path filePath = Paths.get(blogFilePath + "/" + accountDto.getAccountId() + requestDocDto.getFilePath());

        try {
            File file = new File(filePath.toString());

            if (file.isDirectory()) {
                if (!Files.exists(filePath)) {
                    log.error("삭제 대상 경로가 존재하지 않습니다: " + filePath);
                    throw new CmmnException(StatusCode.DOC_DELETE_FAIL);
                }

                try (Stream<Path> walk = Files.walk(filePath)) {
                    walk.sorted(Comparator.reverseOrder()) // 가장 하위부터 삭제
                            .forEach(path -> {
                                try {
                                    Files.deleteIfExists(path);
                                    Member member = memberJpa.findById(accountDto.getAccountId())
                                            .orElseThrow(() -> new CmmnException(StatusCode.MEMBER_NOT_FOUND));

                                    DocHistory docHistory = DocHistory.of(member, path.toString(),
                                            FileStatusCode.DELETE);
                                    docJpa.save(docHistory);
                                } catch (IOException e) {
                                    throw new CmmnException(StatusCode.DOC_DELETE_FAIL, e);
                                }
                            });
                }

            } else {
                Files.deleteIfExists(filePath);
                Member member = memberJpa.findById(accountDto.getAccountId())
                        .orElseThrow(() -> new CmmnException(StatusCode.MEMBER_NOT_FOUND));

                DocHistory docHistory = DocHistory.of(member, filePath.toString(), FileStatusCode.DELETE);
                docJpa.save(docHistory);
            }
        } catch (IOException e) {
            throw new CmmnException(StatusCode.DOC_DELETE_FAIL, e);
        }
    }
}
