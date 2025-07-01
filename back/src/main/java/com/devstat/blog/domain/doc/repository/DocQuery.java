package com.devstat.blog.domain.doc.repository;

import com.devstat.blog.domain.doc.dto.DocDto;
import static com.devstat.blog.domain.doc.entity.QDoc.doc;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

// import static com.devstat.blog.domain.menu.entity.QDoc.doc;

@Repository
@RequiredArgsConstructor
public class DocQuery {

    private final JPAQueryFactory query;

    public List<DocDto> findDocs(String member_alias) {
        // return query.selectFrom(doc)
        // .where(doc.member.id.eq(member_alias))
        // .fetch()
        // .stream()
        // .map(DocDto::new)
        // .toList();
        return null;
    }
}
