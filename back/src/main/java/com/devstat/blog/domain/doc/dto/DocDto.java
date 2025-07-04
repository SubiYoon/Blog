package com.devstat.blog.domain.doc.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocDto {

    private String title;
    private String content;
    private LocalDateTime updateAt;
}
