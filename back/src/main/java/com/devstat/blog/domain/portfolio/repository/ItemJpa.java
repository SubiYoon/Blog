package com.devstat.blog.domain.portfolio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.devstat.blog.domain.portfolio.entity.Item;

public interface ItemJpa extends JpaRepository<Item, Long> {

    // Spring Data JPA 네이밍 컨벤션 사용
    @Query("select i from Item i where i.project.id = :id and i.createBy = :createBy")
    Optional<Item> findById(Long id, String createBy);

    // 삭제되지 않은 모든 아이템 조회
    List<Item> findByDeleteYnIsNullOrDeleteYnNot(String deleteYn);

}
