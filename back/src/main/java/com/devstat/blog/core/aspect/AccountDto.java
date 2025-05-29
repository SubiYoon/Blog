package com.devstat.blog.core.aspect;

import com.devstat.blog.core.code.RoleCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PROTECTED)
public class AccountDto {
    private String accountId;
    private String accountName;
    private String accountBirthday;
    private RoleCode accountRole;
}
