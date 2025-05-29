package com.devstat.blog.core.exception;

import com.devstat.blog.core.code.StatusCode;
import lombok.Getter;

@Getter
public class CustomJwtException extends CmmnException {
    public CustomJwtException(StatusCode statusCode) {
        super(statusCode);
    }
}
