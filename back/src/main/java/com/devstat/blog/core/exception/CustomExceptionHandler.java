package com.devstat.blog.core.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(CmmnException.class)
    public ResponseEntity<Object> handleIsNotAdminUserException(CmmnException ex) {

        ExceptionVo exceptionVo = ex.getExceptionVo();

        return new ResponseEntity<>(exceptionVo, exceptionVo.getStatusCode());
    }
}
