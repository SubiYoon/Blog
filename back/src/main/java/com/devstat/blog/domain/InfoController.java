package com.devstat.blog.domain;

import com.devstat.blog.domain.member.dto.MemberDto;
import com.devstat.blog.domain.member.service.MemberService;
import com.devstat.blog.domain.menu.dto.MenuDto;
import com.devstat.blog.domain.menu.service.MenuService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/info")
@RequiredArgsConstructor
public class InfoController {

    private final MemberService memberService;
    private final MenuService menuService;

    @GetMapping("/{alias}")
    public ResponseEntity<MemberDto> getMainDocs(@PathVariable("alias") String alias) {
        MemberDto member = memberService.findByAlias(alias);

        return ResponseEntity.ok(member);
    }

    @GetMapping("/{alias}/notion")
    public ResponseEntity<String> getNotionPath(@PathVariable("alias") String alias) {
        MemberDto member = memberService.findByAlias(alias);

        return ResponseEntity.ok(member.getNotionUrl());
    }

    @GetMapping("/{alias}/docs")
    public ResponseEntity<List<MenuDto>> getDocsList(@PathVariable("alias") String alias) {
        return ResponseEntity.ok(menuService.getMenuList(alias));
    }

}
