package com.devstat.blog.domain.member.service;

import com.devstat.blog.core.annotation.CheckAdmin;
import com.devstat.blog.core.code.RoleCode;
import com.devstat.blog.core.code.StatusCode;
import com.devstat.blog.core.exception.CmmnException;
import com.devstat.blog.domain.member.dto.MemberDto;
import com.devstat.blog.domain.member.dto.MemberRequestDto;
import com.devstat.blog.domain.member.entity.Member;
import com.devstat.blog.domain.member.repository.MemberJpaRepository;
import com.devstat.blog.domain.member.repository.MemberQueryRepository;
import com.devstat.blog.utility.ItemCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberJpaRepository memberJpa;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void join(MemberRequestDto dto) {
        Member findMember = memberJpa.findById(dto.getId()).orElse(null);

        if (ItemCheck.isNotEmpty(findMember)) {
            throw new CmmnException(StatusCode.DUPLICATED_ID);
        }

        Member saveMember = Member.of(dto.getId(), dto.getName(), passwordEncoder.encode(dto.getPassword()), dto.getBirthday());

        memberJpa.save(saveMember);
    }

    public Member findById(String id) {
        return memberJpa.findById(id).orElseThrow(() -> new CmmnException(StatusCode.USER_NOT_FOUND));
    }

    @CheckAdmin
    @Transactional
    public void updateRole(String memberId, RoleCode role) {
        Member findMember = memberJpa.findById(memberId).orElseThrow(() -> new CmmnException(StatusCode.USER_NOT_FOUND));

        findMember.changeRole(role);
    }

    @CheckAdmin
    public List<MemberDto> findAll() {
        return memberJpa.findAll().stream().map(MemberDto::new).toList();
    }

}
