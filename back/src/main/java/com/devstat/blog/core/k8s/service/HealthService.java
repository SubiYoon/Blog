package com.devstat.blog.core.k8s.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class HealthService {

    private final AtomicBoolean started = new AtomicBoolean(false);
    private final AtomicBoolean ready = new AtomicBoolean(false);

    @PostConstruct
    public void init() {
        // 가상의 초기화 시뮬레이션 (예: DB 연결 등)
        new Thread(() -> {
            try {
                Thread.sleep(5000); // 5초 후 시작
                started.set(true);
                ready.set(true); // 조건에 따라 설정
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    public boolean isStarted() {
        return started.get();
    }

    public boolean isReady() {
        return ready.get();
    }

    public boolean isAlive() {
        return true; // 실제 로직에 따라 DB ping 등 수행 가능
    }
}
