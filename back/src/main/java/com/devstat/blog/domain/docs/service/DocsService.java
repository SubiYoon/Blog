package com.devstat.blog.domain.docs.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.devstat.blog.domain.docs.dto.DocsDto;
import com.devstat.blog.domain.docs.repository.DocsJpa;
import com.devstat.blog.domain.docs.repository.DocsQuery;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocsService {

    private final DocsJpa docsJpa;
    private final DocsQuery docsQuery;

    public List<DocsDto> getDocsList(String alias) {
        return docsQuery.getDocsList(alias);
    }
}
