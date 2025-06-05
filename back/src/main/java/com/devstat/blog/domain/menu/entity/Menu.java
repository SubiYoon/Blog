package com.devstat.blog.domain.menu.entity;

import com.devstat.blog.core.baseEntity.BaseEntity;
import com.devstat.blog.domain.member.entity.Member;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_alias")
    private Member member;

    @Column(length = 10, nullable = false)
    private String label;

    @Column(name = "page_path", nullable = false)
    private String pagePath;

    private String description;

    @Column(name = "is_use", nullable = false)
    private Character isUse;

    private Menu(Member member, String label, String pagePath, String description) {
        this.member = member;
        this.label = label;
        this.pagePath = pagePath;
        this.description = description;
        this.isUse = 'Y';
    }

    public static Menu of(Member member, String label, String pagePath, String description) {
        return new Menu(member, label, pagePath, description);
    }

    public void changeUse(Character isUse) {
        this.isUse = isUse;
    }
}
