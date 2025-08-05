package com.devstat.blog.domain.portfolio.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ProjectRequestDto {
    private Long companyId;
    private String projectName;
    private LocalDate projectStart;
    private LocalDate projectEnd;

}