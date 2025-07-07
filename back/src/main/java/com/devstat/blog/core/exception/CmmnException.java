package com.devstat.blog.core.exception;

import com.devstat.blog.core.code.StatusCode;
import lombok.Getter;

@Getter
public class CmmnException extends RuntimeException {
    ExceptionVo exceptionVo;

    /**
     * stackTrace가 필요 없는 경우
     */
    public CmmnException(StatusCode statusCode) {
        super(statusCode.getMessage());
        this.exceptionVo = new ExceptionVo(statusCode.getStatusCode(), statusCode.getCustomCode(),
                statusCode.getMessage());
    }

    /**
     * stackTrace가 필요 한 경우
     */
    public CmmnException(StatusCode statusCode, Throwable e) {
        super(statusCode.getMessage(), e);
        e.printStackTrace();
        this.exceptionVo = new ExceptionVo(statusCode.getStatusCode(), statusCode.getCustomCode(),
                statusCode.getMessage());
    }
}
