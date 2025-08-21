package com.devstat.blog.domain.portfolio.dto;

import java.time.LocalDate;
import java.util.List;

import com.devstat.blog.utility.ItemCheck;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class ProjectDto {

    private Long projectId;
    private Long companyId; // Company와의 매핑을 위한 필드 추가
    private String name;
    private String date;
    private List<ItemDto> itemList;

    @QueryProjection
    public ProjectDto(Long projectId, Long companyId, String name, LocalDate startDate, LocalDate endDate) {
        this.projectId = projectId;
        this.companyId = companyId;
        this.name = name;

        if (ItemCheck.isNotEmpty(endDate)) {
            this.date = startDate + " ~ " + endDate;
        } else {
            this.date = startDate + " ~ 진행중";
        }
    }
}
