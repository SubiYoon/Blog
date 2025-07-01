package com.devstat.blog.domain.doc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devstat.blog.domain.doc.dto.DocDto;

import com.devstat.blog.domain.doc.repository.DocJpa;
import com.devstat.blog.domain.doc.repository.DocQuery;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DocService {

    private final DocJpa docJpa;
    private final DocQuery docQuery;

    public List<DocDto> findByAlias(String alias) {
        return docQuery.findDocs(alias);
    }
}
