package com.devstat.blog.core.security;

import com.devstat.blog.domain.member.entity.Member;
import com.devstat.blog.domain.member.service.MemberService;
import com.devstat.blog.utility.ItemCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private final MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Member member = memberService.findById(id);
        if(ItemCheck.isEmpty(member)){
            throw new UsernameNotFoundException("user not found");
        }

        return member;
    }
}
