package com.devstat.blog.domain.portfolio.repository;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.devstat.blog.domain.portfolio.dto.ImageDto;
import com.devstat.blog.domain.portfolio.dto.ItemDto;
import com.devstat.blog.domain.portfolio.dto.PortfolioDto;
import com.devstat.blog.domain.portfolio.dto.ProjectDto;
import com.devstat.blog.domain.portfolio.entity.QCompany;
import com.devstat.blog.domain.portfolio.entity.QProject;
import com.devstat.blog.domain.portfolio.entity.QItem;
import com.devstat.blog.domain.portfolio.entity.QImage;
import com.devstat.blog.domain.portfolio.code.ContentCode;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CompanyQuery {

    private final JPAQueryFactory query;
    
    private final QCompany company = QCompany.company;
    private final QProject project = QProject.project;
    private final QItem item = QItem.item;
    private final QImage image = QImage.image;
    
    public List<PortfolioDto> findPortfolioInfo() {
        return query
            .select(Projections.bean(PortfolioDto.class,
                company.id.as("companyId"),
                company.companyName.as("name"),
                company.companyLogoPath.as("logo"),
                company.companyIn.stringValue().concat(" ~ ").concat(company.companyOut.stringValue()).as("date")
            ))
            .from(company)
            .where(company.deleteYn.isNull().or(company.deleteYn.ne("Y")))
            .orderBy(company.companyIn.asc())
            .fetch();
    }
    
    public List<ProjectDto> findProjectsByCompanyId(Long companyId) {
        // 현재 Project 엔티티에 companyId 필드가 없으므로 모든 프로젝트를 반환
        // 실제 구현 시에는 Project 엔티티에 companyId 외래키가 필요합니다
        return query
            .select(Projections.bean(ProjectDto.class,
                project.id.as("projectId"),
                project.projectName.as("name"),
                project.projectStart.stringValue().concat(" ~ ").concat(project.projectEnd.stringValue()).as("date")
            ))
            .from(project)
            .where(project.deleteYn.isNull().or(project.deleteYn.ne("Y")))
            .orderBy(project.projectStart.asc())
            .fetch();
    }
    
    public List<ItemDto> findItemsByProjectId(Long projectId) {
        // 현재 Item 엔티티에 projectId 필드가 없으므로 모든 아이템을 반환
        // 실제 구현 시에는 Item 엔티티에 projectId 외래키가 필요합니다
        return query
            .select(Projections.bean(ItemDto.class,
                item.id.as("itemId"),
                item.title.as("name"),
                item.cont
            ))
            .from(item)
            .where(item.deleteYn.isNull().or(item.deleteYn.ne("Y")))
            .orderBy(item.id.asc())
            .fetch();
    }
    
    public List<ImageDto> findImagesByItemId(Long itemId) {
        return query
            .select(Projections.bean(ImageDto.class,
                image.imagePath.as("img")
            ))
            .from(image)
            .where(image.contentId.eq(itemId)
                .and(image.contentGb.eq(ContentCode.ITEM))
                .and(image.deleteYn.isNull().or(image.deleteYn.ne("Y"))))
            .orderBy(image.id.asc())
            .fetch();
    }

}
