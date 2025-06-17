package com.devstat.blog.domain.docs.dto;

import com.devstat.blog.domain.docs.entity.Docs;
import lombok.Data;

@Data
public class DocsDto {
    private String directoryPath;

    public DocsDto(Docs docs) {
        this.directoryPath = docs.getDirectoryPath();
    }
}
