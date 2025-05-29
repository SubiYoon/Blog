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
        super(statusCode.getMassage());
        this.exceptionVo = new ExceptionVo(statusCode.getStatusCode(), statusCode.getCustomCode(), statusCode.getMassage());
    }

    /**
     * stackTrace가 필요 한 경우
     */
    public CmmnException(StatusCode statusCode, Throwable e) {
        super(statusCode.getMassage(), e);
        e.printStackTrace();
        this.exceptionVo = new ExceptionVo(statusCode.getStatusCode(), statusCode.getCustomCode(), statusCode.getMassage());
    }
}
