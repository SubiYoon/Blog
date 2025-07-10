package com.devstat.blog.init;

import com.devstat.blog.core.code.RoleCode;
import com.devstat.blog.domain.member.entity.Member;
import com.devstat.blog.domain.menu.entity.Menu;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@Qualifier("member")
@RequiredArgsConstructor
public class TestDataInit {

    @Value("${init.user.alias}")
    private String initUserAlias;

    @Value("${init.user.name}")
    private String initUserName;

    @Value("${init.user.password}")
    private String initUserPassword;

    @Value("${blog.file.path}")
    private String blogFilePath;

    private final EntityManager em;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
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

        File blogDir = new File(blogFilePath + "/" + initUserAlias);

        File[] listFiles = blogDir.listFiles();

        for (File file : listFiles) {
            if (file.isDirectory() && !file.getName().equals(".obsidian") && !file.getName().equals("Attached File")
                    && !file.getName().equals(".git")) {
                Menu menu = Menu.of(file.getName(), file.getName(), findMember);
                em.persist(menu);
            }
        }
        em.flush();

        System.out.println("✅ Member 생성 완료: " + findMember.getId());

        createSymlink(Paths.get(blogDir.getAbsolutePath() + "/" + "Attached File"),
                Paths.get("../front/static/Attached File"));
    }

    private void createSymlink(Path target, Path link) {
        try {
            // 심볼릭 링크가 이미 존재하면 삭제
            if (Files.exists(link, LinkOption.NOFOLLOW_LINKS)) {
                Files.delete(link);
            }

            // 심볼릭 링크 생성
            Files.createSymbolicLink(link, target);

            System.out.printf("🔗 심볼릭 링크 생성 완료: %s → %s%n", link, target);

        } catch (UnsupportedOperationException e) {
            System.err.println("❌ 현재 운영 체제에서는 심볼릭 링크를 지원하지 않습니다.");
        } catch (FileAlreadyExistsException e) {
            System.err.println("❌ 해당 링크가 이미 존재합니다: " + link);
        } catch (IOException e) {
            System.err.println("❌ 입출력 오류: " + e.getMessage());
        } catch (SecurityException e) {
            System.err.println("❌ 보안 정책에 의해 링크 생성이 거부되었습니다.");
        }
    }
}
