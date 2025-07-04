package com.devstat.blog.init;

import com.devstat.blog.core.code.RoleCode;
import com.devstat.blog.domain.member.entity.Member;
import com.devstat.blog.domain.menu.entity.Menu;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Qualifier("member")
@Component
@Profile("dev")
@RequiredArgsConstructor
public class TestDataInit {

    @Value("${init.user.alias}")
    private String initUserAlias;

    @Value("${init.user.name}")
    private String initUserName;

    @Value("${init.user.password}")
    private String initUserPassword;

    private final EntityManager em;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initAfterAppReady() {
        Member member = Member.of(
                initUserAlias,
                initUserName,
                passwordEncoder.encode(initUserPassword),
                RoleCode.ADMIN,
                "https://abctodev.notion.site/B-E-a7b07fca67da4327b00f1448d645de59?source=copy_link",
                "https://github.com/SubiYoon/SubiYoon.github.io.git",
                "/Users/ABCD/blogTempFile/ABCD/",
                "/velog/Language/SOLID 원칙");

        em.persist(member);
        em.flush();

        Member findMember = em.find(Member.class, initUserAlias);

        Menu menu1 = Menu.of("Study", "velog", findMember);
        Menu menu2 = Menu.of("test", "test", findMember);

        em.persist(menu1);
        em.persist(menu2);
        em.flush();

        System.out.println("✅ Member 생성 완료: " + findMember.getId());
        // System.out.println("✅ Menu 생성 완료: " + em.find(Doc.class,
        // menu.getId()).getLabel());
        System.out.println("✅ Menu 생성 완료: " + em.find(Menu.class, menu1.getId()).getId());
    }
}
