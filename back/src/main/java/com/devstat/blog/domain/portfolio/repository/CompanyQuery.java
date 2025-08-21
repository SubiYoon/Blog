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
        // 1. Company 기본 정보 조회
        List<PortfolioDto> companies = query
            .select(Projections.bean(PortfolioDto.class,
                company.id.as("companyId"),
                company.companyName.as("name"),
                company.companyIn.stringValue().concat(" ~ ").concat(company.companyOut.stringValue()).as("date")
            ))
            .from(company)
            .where(company.deleteYn.isNull().or(company.deleteYn.ne("Y")))
            .orderBy(company.companyIn.asc())
            .fetch();

        if (companies.isEmpty()) {
            return companies;
        }

        // 2. Company ID 목록 추출
        List<Long> companyIds = companies.stream()
                .map(PortfolioDto::getCompanyId)
                .collect(java.util.stream.Collectors.toList());

        // 3. Company 로고 이미지 조회
        List<ImageDto> companyLogos = query
                .select(Projections.bean(ImageDto.class,
                        image.id.as("imageId"),
                        image.contentId.as("itemId"),
                        image.imagePath.as("img")))
                .from(image)
                .where(image.contentId.in(companyIds)
                        .and(image.contentGb.eq(ContentCode.COMPANY))
                        .and(image.deleteYn.isNull().or(image.deleteYn.ne("Y"))))
                .fetch();

        // 4. Company별로 로고 매핑 (ID와 경로)
        java.util.Map<Long, ImageDto> logosByCompany = companyLogos.stream()
                .collect(java.util.stream.Collectors.toMap(
                        img -> (Long) img.getItemId(),
                        img -> img,
                        (existing, replacement) -> existing
                ));

        // 5. Company에 로고 정보 설정
        companies.forEach(companyDto -> {
            ImageDto logoImage = logosByCompany.get(companyDto.getCompanyId());
            if (logoImage != null) {
                companyDto.setLogo(logoImage.getImg());
                companyDto.setLogoId(logoImage.getImageId());
            }
        });

        return companies;
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
