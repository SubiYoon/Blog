package com.devstat.blog.domain.portfolio.controller;

import com.devstat.blog.core.aspect.AccountDto;
import com.devstat.blog.core.code.StatusCode;
import com.devstat.blog.domain.portfolio.dto.*;
import com.devstat.blog.domain.portfolio.service.PortfolioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping("")
    public ResponseEntity<List<PortfolioDto>> getPortfolioInfoByUser() {
        return new ResponseEntity<>(portfolioService.getPortfolioInfo(new AccountDto()), HttpStatus.OK);
    }

    @PostMapping("company")
    public ResponseEntity<StatusCode> insertCompany(
            @ModelAttribute @Valid CompanyRequestDto dto,
            @RequestParam("logo") MultipartFile logo) {
        StatusCode result = portfolioService.insertCompany(new AccountDto(), dto, logo);
        return new ResponseEntity<>(result, result.getStatusCode());
    }

    @PatchMapping("company/{id}")
    public ResponseEntity<StatusCode> updateCompany(
            @PathVariable("id") Long id,
            @ModelAttribute @Valid CompanyRequestDto dto) {
        StatusCode result = portfolioService.updateCompany(new AccountDto(), id, dto);
        return new ResponseEntity<>(result, result.getStatusCode());
    }

    @DeleteMapping("company/{id}")
    public ResponseEntity<StatusCode> deleteCompany(@PathVariable("id") Long id) {
        StatusCode result = portfolioService.deleteCompany(new AccountDto(), id);
        return new ResponseEntity<>(result, result.getStatusCode());
    }

    @PostMapping("item")
    public ResponseEntity<StatusCode> insertItem(
            @ModelAttribute ItemRequestDto dto) {
        StatusCode result = portfolioService.insertItem(new AccountDto(), dto);
        return new ResponseEntity<>(result, result.getStatusCode());
    }

    @PatchMapping("item/{id}")
    public ResponseEntity<StatusCode> updateItem(
            @PathVariable("id") Long id,
            @ModelAttribute ItemRequestDto dto) {
        StatusCode result = portfolioService.updateItem(new AccountDto(), id, dto);
        return new ResponseEntity<>(result, result.getStatusCode());
    }

    @DeleteMapping("item/{id}")
    public ResponseEntity<StatusCode> deleteItem(@PathVariable("id") Long id) {
        StatusCode result = portfolioService.deleteItem(new AccountDto(), id);
        return new ResponseEntity<>(result, result.getStatusCode());
    }

    @PostMapping("image")
    public ResponseEntity<ImageDto> uploadImage(
            @RequestParam("contentId") Long contentId,
            @RequestParam("contentGb") String contentGb,
            @RequestParam("image") MultipartFile image) {
        ImageDto result = portfolioService.saveItemImages(new AccountDto(), contentId, contentGb, image);
        return new ResponseEntity<>(result, StatusCode.SUCCESS.getStatusCode());
    }

    @DeleteMapping("image/{imageId}")
    public ResponseEntity<StatusCode> deleteImage(@PathVariable("imageId") Long id) {
        StatusCode result = portfolioService.deleteImage(new AccountDto(), id);
        return new ResponseEntity<>(result, result.getStatusCode());
    }

    @PostMapping("project")
    public ResponseEntity<StatusCode> insertProject(@ModelAttribute ProjectRequestDto dto) {
        StatusCode result = portfolioService.insertProject(new AccountDto(), dto);
        return new ResponseEntity<>(result, result.getStatusCode());
    }

    @PatchMapping("project/{id}")
    public ResponseEntity<StatusCode> updateProject(
            @PathVariable("id") Long id,
            @ModelAttribute ProjectRequestDto dto) {
        StatusCode result = portfolioService.updateProject(new AccountDto(), id, dto);
        return new ResponseEntity<>(result, result.getStatusCode());
    }

    @DeleteMapping("project/{id}")
    public ResponseEntity<StatusCode> deleteProject(@PathVariable("id") Long id) {
        StatusCode result = portfolioService.deleteProject(new AccountDto(), id);
        return new ResponseEntity<>(result, result.getStatusCode());
    }
}
