package com.devstat.blog.domain.doc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devstat.blog.core.JsonParams;
import com.devstat.blog.core.ParamMap;
import com.devstat.blog.core.aspect.AccountDto;
import com.devstat.blog.core.code.StatusCode;
import com.devstat.blog.domain.doc.dto.DocDto;
import com.devstat.blog.domain.doc.dto.RequestDocDto;
import com.devstat.blog.domain.doc.service.DocService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/doc")
@RequiredArgsConstructor
public class DocController {

    private final DocService docService;

    @GetMapping("")
    public DocDto selectDoc(@ModelAttribute RequestDocDto requestDocDto) {
        return docService.selectDoc(new AccountDto(), requestDocDto);
    }

    @PostMapping("")
    public ResponseEntity<StatusCode> saveDocsTree(@RequestBody JsonParams docsTree) {
        ParamMap params = ParamMap.init(docsTree);
        StatusCode code = docService.saveDocsTree(new AccountDto(), params);

        return new ResponseEntity<>(code, code.getStatusCode());
    }

    @DeleteMapping("")
    public ResponseEntity<StatusCode> deleteDoc(@ModelAttribute RequestDocDto requestDocDto) {
        StatusCode code = docService.deleteDoc(new AccountDto(), requestDocDto);

        return new ResponseEntity<>(code, code.getStatusCode());
    }

    @GetMapping("/docsTree")
    public ResponseEntity<String> getDocsTree() {
        return ResponseEntity.ok(docService.getDocsTree(new AccountDto()));
    }

    @PostMapping("/restart")
    public ResponseEntity<StatusCode> restartFront() {
        docService.restartFront();
        return new ResponseEntity<>(StatusCode.SUCCESS, StatusCode.SUCCESS.getStatusCode());
    }

    @PostMapping("/pull")
    public ResponseEntity<StatusCode> gitPull() {
        docService.gitPull();
        return new ResponseEntity<>(StatusCode.SUCCESS, StatusCode.SUCCESS.getStatusCode());
    }
}
