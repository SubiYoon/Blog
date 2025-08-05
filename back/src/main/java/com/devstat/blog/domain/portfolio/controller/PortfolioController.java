package com.devstat.blog.domain.portfolio.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.devstat.blog.core.aspect.AccountDto;
import com.devstat.blog.core.code.StatusCode;
import com.devstat.blog.domain.portfolio.dto.CompanyRequestDto;
import com.devstat.blog.domain.portfolio.dto.ItemRequestDto;
import com.devstat.blog.domain.portfolio.dto.ProjectRequestDto;
import com.devstat.blog.domain.portfolio.dto.PortfolioDto;
import com.devstat.blog.domain.portfolio.service.PortfolioService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @PostMapping("company")
    public ResponseEntity<StatusCode> insertCompany(
            @RequestParam("info") CompanyRequestDto dto,
            @RequestParam("logo") MultipartFile logo) {
        StatusCode result = portfolioService.insertCompany(dto, logo);
        return new ResponseEntity<>(result, result.getStatusCode());
    }

    @PatchMapping("company/{id}")
    public ResponseEntity<StatusCode> updateCompany(
            @PathVariable("id") Long id,
            @RequestParam("info") CompanyRequestDto dto,
            @RequestParam("logo") @Nullable MultipartFile logo) {
        StatusCode result = portfolioService.updateCompany(new AccountDto(), id, dto, logo);
        return new ResponseEntity<>(result, result.getStatusCode());
    }

    @DeleteMapping("company/{id}")
    public ResponseEntity<StatusCode> deleteCompany(@PathVariable("id") Long id) {
        StatusCode result = portfolioService.deleteCompany(new AccountDto(), id);
        return new ResponseEntity<>(result, result.getStatusCode());
    }

    @PostMapping("item")
    public ResponseEntity<StatusCode> insertItem(
            @RequestParam("info") ItemRequestDto dto,
            @RequestParam(value = "images", required = false) List<MultipartFile> images) {
        StatusCode result = portfolioService.insertItem(new AccountDto(), dto, images);
        return new ResponseEntity<>(result, result.getStatusCode());
    }

    @PatchMapping("item/{id}")
    public ResponseEntity<StatusCode> updateItem(
            @PathVariable("id") Long id,
            @RequestParam("info") ItemRequestDto dto,
            @RequestParam(value = "images", required = false) List<MultipartFile> images) {
        StatusCode result = portfolioService.updateItem(new AccountDto(), id, dto, images);
        return new ResponseEntity<>(result, result.getStatusCode());
    }

    @DeleteMapping("item/{id}")
    public ResponseEntity<StatusCode> deleteItem(@PathVariable("id") Long id) {
        StatusCode result = portfolioService.deleteItem(new AccountDto(), id);
        return new ResponseEntity<>(result, result.getStatusCode());
    }

    @PostMapping("project")
    public ResponseEntity<StatusCode> insertProject(@RequestParam("info") ProjectRequestDto dto) {
        StatusCode result = portfolioService.insertProject(new AccountDto(), dto);
        return new ResponseEntity<>(result, result.getStatusCode());
    }

    @PatchMapping("project/{id}")
    public ResponseEntity<StatusCode> updateProject(
            @PathVariable("id") Long id,
            @RequestParam("info") ProjectRequestDto dto) {
        StatusCode result = portfolioService.updateProject(new AccountDto(), id, dto);
        return new ResponseEntity<>(result, result.getStatusCode());
    }

    @DeleteMapping("project/{id}")
    public ResponseEntity<StatusCode> deleteProject(@PathVariable("id") Long id) {
        StatusCode result = portfolioService.deleteProject(new AccountDto(), id);
        return new ResponseEntity<>(result, result.getStatusCode());
    }
}
