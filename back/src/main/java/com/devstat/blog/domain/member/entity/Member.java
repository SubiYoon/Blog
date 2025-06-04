package com.devstat.blog.domain.member.entity;

import com.devstat.blog.core.baseEntity.BaseTimeEntity;
import com.devstat.blog.core.code.RoleCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity implements UserDetails, Persistable<String> {

    @Id
    @Column(name = "member_id")
    private String id;
    @Column(length = 10, nullable = false)
    private String name;
    private String password;

    @Enumerated(EnumType.STRING)
    private RoleCode role;

    private Member(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = RoleCode.NOT_ALLOW;
    }

    public static Member of(String id, String name, String password) {
        return new Member(id, name, password);
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
