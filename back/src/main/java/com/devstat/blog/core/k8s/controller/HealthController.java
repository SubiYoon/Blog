package com.devstat.blog.core.k8s.controller;

import com.devstat.blog.core.k8s.service.HealthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HealthController {

    private final HealthService healthService;

    @GetMapping("/startup")
    public ResponseEntity<String> startupCheck() {
        return healthService.isStarted()
                ? ResponseEntity.ok("STARTUP_OK")
                : ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("STARTUP_FAILED");
    }

    @GetMapping("/readiness")
    public ResponseEntity<String> readinessCheck() {
        return healthService.isReady()
                ? ResponseEntity.ok("READY")
                : ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("NOT_READY");
    }

    @GetMapping("/liveness")
    public ResponseEntity<String> livenessCheck() {
        return healthService.isAlive()
                ? ResponseEntity.ok("ALIVE")
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("DEAD");
    }
}
