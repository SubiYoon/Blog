package com.devstat.blog.core.aspect;

import com.devstat.blog.core.annotation.Git;
import com.devstat.blog.core.code.StatusCode;
import com.devstat.blog.core.exception.CmmnException;
import com.devstat.blog.domain.member.entity.Member;
import com.devstat.blog.utility.NpmUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AnnotationAop {

    @Around("@annotation(com.devstat.blog.core.annotation.InjectAccountInfo)")
    public Object injectAccountInfo(ProceedingJoinPoint joinPoint) throws Throwable {

        log.info("현재 로그인한 회원 정보 조회");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal();

        Object firstArg = joinPoint.getArgs()[0];
        if (firstArg instanceof AccountDto) {
            AccountDto dto = (AccountDto) firstArg;

            if (member != null) {
                dto.setAccountId(member.getId());
                dto.setAccountName(member.getName());
                dto.setAccountRole(member.getRole());
            } else {
                log.warn("Member is null, unable to inject account info");
            }
        } else {
            log.warn("The argument is not of type Map<String, Object>, unable to inject account info");
        }
        return joinPoint.proceed();
    }

    @Around("@annotation(com.devstat.blog.core.annotation.restartDocs)")
    public Object restartDocs(ProceedingJoinPoint joinPoint) throws Throwable {

        log.info("Docs 재시작");

        NpmUtil.docsRestart();

        return joinPoint.proceed();
    }

    @Around("@annotation(com.devstat.blog.core.annotation.Git)")
    public Object git(ProceedingJoinPoint joinPoint) throws Throwable {

        log.info("🔁 git process execute");

        // 🔽 현재 실행 중인 메서드 정보를 가져옴
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 🔽 메서드에 붙은 @Git 애노테이션을 가져옴
        Git git = method.getAnnotation(Git.class);

        if (git == null) {
            throw new RuntimeException("❌ @Git annotation not found on method");
        }

        String task = git.task();
        String path = git.path();

        String projectOwner = "";

        Object firstArg = joinPoint.getArgs()[0];

        if (firstArg instanceof String) {
            projectOwner = firstArg.toString();
        } else if (firstArg instanceof AccountDto) {
            AccountDto dto = (AccountDto) firstArg;
            projectOwner = dto.getAccountId();
        }

        Path projectPath = Paths.get(path + projectOwner);

        if (Files.exists(projectPath.resolve(".git"))) {
            ProcessBuilder processBuilder = new ProcessBuilder("git", task);
            processBuilder.directory(projectPath.toFile());

            try {
                Process process = processBuilder.start();
                process.waitFor(); // git task 완료 대기

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        log.debug("▶ git output: " + line);
                    }
                }
            } catch (IOException | InterruptedException e) {
                log.error("git " + task + " 실패", e);
                throw new CmmnException(StatusCode.GIT_TASK_FAIL, e);
            }
        }

        return joinPoint.proceed();
    }
}
