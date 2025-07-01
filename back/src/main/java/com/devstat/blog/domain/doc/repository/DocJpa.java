package com.devstat.blog.domain.doc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devstat.blog.domain.doc.entity.Doc;


public interface DocJpa extends JpaRepository<Doc, Long> {
}
