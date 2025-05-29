package com.devstat.blog.domain.member.dto;

import com.devstat.blog.domain.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberDto {

    private String id;
    private String name;
    private String birthday;
    private String role;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.birthday = member.getBirthday();
        this.role = member.getRole().toString();
        this.createDate = member.getCreateDate();
        this.updateDate = member.getUpdateDate();
    }
}
