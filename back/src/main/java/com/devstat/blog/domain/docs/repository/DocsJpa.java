package com.devstat.blog.domain.docs.repository;

import com.devstat.blog.domain.docs.entity.Docs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocsJpa extends JpaRepository<Docs, Long> {
}
