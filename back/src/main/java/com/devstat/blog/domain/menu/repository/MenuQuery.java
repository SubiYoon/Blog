package com.devstat.blog.domain.menu.repository;

import com.devstat.blog.domain.menu.dto.MenuDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.devstat.blog.domain.menu.entity.QMenu.menu;

@Repository
@RequiredArgsConstructor
public class MenuQuery {

    private final JPAQueryFactory query;

    public List<MenuDto> findMenu(String member_alias) {
        return query.
                selectFrom(menu)
                .where(menu.member.id.eq(member_alias))
                .fetch()
                .stream()
                .map(MenuDto::new)
                .toList();
    }
}
