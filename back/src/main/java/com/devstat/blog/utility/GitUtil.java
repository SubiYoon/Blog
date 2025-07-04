package com.devstat.blog.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.devstat.blog.core.aspect.AccountDto;
import com.devstat.blog.core.code.StatusCode;
import com.devstat.blog.core.exception.CmmnException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GitUtil {

    public static void gitTask(AccountDto accountDto, String blogRootPath, String... task) {
        String projectOwner = accountDto.getAccountId();
        Path projectPath = Paths.get(blogRootPath, projectOwner);

        if (Files.exists(projectPath.resolve(".git"))) {
            // git 명령어 전체 리스트 구성
            List<String> command = new ArrayList<>();
            command.add("git");
            command.addAll(Arrays.asList(task));

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.directory(projectPath.toFile());

            try {
                Process process = processBuilder.start();
                process.waitFor(); // 명령어 종료 대기

                // 표준 출력 처리
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        log.debug("▶ git output: " + line);
                    }
                }

                // 표준 에러도 로그로 확인
                try (BufferedReader errReader = new BufferedReader(
                        new InputStreamReader(process.getErrorStream()))) {
                    String errLine;
                    while ((errLine = errReader.readLine()) != null) {
                        log.warn("⚠ git error: " + errLine);
                    }
                }
            } catch (IOException | InterruptedException e) {
                log.error("git 명령 실패: " + String.join(" ", task), e);
                throw new CmmnException(StatusCode.GIT_TASK_FAIL, e);
            }
        } else {
            log.warn("Git 저장소가 존재하지 않습니다: " + projectPath);
        }
    }

}
