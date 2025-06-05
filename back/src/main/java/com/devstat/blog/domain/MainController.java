package com.devstat.blog.domain;

import com.devstat.blog.domain.member.dto.MemberDto;
import com.devstat.blog.domain.member.service.MemberService;
import com.devstat.blog.domain.menu.dto.MenuDto;
import com.devstat.blog.domain.menu.service.MenuService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final MemberService memberService;
    private final MenuService menuService;

    @GetMapping("/{alias}")
    public ResponseEntity<MainDataDto> getMainDocs(@PathVariable("alias") String alias) {
        MemberDto member = memberService.findByAlias(alias);
        List<MenuDto> menuList = menuService.findByAlias(alias);

        return ResponseEntity.ok(new MainDataDto(member, menuList));
    }

    @Data
    private class MainDataDto {
        MemberDto memberInfo;
        List<MenuDto> menuList;

        public MainDataDto(MemberDto memberInfo, List<MenuDto> menuList) {
            this.memberInfo = memberInfo;
            this.menuList = menuList;
        }
    }
}
