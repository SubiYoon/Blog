package com.devstat.blog.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.devstat.blog.core.aspect.AccountDto;
import com.devstat.blog.core.code.StatusCode;
import com.devstat.blog.core.exception.CmmnException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NpmUtil {

    private static final int TARGET_PORT = 3000;
    @Value("${blog.file.path}")
    private static String rootRepo;

    public static void docsRestart() {
        try {
            // 0️⃣ Git pull
            gitPull();

            // 1️⃣ 3000 포트를 점유 중인 프로세스 강제 종료
            killProcessOnPort(TARGET_PORT);

            // 2️⃣ dev 서버 실행
            startDevServer();

        } catch (Exception e) {
            throw new CmmnException(StatusCode.DOCS_RESTART_FAIL, e);
        }
    }

    // 0️⃣ Git pull 수행
    private static void gitPull() throws Exception {
        AccountDto currentAccount = SecurityUtil.getCurrentMember(new AccountDto());

        File git_repo_dir = new File(rootRepo + currentAccount.getAccountId());

        ProcessBuilder builder = new ProcessBuilder("git", "pull");
        builder.directory(git_repo_dir);
        builder.redirectErrorStream(true);

        Process process = builder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                log.info("[git] {}", line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("git pull 실패: exit code = " + exitCode);
        }

        log.info("✅ Git pull 완료");
    }

    // 1️⃣ 해당 포트를 사용하는 프로세스를 종료
    private static void killProcessOnPort(int port) throws Exception {
        ProcessBuilder builder = new ProcessBuilder("bash", "-c", "lsof -ti :" + port);
        Process process = builder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String pid;
            while ((pid = reader.readLine()) != null) {
                log.info("🔍 포트 {}에서 프로세스 발견: PID={}", port, pid);
                Process kill = new ProcessBuilder("kill", "-9", pid).start();
                kill.waitFor();
                log.info("💀 PID {} 종료 완료", pid);
            }
        }

        process.waitFor();
    }

    // 2️⃣ npm run dev 실행
    private static void startDevServer() throws Exception {
        File frontDir = new File("/front").getCanonicalFile();

        ProcessBuilder builder = new ProcessBuilder("npm", "run", "dev");
        builder.directory(frontDir);
        builder.redirectErrorStream(true);

        Process process = builder.start();

        // 비동기 로그 출력
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                boolean foundSuccess = false;
                while ((line = reader.readLine()) != null) {
                    log.info("[npm] {}", line);
                    // ✅ 성공 키워드는 프레임워크별로 달라서 일부 수정
                    if (line.contains("compiled successfully")) {
                        foundSuccess = true;
                    }
                }

                if (!foundSuccess) {
                    log.warn("npm run dev 실행 결과에 성공 키워드가 없습니다.");
                }

            } catch (Exception e) {
                log.error("npm 로그 읽기 중 오류", e);
            }
        }).start();

        log.info("✅ npm dev 서버 실행 요청 완료 (포트 {})", TARGET_PORT);
    }
}
