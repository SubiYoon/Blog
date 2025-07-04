package com.devstat.blog.domain.doc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devstat.blog.domain.doc.entity.DocHistory;

public interface DocJpa extends JpaRepository<DocHistory, Long> {
}
