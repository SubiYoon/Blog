package com.devstat.blog.domain.doc.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DocQuery {

    private final JPAQueryFactory query;

}
