package com.devstat.blog.domain.portfolio.entity;

import com.devstat.blog.core.baseEntity.BaseEntity;
import com.devstat.blog.domain.portfolio.code.ContentCode;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends BaseEntity {

    @Id
    @Column(name = "image_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_seq_generator")
    @SequenceGenerator(name = "image_seq_generator", sequenceName = "image_seq", allocationSize = 1)
    private Long id;

    @Column(name = "content_id")
    private Long contentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_gb")
    private ContentCode contentGb;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "delete_yn")
    private String deleteYn;

    public static Image of(Long contentId, ContentCode contentGb, String imagePath) {
        Image image = new Image();
        image.contentId = contentId;
        image.contentGb = contentGb;
        image.imagePath = imagePath;
        image.deleteYn = "N";
        return image;
    }

    public void update(String imagePath) {
        this.imagePath = imagePath;
    }

    public void delete() {
        this.deleteYn = "Y";
    }
}
