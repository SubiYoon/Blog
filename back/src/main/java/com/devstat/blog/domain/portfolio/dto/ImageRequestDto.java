package com.devstat.blog.domain.portfolio.dto;

import com.devstat.blog.domain.portfolio.code.ContentCode;

import lombok.Data;

@Data
public class ImageRequestDto {

    private Long contentId;
    private ContentCode contentGb;

}