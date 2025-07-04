package com.devstat.blog.domain.doc.repository;

import static com.devstat.blog.domain.doc.entity.QDoc.doc;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DocQuery {

    private final JPAQueryFactory query;

}
