package com.devstat.blog.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JsonParams implements Serializable {

    private Map<String, Object> params;

    protected Map<String, Object> getParams() {
        return params;
    }

    protected void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
