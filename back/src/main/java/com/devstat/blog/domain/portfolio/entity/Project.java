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
public class Project extends BaseEntity {

    @Id
    @Column(name = "project_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_seq_generator")
    @SequenceGenerator(name = "project_seq_generator", sequenceName = "project_seq", allocationSize = 1)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "project")
    private List<Item> items;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "project_start")
    private LocalDate projectStart;

    @Column(name = "project_end")
    private LocalDate projectEnd;

    @Column(name = "delete_yn")
    private String deleteYn;

    public static Project of(Company company, String projectName, LocalDate projectStart, LocalDate projectEnd) {
        Project project = new Project();
        project.company = company;
        project.projectName = projectName;
        project.projectStart = projectStart;
        project.projectEnd = projectEnd;
        project.deleteYn = "N";
        return project;
    }

    public void update(String projectName, LocalDate projectStart, LocalDate projectEnd) {
        this.projectName = projectName;
        this.projectStart = projectStart;
        this.projectEnd = projectEnd;
    }

    public void delete() {
        this.deleteYn = "Y";
    }
}
