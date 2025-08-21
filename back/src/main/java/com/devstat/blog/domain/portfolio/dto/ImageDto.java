package com.devstat.blog.domain.portfolio.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
public class ImageDto {

    private Long imageId;
    private Long itemId; // Item과의 매핑을 위한 필드 추가
    private String img;

    @QueryProjection
    public ImageDto(Long imageId, Long itemId, String img) {
        this.imageId = imageId;
        this.itemId = itemId;
        this.img = img;
    }
}
