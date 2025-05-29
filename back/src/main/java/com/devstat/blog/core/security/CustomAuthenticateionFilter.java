package com.devstat.blog.core.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class CustomAuthenticateionFilter extends AbstractAuthenticationProcessingFilter {

    private ObjectMapper objectMapper = new ObjectMapper();

    public CustomAuthenticateionFilter(){
        // 로그인 시도시에 필터 동작
        super(new RegexRequestMatcher("/login", HttpMethod.POST.name()));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {

        if(!"POST".equals(request.getMethod())){
            throw new IllegalStateException("Authentication is supported only 'POST'");
        }

        // body를 UserVo에 맵핑
        accountVo accountVo = objectMapper.readValue(request.getReader(), accountVo.class);

        if(!StringUtils.hasLength(accountVo.getId()) || !StringUtils.hasLength(accountVo.getPassword())){
            throw new IllegalArgumentException("userName or userPassword is empty!!");
        }

        // 토큰생성
        CustomAuthenticationToken token = new CustomAuthenticationToken(
                accountVo.getId(),
                accountVo.getPassword()
        );

        return getAuthenticationManager().authenticate(token);
    }

    @Data
    public static class accountVo{
        private String id;
        private String password;
    }
}
