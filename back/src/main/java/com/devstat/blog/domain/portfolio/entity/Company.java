package com.devstat.blog.domain.portfolio.entity;

import java.time.LocalDate;
import java.util.List;

import com.devstat.blog.core.baseEntity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Company extends BaseEntity {

    @Id
    @Column(name = "company_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_seq_generator")
    @SequenceGenerator(name = "company_seq_generator", sequenceName = "company_seq", allocationSize = 1)
    private Long id;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "company_logo_path")
    private String companyLogoPath;

    @Column(name = "company_in")
    private LocalDate companyIn;

    @Column(name = "company_out")
    private LocalDate companyOut;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "company")
    private List<Project> projectList;

    @Column(name = "delete_yn")
    private String deleteYn;

    public static Company of(String companyName, String companyLogoPath, LocalDate companyIn, LocalDate companyOut) {
        Company company = new Company();
        company.companyName = companyName;
        company.companyLogoPath = companyLogoPath;
        company.companyIn = companyIn;
        company.companyOut = companyOut;
        return company;
    }

    public void update(String companyName, String companyLogoPath, LocalDate companyIn, LocalDate companyOut) {
        this.companyName = companyName;
        this.companyLogoPath = companyLogoPath;
        this.companyIn = companyIn;
        this.companyOut = companyOut;
    }

    public void update(String companyName, LocalDate companyIn, LocalDate companyOut) {
        this.companyName = companyName;
        this.companyIn = companyIn;
        this.companyOut = companyOut;
    }

    public void delete() {
        this.deleteYn = "Y";
    }
}
