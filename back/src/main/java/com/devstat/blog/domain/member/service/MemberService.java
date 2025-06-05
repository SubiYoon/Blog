package com.devstat.blog.domain.member.service;

import com.devstat.blog.core.code.StatusCode;
import com.devstat.blog.core.exception.CmmnException;
import com.devstat.blog.domain.member.dto.MemberDto;
import com.devstat.blog.domain.member.entity.Member;
import com.devstat.blog.domain.member.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberJpaRepository memberJpa;

    public Member findById(String id) {
        return memberJpa.findById(id).orElseThrow(() -> new CmmnException(StatusCode.USER_NOT_FOUND));
    }

    public MemberDto findByAlias(String alias) {
        Member findMember = memberJpa.findById(alias).orElseThrow(() -> new CmmnException(StatusCode.USER_NOT_FOUND));
        return new MemberDto(findMember);
    }
}
