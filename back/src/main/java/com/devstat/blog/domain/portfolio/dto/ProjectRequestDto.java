package com.devstat.blog.domain.portfolio.dto;

import lombok.Data;

@Data
public class ProjectRequestDto {
    private Long companyId;
    private String projectName;
    private String date;

}