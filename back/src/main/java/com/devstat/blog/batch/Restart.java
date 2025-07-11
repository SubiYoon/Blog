package com.devstat.blog.batch;

import org.springframework.stereotype.Service;

import com.devstat.blog.core.code.StatusCode;
import com.devstat.blog.core.exception.CmmnException;
import com.devstat.blog.domain.menu.repository.MenuJpa;
import com.devstat.blog.utility.DirectoryWatcher;
import com.devstat.blog.utility.NpmUtil;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.devstat.blog.domain.member.repository.MemberJpa;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

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

    @PostConstruct
    public void tryStartWatcher() {
        try {
            new DirectoryWatcher(blogFilePath, initUserAlias, menuJpa, memberJpa, npmUtil, em);
            npmUtil.docsRestart(false);
        } catch (Exception e) {
            throw new CmmnException(StatusCode.DOCS_RESTART_FAIL, e);
        }

    }

    @Scheduled(fixedRate = 60 * 60 * 1000) // 1시간마다
    public void batchGitPull() {
        try {
            npmUtil.gitPull(initUserAlias);

        } catch (Exception e) {
            throw new CmmnException(StatusCode.DOCS_RESTART_FAIL, e);
        }
    }

}
