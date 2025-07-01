package com.devstat.blog.domain.menu.entity;

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
public class Menu extends BaseEntity {

    @Id
    @Column(name = "menu_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menu_seq_generator")
    @SequenceGenerator(name = "menu_seq_generator", sequenceName = "menu_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, name = "sidebar_id")
    private String sidebarId;

    @Column(nullable = false, name = "target_folder")
    private String targetFolder;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_alias", nullable = false)
    private Member member;

    public static Menu of(String sidebarId, String targetFolder, Member findMember) {
        Menu menu = new Menu();
        menu.sidebarId = sidebarId;
        menu.targetFolder = targetFolder;
        menu.member = findMember;
        return menu;
    }

}
