package com.devstat.blog.core.aspect;

import com.devstat.blog.core.code.RoleCode;
import com.devstat.blog.core.code.StatusCode;
import com.devstat.blog.core.exception.IsNotAdminUserException;
import com.devstat.blog.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

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

    @Around("@annotation(com.devstat.blog.core.annotation.CheckAdmin)")
    public Object checkAdmin(ProceedingJoinPoint joinPoint) throws Throwable {

        log.info("현재 로그인한 회원 관리자인지 조회");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal();

        if (!member.getRole().equals(RoleCode.ADMIN)) {
            throw new IsNotAdminUserException(StatusCode.IS_NOT_ADMIN_USER);
        }

        return joinPoint.proceed();
    }

}
