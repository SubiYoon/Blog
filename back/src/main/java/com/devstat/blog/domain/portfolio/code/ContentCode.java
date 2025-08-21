package com.devstat.blog.domain.portfolio.code;

public enum ContentCode {

    ITEM("아이템"),
    COMPANY("회사"),;

    private String content;

    ContentCode(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
