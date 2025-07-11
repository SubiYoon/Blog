package com.devstat.blog.utility;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.devstat.blog.domain.menu.entity.Menu;
import com.devstat.blog.domain.menu.repository.MenuJpa;
import com.devstat.blog.core.code.StatusCode;
import com.devstat.blog.core.exception.CmmnException;
import com.devstat.blog.domain.member.entity.Member;
import com.devstat.blog.domain.member.repository.MemberJpa;
import jakarta.persistence.EntityManager; // Assuming Jakarta Persistence

@Slf4j
public class DirectoryWatcher {

    private static WatchService watchService;
    private static Path rootPath;
    private static final Map<WatchKey, Path> keys = new ConcurrentHashMap<>();
    private static final Set<String> excludedDirs = new HashSet<>(Arrays.asList(".git", ".obsidian", "Attached File"));
    private static final AtomicBoolean watcherThreadStarted = new AtomicBoolean(false);
    private static long lastGlobalEventTime = 0;
    private static final long GLOBAL_DEBOUNCE_MILLIS = 1000; // 1 second debounce

    // New static fields for dependencies
    private static String blogFilePath;
    private static String alias;
    private static MenuJpa menuJpa;
    private static MemberJpa memberJpa;
    private static NpmUtil npmUtil;
    private static EntityManager em;

    public DirectoryWatcher(String blogFilePath, String alias, MenuJpa menuJpa, MemberJpa memberJpa, NpmUtil npmUtil,
            EntityManager em) throws IOException {
        String blogPath = blogFilePath + "/" + alias;

        log.info("DirectoryWatcher constructor called for path: {}", blogPath);
        // Initialize only once
        if (watchService == null) {
            synchronized (DirectoryWatcher.class) {
                if (watchService == null) { // Double-check locking
                    log.info("Initializing new DirectoryWatcher instance.");
                    watchService = FileSystems.getDefault().newWatchService();
                    rootPath = Paths.get(blogPath);

                    // Assign dependencies to static fields
                    DirectoryWatcher.blogFilePath = blogFilePath;
                    DirectoryWatcher.alias = alias;
                    DirectoryWatcher.menuJpa = menuJpa;
                    DirectoryWatcher.memberJpa = memberJpa;
                    DirectoryWatcher.npmUtil = npmUtil;
                    DirectoryWatcher.em = em;

                    if (!Files.isDirectory(rootPath)) {
                        log.error("Provided path is not a directory: {}", blogPath);
                        throw new IllegalArgumentException("Path must be a directory.");
                    }

                    log.info("Scanning and registering directories under {}", rootPath);
                    // registerAll(rootPath); // Original: Recursively registers all subdirectories
                    register(rootPath); // Modified: Only registers the root directory
                    log.info("Initial registration complete.");

                    if (watcherThreadStarted.compareAndSet(false, true)) {
                        Thread watcherThread = new Thread(DirectoryWatcher::processEvents);
                        watcherThread.setDaemon(true);
                        watcherThread.start();
                    }
                }
            }
        } else {
            // If already initialized, ensure the rootPath is the same, or handle
            // appropriately
            // For now, assume it's the same or ignore subsequent calls if already running
            log.warn("DirectoryWatcher already initialized. Ignoring subsequent constructor call for path: {}",
                    blogPath);
        }
    }

    private static void register(Path dir) throws IOException {
        if (excludedDirs.contains(dir.getFileName().toString())) {
            log.debug("Skipping excluded directory: {}", dir);
            return;
        }
        WatchKey key = dir.register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE);
        keys.put(key, dir);
        log.debug("Registered directory: {}", dir);
    }

    private static void processEvents() {
        log.info("Starting to watch directory: {}", rootPath);
        // Now you can use menuJpa, memberJpa, npmUtil, em here
        // Example: menuJpa.someMethod();
        while (!Thread.currentThread().isInterrupted()) {
            WatchKey key;
            try {
                key = watchService.take();
            } catch (InterruptedException | ClosedWatchServiceException e) {
                log.info("Directory watcher for {} interrupted or closed. Stopping.", rootPath);
                Thread.currentThread().interrupt();
                break;
            }

            Path dir = keys.get(key);
            if (dir == null) {
                log.warn("WatchKey not recognized!! This should not happen if keys map is correctly managed.");
                key.reset(); // Attempt to reset even if not recognized to prevent key from becoming invalid
                continue;
            }

            boolean eventDetected = false;
            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();

                if (kind == StandardWatchEventKinds.OVERFLOW) {
                    log.warn("OVERFLOW event for directory: {}", dir);
                    continue;
                }

                WatchEvent<Path> ev = (WatchEvent<Path>) event;
                Path name = ev.context();
                Path child = dir.resolve(name);

                boolean isExcluded = false;
                for (String excludedDirName : excludedDirs) {
                    Path excludedPath = rootPath.resolve(excludedDirName);
                    log.debug("Comparing child: {} with excludedPath: {}", child, excludedPath);
                    if (child.startsWith(excludedPath)) {
                        isExcluded = true;
                        break;
                    }
                }

                if (isExcluded) {
                    log.debug("Skipping event for excluded path: {}", child);
                    continue;
                }

                log.info("Event kind: {}, File affected: {}", kind.name(), child);
                eventDetected = true; // Mark that a non-excluded event was detected

            }

            // Apply global debounce for the "파일 변경 이력 발생!" log
            if (eventDetected) {
                long currentTime = System.currentTimeMillis();
                log.debug("Debounce check: currentTime={}, lastGlobalEventTime={}, difference={}", currentTime,
                        lastGlobalEventTime, currentTime - lastGlobalEventTime);
                if (currentTime - lastGlobalEventTime > GLOBAL_DEBOUNCE_MILLIS) {
                    log.info("파일 변경 이력 발생!");

                    menuJpa.deleteAll();
                    Member findMember = memberJpa.findById(alias)
                            .orElseThrow(() -> new CmmnException(StatusCode.MEMBER_NOT_FOUND));

                    File blogDir = new File(blogFilePath + "/" + alias);

                    File[] listFiles = blogDir.listFiles();

                    for (File file : listFiles) {
                        if (file.isDirectory() && !file.getName().equals(".obsidian")
                                && !file.getName().equals("Attached File")
                                && !file.getName().equals(".git")) {
                            Menu menu = Menu.of(file.getName(), file.getName(), findMember);
                            menuJpa.save(menu);
                        }
                    }

                    npmUtil.docsRestart(false);
                    lastGlobalEventTime = currentTime;
                } else {
                    log.debug("Debouncing global event log. Last log was {}ms ago.", currentTime - lastGlobalEventTime);
                }
            }

            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);
                if (keys.isEmpty()) {
                    log.warn("All directories are inaccessible. Stopping watch service for {}", rootPath);
                    break;
                }
            }
        }
    }

}
