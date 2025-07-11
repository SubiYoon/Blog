package com.devstat.blog.utility;

import org.springframework.stereotype.Component;

import com.devstat.blog.core.annotation.InjectAccountInfo;
import com.devstat.blog.core.aspect.AccountDto;

@Component
public class SecurityUtil {

    @InjectAccountInfo
    public AccountDto getCurrentMember(AccountDto accountDto) {
        return accountDto;
    }
}
