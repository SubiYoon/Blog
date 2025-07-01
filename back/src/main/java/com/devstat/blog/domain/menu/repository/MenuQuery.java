package com.devstat.blog.domain.menu.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import com.devstat.blog.domain.menu.dto.MenuDto;
import static com.devstat.blog.domain.menu.entity.QMenu.menu;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MenuQuery {

    private final JPAQueryFactory query;

    public List<MenuDto> getMenuList(String alias) {
        return query
                .selectFrom(menu)
                .where(menu.member.id.eq(alias))
                .fetch()
                .stream()
                .map(MenuDto::new)
                .toList();
    }
}
