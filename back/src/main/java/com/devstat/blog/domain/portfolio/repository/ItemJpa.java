package com.devstat.blog.domain.portfolio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.devstat.blog.domain.portfolio.entity.Item;

public interface ItemJpa extends JpaRepository<Item, Long> {

    // Spring Data JPA 네이밍 컨벤션 사용
    @Query("select i from Item i where i.id = :id and i.createBy = :createBy")
    Optional<Item> findById(Long id, String createBy);
}
