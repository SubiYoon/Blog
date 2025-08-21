package com.devstat.blog.domain.portfolio.repository;

import com.devstat.blog.domain.portfolio.code.ContentCode;
import com.devstat.blog.domain.portfolio.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ImageJpa extends JpaRepository<Image, Long> {

    @Query("select i from Image i where i.contentId = :contentId and i.contentGb = :contentGb and i.createBy = :createBy")
    List<Image> findByContentIdAndContentGb(Long contentId, ContentCode contentGb, String createBy);

    @Query("select i from Image i where i.id = :id and i.createBy = :accountId")
    Optional<Image> findById(Long id, String accountId);
}
