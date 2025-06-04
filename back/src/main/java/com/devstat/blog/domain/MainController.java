package com.devstat.blog.domain;

import com.devstat.blog.domain.member.dto.MemberDto;
import com.devstat.blog.domain.member.entity.Member;
import com.devstat.blog.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final MemberService memberService;

    @GetMapping("/{alias}")
    MemberDto getMemberDocs(@PathVariable(name = "alias") String alias) {
        Member member = memberService.findById(alias);
        return new MemberDto(member);
    }
}
