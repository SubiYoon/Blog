package com.devstat.blog.domain.portfolio.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProjectDto {

    private Long projectId;
    private Long companyId; // Company와의 매핑을 위한 필드 추가
    private String name;
    private String date;
    private List<ItemDto> itemList;
}
