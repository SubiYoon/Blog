package com.devstat.blog.domain.menu.dto;

import com.devstat.blog.domain.menu.entity.Menu;
import lombok.Data;

@Data
public class MenuDto {
    private long id;
    private String sidebarId;
    private String targetFolder;

    public MenuDto(Menu docs) {
        this.id = docs.getId();
        this.sidebarId = docs.getSidebarId();
        this.targetFolder = docs.getTargetFolder();
    }
}
