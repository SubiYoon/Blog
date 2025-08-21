package com.devstat.blog.domain.portfolio.repository;

import com.devstat.blog.domain.portfolio.code.ContentCode;
import com.devstat.blog.domain.portfolio.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageJpa extends JpaRepository<Image, Long> {

    @Query("select i from Image i where i.contentId = :contentId and i.contentGb = :contentGb and i.createBy = :createBy")
    List<Image> findByContentIdAndContentGb(Long contentId, ContentCode contentGb, String createBy);
}
