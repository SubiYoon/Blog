package com.devstat.blog.domain.portfolio.dto;

import java.util.List;

import lombok.Data;

@Data
public class ItemDto {

    private Long itemId;
    private Long projectId; // Project와의 매핑을 위한 필드 추가
    private String name;
    private String cont;
    private List<ImageDto> imageList;
}
