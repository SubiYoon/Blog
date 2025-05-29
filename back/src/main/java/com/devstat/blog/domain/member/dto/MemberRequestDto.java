package com.devstat.blog.domain.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberRequestDto {
    private String id;
    private String name;
    private String password;
    private String birthday;
    private String role;
}
