package com.devstat.blog.domain.docs.entity;

import com.devstat.blog.core.baseEntity.BaseEntity;
import com.devstat.blog.domain.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Docs extends BaseEntity {

    @Id
    @Column(name = "docs_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "docs_seq_generator")
    @SequenceGenerator(name = "docs_seq_generator", sequenceName = "docs_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, name = "sidebar_id")
    private String sidebarId;

    @Column(nullable = false, name = "target_folder")
    private String targetFolder;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_alias", nullable = false)
    private Member member;

    public static Docs of(String sidebarId, String targetFolder, Member findMember) {
        Docs docs = new Docs();
        docs.sidebarId = sidebarId;
        docs.targetFolder = targetFolder;
        docs.member = findMember;
        return docs;
    }

}
