package com.devstat.blog.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.devstat.blog.core.aspect.AccountDto;
import com.devstat.blog.core.code.StatusCode;
import com.devstat.blog.core.exception.CmmnException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NpmUtil {

    private final int TARGET_PORT = 3000;

    @Value("${blog.file.path}")
    private String blogRootPath;
    @Value("${blog.front.path}")
    private String blogFrontPath;
    @Value("${blog.just.path}")
    private String justExecutablePath;

    private final SecurityUtil securityUtil;

    private static Process npmProcess; // To hold the background npm process
    private static final AtomicBoolean shutdownHookRegistered = new AtomicBoolean(false);

    public void docsRestart(Boolean isApi) {
        try {
            if (isApi) {
                AccountDto currentMember = securityUtil.getCurrentMember(new AccountDto());

                gitPull(currentMember.getAccountId());
            }

            // 1️⃣ 3000 포트를 점유 중인 프로세스 강제 종료
            frontJustTask("stop", String.valueOf(TARGET_PORT));

            // 2️⃣ dev 서버 실행 (이제 백그라운드에서 실행됨)
            frontJustTask("dev");

        } catch (Exception e) {
            throw new CmmnException(StatusCode.DOCS_RESTART_FAIL, e);
        }
    }

    // 0️⃣ Git pull 수행
    public void gitPull(String alias) throws Exception {

        File git_repo_dir = new File(blogRootPath + "/" + alias);

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

    private void frontJustTask(String... args) throws Exception {
        File frontDir = new File(blogFrontPath).getCanonicalFile();

        if (args.length < 1) {
            throw new IllegalArgumentException("just 명령어가 비어있습니다.");
        }

        String action = args[0];

        List<String> commandList = new ArrayList<>();
        commandList.add(justExecutablePath);
        commandList.addAll(Arrays.asList(args));

        ProcessBuilder builder = new ProcessBuilder(commandList);
        builder.directory(frontDir);
        builder.redirectErrorStream(true);

        Process process = builder.start();
        String commandString = String.join(" ", commandList);

        if ("dev".equals(action)) {
            // Store the process for later termination
            npmProcess = process;

            // Register shutdown hook only once
            if (shutdownHookRegistered.compareAndSet(false, true)) {
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    if (npmProcess != null && npmProcess.isAlive()) {
                        log.info("Shutting down npm process...");
                        npmProcess.destroyForcibly();
                        try {
                            npmProcess.waitFor(10, TimeUnit.SECONDS); // Give it some time to terminate
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            log.warn("Interrupted while waiting for npm process to terminate.", e);
                        }
                        if (npmProcess.isAlive()) {
                            log.error("npm process did not terminate gracefully.");
                        } else {
                            log.info("npm process terminated.");
                        }
                    }
                }));
            }

            // Start a thread to read and log output without blocking
            Thread devReaderThread = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String devLine;
                    boolean compiledSuccessfully = false;
                    while ((devLine = reader.readLine()) != null) {
                        log.info("[{}] {}", action, devLine);
                        if (devLine.contains("compiled successfully")) {
                            compiledSuccessfully = true;
                            log.info("✅ Docusaurus dev server compiled successfully (port {})", TARGET_PORT);
                        }
                    }
                    if (!compiledSuccessfully) {
                        log.warn("Docusaurus dev server output stream ended without 'compiled successfully' message.");
                    }
                } catch (Exception e) {
                    if (!"Stream closed".equals(e.getMessage())) {
                        log.error("dev server log reader thread error", e);
                    }
                }
            });
            devReaderThread.setDaemon(true); // Allow JVM to exit even if this thread is running
            devReaderThread.start();

            log.info("✅ Docusaurus dev server started in background (port {})", TARGET_PORT);

        } else if ("stop".equals(action)) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.info("[{}] {}", action, line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                log.warn("❌ Port termination command might have failed. (exitCode={})", exitCode);
            }

            log.info("✅ Port termination command executed: {}", commandString);

        } else {
            throw new IllegalArgumentException("Unsupported just command: " + action);
        }

    } // No catch block here, let exceptions propagate to docsRestart
}
