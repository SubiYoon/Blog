package com.devstat.blog.domain.portfolio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.devstat.blog.domain.portfolio.code.ContentCode;
import com.devstat.blog.domain.portfolio.entity.Image;

public interface ImageJpa extends JpaRepository<Image, Long> {

    // Spring Data JPA 네이밍 컨벤션 사용
    List<Image> findByContentIdAndContentGb(Long contentId, ContentCode contentGb);

    @Query("select i from Image i where i.contentId = :contentId and i.contentGb = :contentGb and i.createBy = :createBy")
    List<Image> findByContentIdAndContentGb(Long contentId, ContentCode contentGb, String createBy);

    // 삭제되지 않은 이미지 조회
    List<Image> findByContentIdAndContentGbAndDeleteYnIsNullOrDeleteYnNot(Long contentId, ContentCode contentGb,
            String deleteYn);

}
