package com.devstat.blog.domain.docs.dto;

import com.devstat.blog.domain.docs.entity.Docs;
import lombok.Data;

@Data
public class DocsDto {
    private long id;
    private String sidebarId;
    private String targetFolder;

    public DocsDto(Docs docs) {
        this.id = docs.getId();
        this.sidebarId = docs.getSidebarId();
        this.targetFolder = docs.getTargetFolder();
    }
}
