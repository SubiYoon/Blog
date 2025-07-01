package com.devstat.blog.domain.member.repository;

import com.devstat.blog.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpa extends JpaRepository<Member, String> {
}
