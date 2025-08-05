package com.devstat.blog.domain.portfolio.dto;

import lombok.Data;

@Data
public class ItemRequestDto {
    private Long projectId;
    private String title;
    private String cont;

}