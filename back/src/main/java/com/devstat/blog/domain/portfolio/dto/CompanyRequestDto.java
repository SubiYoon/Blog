package com.devstat.blog.domain.portfolio.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CompanyRequestDto {

    private String companyName;
    private LocalDate companyIn;
    private LocalDate companyOut;

}
