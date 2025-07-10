package com.devstat.blog.core.aspect;

import com.devstat.blog.domain.member.entity.Member;
import com.devstat.blog.utility.NpmUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Order(1)
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
}
