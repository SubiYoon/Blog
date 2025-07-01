package com.devstat.blog.domain.menu.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.devstat.blog.domain.menu.dto.MenuDto;
import com.devstat.blog.domain.menu.repository.MenuJpa;
import com.devstat.blog.domain.menu.repository.MenuQuery;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuJpa menuJpa;
    private final MenuQuery menuQuery;

    public List<MenuDto> getMenuList(String alias) {
        return menuQuery.getMenuList(alias);
    }
}
