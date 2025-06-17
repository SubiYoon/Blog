package com.devstat.blog.domain.member.dto;

import com.devstat.blog.domain.docs.dto.DocsDto;
import com.devstat.blog.domain.docs.entity.Docs;
import com.devstat.blog.domain.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MemberDto {

    private String alias;
    private String name;
    private String role;
    private String notionUrl;
    private String githubUrl;
    private List<DocsDto> docsList;

    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public MemberDto(Member member) {
        this.alias = member.getId();
        this.name = member.getName();
        this.role = member.getRole().toString();
        this.notionUrl = member.getNotionUrl();
        this.githubUrl = member.getGithubUrl();
        this.createDate = member.getCreateDate();
        this.updateDate = member.getUpdateDate();

        this.docsList = new ArrayList<>();
        for (Docs docs : member.getDocsList()) {
            this.docsList.add(new DocsDto(docs));
        }
    }
}
