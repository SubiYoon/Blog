package com.devstat.blog.domain.menu.service;

import com.devstat.blog.domain.menu.dto.MenuDto;
import com.devstat.blog.domain.menu.repository.MenuJpa;
import com.devstat.blog.domain.menu.repository.MenuQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuService {

    private final MenuJpa menuJpa;
    private final MenuQuery menuQuery;

    public List<MenuDto> findByAlias(String alias) {
        return menuQuery.findMenu(alias);
    }
}
