package com.devstat.blog.domain.member.entity;

import com.devstat.blog.core.baseEntity.BaseTimeEntity;
import com.devstat.blog.core.code.RoleCode;
import com.devstat.blog.domain.docs.entity.Docs;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity implements UserDetails, Persistable<String> {

    @Id
    @Column(name = "member_alias")
    private String id;
    @Column(length = 10, nullable = false)
    private String name;
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private RoleCode role;

    @Column(name = "notion_url")
    private String notionUrl;

    @Column(name = "github_url")
    private String githubUrl;

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER)
    private List<Docs> docsList = new ArrayList<>();

    private Member(String id, String name, String password, RoleCode role, String notionUrl, String githubUrl) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
        this.notionUrl = notionUrl;
        this.githubUrl = githubUrl;
    }

    public static Member of(String id, String name, String password, String notionUrl, String githubUrl) {
        return new Member(id, name, password, RoleCode.USER, notionUrl, githubUrl);
    }

    public static Member of(String id, String name, String password, RoleCode role, String notionUrl, String githubUrl) {
        return new Member(id, name, password, role, notionUrl, githubUrl);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(role.toString()));
        return roles;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isNew() {
        return createDate == null;
    }

    public void changePassword(String password) {
        this.password = password;
    }

}
