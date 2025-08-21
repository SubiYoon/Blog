package com.devstat.blog.domain.portfolio.repository;

import com.devstat.blog.domain.portfolio.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProjectJpa extends JpaRepository<Project, Long> {

    // Spring Data JPA 네이밍 컨벤션 사용
    @Query("select p from Project p where p.id = :id and p.createBy = :createBy")
    Optional<Project> findById(Long id, String createBy);

}
