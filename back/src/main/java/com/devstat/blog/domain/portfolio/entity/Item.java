package com.devstat.blog.domain.portfolio.entity;

import com.devstat.blog.core.baseEntity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseEntity {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_seq_generator")
    @SequenceGenerator(name = "item_seq_generator", sequenceName = "item_seq", allocationSize = 1)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "title")
    private String title;

    @Column(name = "cont")
    private String cont;

    @Column(name = "delete_yn")
    private String deleteYn;

    public static Item of(Project project, String title, String cont) {
        Item item = new Item();
        item.project = project;
        item.title = title;
        item.cont = cont;
        item.deleteYn = "N";
        return item;
    }

    public void update(String title, String cont) {
        this.title = title;
        this.cont = cont;
    }

    public void delete() {
        this.deleteYn = "Y";
    }
}
