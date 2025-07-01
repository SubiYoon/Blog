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
    private String notionUrl;
    private String githubUrl;
    private String directoryPath;
    private String blogInitPath;

    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public MemberDto(Member member) {
        this.alias = member.getId();
        this.name = member.getName();
        this.role = member.getRole().toString();
        this.notionUrl = member.getNotionUrl();
        this.githubUrl = member.getGithubUrl();
        this.directoryPath = member.getDirectoryPath();
        this.blogInitPath = member.getBlogInitPath();

        this.createDate = member.getCreateDate();
        this.updateDate = member.getUpdateDate();
    }
}
