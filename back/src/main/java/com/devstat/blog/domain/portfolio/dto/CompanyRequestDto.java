package com.devstat.blog.domain.portfolio.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CompanyRequestDto {

    @NotNull(message = "회사 이름은 공백일 수 없습니다.")
    private String companyName;

    @NotNull(message = "입사일은 공백일 수 없습니다.")
    private String date;

}
