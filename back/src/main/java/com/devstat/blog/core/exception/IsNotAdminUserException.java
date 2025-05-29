package com.devstat.blog.core.exception;

import com.devstat.blog.core.code.StatusCode;
import lombok.Getter;

@Getter
public class IsNotAdminUserException extends CmmnException {
    public IsNotAdminUserException(StatusCode statusCode) {
        super(statusCode);
    }

    public IsNotAdminUserException(StatusCode statusCode, Throwable e) {
        super(statusCode);
        e.printStackTrace();
    }
}
