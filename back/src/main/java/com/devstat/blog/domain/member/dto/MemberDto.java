package com.devstat.blog.domain.member.dto;

import com.devstat.blog.domain.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberDto {

    private String alias;
    private String name;
    private String role;
    private String linkPath;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public MemberDto(Member member) {
        this.alias = member.getId();
        this.name = member.getName();
        this.role = member.getRole().toString();
        this.linkPath = member.getLinkPath();
        this.createDate = member.getCreateDate();
        this.updateDate = member.getUpdateDate();
    }
}
