package com.devstat.blog.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devstat.blog.domain.member.entity.Member;

public interface MemberJpaRepository extends JpaRepository<Member, String> {
}
