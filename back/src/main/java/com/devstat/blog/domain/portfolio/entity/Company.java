package com.devstat.blog.domain.portfolio.entity;

import com.devstat.blog.core.baseEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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


    @Column(name = "company_in")
    private LocalDate companyIn;

    @Column(name = "company_out")
    private LocalDate companyOut;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "company")
    private List<Project> projectList = new ArrayList<>();

    @Column(name = "delete_yn")
    private String deleteYn;

    public static Company of(String companyName, LocalDate companyIn, LocalDate companyOut) {
        Company company = new Company();
        company.companyName = companyName;
        company.companyIn = companyIn;
        company.companyOut = companyOut;
        return company;
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
