package com.devstat.blog.domain.portfolio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto {

    private Long itemId; // Item과의 매핑을 위한 필드 추가
    private String img;
}
