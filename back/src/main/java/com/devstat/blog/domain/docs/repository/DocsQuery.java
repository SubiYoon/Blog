package com.devstat.blog.domain.docs.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.devstat.blog.domain.docs.dto.DocsDto;
import static com.devstat.blog.domain.docs.entity.QDocs.docs;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DocsQuery {

    private final JPAQueryFactory query;

    public List<DocsDto> getDocsList(String alias) {
        return query
                .selectFrom(docs)
                .where(docs.member.id.eq(alias))
                .fetch()
                .stream()
                .map(DocsDto::new)
                .toList();
    }
}
