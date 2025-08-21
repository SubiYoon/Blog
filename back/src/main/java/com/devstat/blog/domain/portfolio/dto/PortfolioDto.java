package com.devstat.blog.domain.portfolio.dto;

import com.devstat.blog.utility.ItemCheck;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PortfolioDto {

    private Long companyId;
    private String name;
    private String logo;
    private Long logoId;
    private String date;
    private List<ProjectDto> projectList;

    @QueryProjection
    public PortfolioDto(Long companyId, String name, String logo, LocalDate companyIn, LocalDate companyOut) {
        this.companyId = companyId;
        this.name = name;
        this.logo = logo;
        if (ItemCheck.isNotEmpty(companyOut)) {
            this.date = companyIn + " ~ " + companyOut;
        } else {
            this.date = companyIn + " ~ 근무중";
        }
    }

    @QueryProjection
    public PortfolioDto(Long companyId, String name, LocalDate companyIn, LocalDate companyOut) {
        this.companyId = companyId;
        this.name = name;
        if (ItemCheck.isNotEmpty(companyOut)) {
            this.date = companyIn + " ~ " + companyOut;
        } else {
            this.date = companyIn + " ~ 근무중";
        }
    }
}
