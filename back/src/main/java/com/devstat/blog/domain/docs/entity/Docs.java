package com.devstat.blog.domain.docs.entity;

import com.devstat.blog.core.baseEntity.BaseEntity;
import com.devstat.blog.domain.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Docs extends BaseEntity implements Persistable<String> {

    @Id
    @Column(nullable = false, name = "directory_path")
    private String directoryPath;

    @Column(nullable = false, name = "sidebar_id")
    private String sidebarId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_alias", nullable = false)
    private Member member;

    public static Docs of(String path, String sidebarId, Member findMember) {
        Docs docs = new Docs();
        docs.directoryPath = path;
        docs.sidebarId = sidebarId;
        docs.member = findMember;
        return docs;
    }

    @Override
    public String getId() {
        return directoryPath;
    }

    @Override
    public boolean isNew() {
        return createDate == null;
    }
}
