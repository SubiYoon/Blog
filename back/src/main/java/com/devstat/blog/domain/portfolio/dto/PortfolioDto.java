package com.devstat.blog.domain.portfolio.dto;

import java.util.List;

import lombok.Data;

@Data
public class PortfolioDto {

    private Long companyId;
    private String name;
    private String logo;
    private String date;
    private List<ProjectDto> projectList;
}
