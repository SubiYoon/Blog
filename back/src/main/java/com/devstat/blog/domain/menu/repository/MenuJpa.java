package com.devstat.blog.domain.menu.repository;

import com.devstat.blog.domain.menu.entity.Menu;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuJpa extends JpaRepository<Menu, Long> {
}
