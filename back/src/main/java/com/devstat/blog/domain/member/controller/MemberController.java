package com.devstat.blog.domain.member.controller;

import com.devstat.blog.core.code.StatusCode;
import com.devstat.blog.domain.member.dto.MemberDto;
import com.devstat.blog.domain.member.dto.MemberRequestDto;
import com.devstat.blog.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("")
    public ResponseEntity<List<MemberDto>> allMember() {
        log.info("MemberController.allMember -> {}", "전체 사용자 조회");

        return ResponseEntity.ok(memberService.findAll());
    }

    @PostMapping("/join")
    public ResponseEntity<StatusCode> join(@RequestBody MemberRequestDto dto) {
        log.info("MemberController.join -> {}", "회원가입 시도");
        memberService.join(dto);

        return ResponseEntity.ok(StatusCode.SUCCESS);
    }

    @GetMapping("/temp")
    public ResponseEntity<String> temp() {
        return ResponseEntity.ok("가냐?");
    }
}
