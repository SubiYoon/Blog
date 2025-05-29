package com.devstat.blog.core.security;

import com.devstat.blog.core.code.StatusCode;
import com.devstat.blog.core.exception.CmmnException;
import com.devstat.blog.core.exception.CustomJwtException;
import com.devstat.blog.core.exception.ExceptionVo;
import com.devstat.blog.utility.ItemCheck;
import com.devstat.blog.utility.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.SignatureException;
import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        if(request.getRequestURI().equals("/member/join")
                || request.getRequestURI().equals("/startup")
                || request.getRequestURI().equals("/liveness")
                || request.getRequestURI().equals("/readiness")
                || request.getRequestURI().contains("/video/tus/file/upload/")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        // 요청 Method가 Options의 경우 pass
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Client에서 API를 요청시 쿠키를 확인
        Cookie[] cookies = request.getCookies();
        String token = null;
        if (ItemCheck.isNotEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                if ("DEVSTAT-JWT".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        try {
            if (ItemCheck.isNotEmpty(token)) {
                if (JwtUtil.isValidToken(token)) {
                    String loginId = JwtUtil.getUserIdFromToken(token);
                    log.debug("loginId Check :: " + loginId);

                    if (ItemCheck.isNotEmpty(loginId)) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(loginId);
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        filterChain.doFilter(request, response);
                    } else throw new CustomJwtException(StatusCode.USER_NOT_FOUND);
                } else {
                    Cookie jwtCookie = new Cookie("DEVSTAT-JWT", null);
                    jwtCookie.setMaxAge(0);
                    jwtCookie.setPath("/");
                    response.addCookie(jwtCookie);
                    throw new CustomJwtException(StatusCode.TOKEN_NOT_VALID);
                }
            }else throw new CustomJwtException(StatusCode.TOKEN_NOT_FOUND);
        }catch (Exception e) {
            ObjectMapper objectMapper = new ObjectMapper();

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");

            objectMapper.writeValue(response.getWriter(), tokenErrorTrace(e));

            Cookie jwt = new Cookie("DEVSTAT-JWT", null);
            jwt.setMaxAge(0);
            jwt.setPath("/");
            response.addCookie(jwt);
        }
    }

    private HashMap<String, Object> tokenErrorTrace(Exception e){

        HashMap<String, Object> jsonMap = new HashMap<>();
        String resultMessage;

        if (e instanceof CmmnException) { // AOP 예외
            ExceptionVo vo = ((CustomJwtException) e).getExceptionVo();
            resultMessage = vo.getMessage();
            jsonMap.put("custom_code", vo.getCustomCode());
        } else if (e instanceof ExpiredJwtException) {
            resultMessage = "TOKEN Expired";
        } else if (e instanceof SignatureException){
            resultMessage = "TOKEN SignatureException Login";
        } else if (e instanceof JwtException) {
            resultMessage = "TOKEN Parsing JwtException";
        } else {
            resultMessage = e.getMessage();
        }

        jsonMap.put("status_code", 401);
        jsonMap.put("message", resultMessage);

        log.debug(resultMessage, e);

        return jsonMap;
    }
}
