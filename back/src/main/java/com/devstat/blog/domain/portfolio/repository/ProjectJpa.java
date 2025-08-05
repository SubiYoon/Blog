package com.devstat.blog.domain.portfolio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.devstat.blog.domain.portfolio.entity.Project;

public interface ProjectJpa extends JpaRepository<Project, Long> {

    // Spring Data JPA 네이밍 컨벤션 사용
    @Query("select p from Project p where p.company.id = :id and p.createBy = :createBy")
    Optional<Project> findById(Long id, String createBy);

    // 삭제되지 않은 모든 프로젝트 조회
    List<Project> findByDeleteYnIsNullOrDeleteYnNot(String deleteYn);

}
