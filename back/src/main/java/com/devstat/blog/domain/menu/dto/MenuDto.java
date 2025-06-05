package com.devstat.blog.domain.menu.dto;

import com.devstat.blog.domain.menu.entity.Menu;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuDto {

    private String label;
    private String to;
    private String description;

    public MenuDto(Menu menu) {
        this.label = menu.getLabel();
        this.to = menu.getPagePath();
        this.description = menu.getDescription();
    }
}
