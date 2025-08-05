package com.devstat.blog.domain.portfolio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devstat.blog.domain.portfolio.entity.Company;
import org.springframework.data.jpa.repository.Query;

public interface CompanyJpa extends JpaRepository<Company, Long> {

    // Spring Data JPA 네이밍 컨벤션 사용
    @Query("select c from Company c where c.id = :id and c.createBy = :createBy")
    Optional<Company> findById(Long id, String createBy);
    
    // 삭제되지 않은 모든 회사 조회
    List<Company> findByDeleteYnIsNullOrDeleteYnNot(String deleteYn);

}
