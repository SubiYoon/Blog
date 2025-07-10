package com.devstat.blog.batch;

import org.springframework.stereotype.Service;

import com.devstat.blog.core.code.StatusCode;
import com.devstat.blog.core.exception.CmmnException;
import jakarta.annotation.PostConstruct;
import com.devstat.blog.domain.menu.repository.MenuJpa;
import com.devstat.blog.utility.DirectoryWatcher;
import com.devstat.blog.utility.NpmUtil;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.devstat.blog.domain.member.entity.Member;
import com.devstat.blog.domain.member.repository.MemberJpa;
import com.devstat.blog.domain.menu.entity.Menu;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class Restart {

    @Value("${init.user.alias}")
    private String initUserAlias;

    @Value("${init.user.name}")
    private String initUserName;

    @Value("${init.user.password}")
    private String initUserPassword;

    @Value("${blog.file.path}")
    private String blogFilePath;

    private final MenuJpa menuJpa;
    private final MemberJpa memberJpa;

    private final NpmUtil npmUtil;

    private final EntityManager em;

    @Scheduled(fixedRate = 1 * 5 * 1000) // 30분마다
    public void tryStartWatcher() {

        try {
            // ✅ 1. 항상 실행되어야 하는 부분
            npmUtil.gitPull(initUserAlias);

            new DirectoryWatcher(blogFilePath, initUserAlias, menuJpa, memberJpa, npmUtil, em);

        } catch (Exception e) {
            throw new CmmnException(StatusCode.DOCS_RESTART_FAIL, e);
        }
    }

}
