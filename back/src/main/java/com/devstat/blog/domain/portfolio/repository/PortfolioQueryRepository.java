package com.devstat.blog.domain.portfolio.repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
public class PortfolioQueryRepository {

    private final JPAQueryFactory query;

    private final QCompany company = QCompany.company;
    private final QProject project = QProject.project;
    private final QItem item = QItem.item;
    private final QImage image = QImage.image;

    /**
     * 포트폴리오 전체 데이터를 계층적으로 조회
     * Company -> Project -> Item -> Image 순으로 조회하여 중첩 구조로 반환
     */
    public List<PortfolioDto> findCompletePortfolioData() {
        // 1. Company 조회
        List<PortfolioDto> companies = query
                .select(Projections.bean(PortfolioDto.class,
                        company.id.as("companyId"),
                        company.companyName.as("name"),
                        company.companyLogoPath.as("logo"),
                        company.companyIn.stringValue().concat(" ~ ").concat(company.companyOut.stringValue())
                                .as("date")))
                .from(company)
                .where(company.deleteYn.isNull().or(company.deleteYn.ne("Y")))
                .orderBy(company.companyIn.asc())
                .fetch();

        if (companies.isEmpty()) {
            return companies;
        }

        // Company ID 목록 추출
        List<Long> companyIds = companies.stream()
                .map(PortfolioDto::getCompanyId)
                .collect(Collectors.toList());

        // 2. Project 조회 (Company와 연관)
        List<ProjectDto> projects = query
                .select(Projections.bean(ProjectDto.class,
                        project.id.as("projectId"),
                        project.company.id.as("companyId"),
                        project.projectName.as("name"),
                        project.projectStart.stringValue().concat(" ~ ").concat(project.projectEnd.stringValue())
                                .as("date")))
                .from(project)
                .where(project.company.id.in(companyIds)
                        .and(project.deleteYn.isNull().or(project.deleteYn.ne("Y"))))
                .orderBy(project.projectStart.asc())
                .fetch();

        // Project를 Company별로 그룹핑
        Map<Long, List<ProjectDto>> projectsByCompany = projects.stream()
                .collect(Collectors.groupingBy(p -> (Long) p.getCompanyId()));

        if (!projects.isEmpty()) {
            // Project ID 목록 추출
            List<Long> projectIds = projects.stream()
                    .map(ProjectDto::getProjectId)
                    .collect(Collectors.toList());

            // 3. Item 조회 (Project와 연관)
            List<ItemDto> items = query
                    .select(Projections.bean(ItemDto.class,
                            item.id.as("itemId"),
                            item.project.id.as("projectId"),
                            item.title.as("name"),
                            item.cont))
                    .from(item)
                    .where(item.project.id.in(projectIds)
                            .and(item.deleteYn.isNull().or(item.deleteYn.ne("Y"))))
                    .orderBy(item.id.asc())
                    .fetch();

            // Item을 Project별로 그룹핑
            Map<Long, List<ItemDto>> itemsByProject = items.stream()
                    .collect(Collectors.groupingBy(i -> (Long) i.getProjectId()));

            if (!items.isEmpty()) {
                // Item ID 목록 추출
                List<Long> itemIds = items.stream()
                        .map(ItemDto::getItemId)
                        .collect(Collectors.toList());

                // 4. Image 조회 (Item과 연관)
                List<ImageDto> images = query
                        .select(Projections.bean(ImageDto.class,
                                image.contentId.as("itemId"),
                                image.imagePath.as("img")))
                        .from(image)
                        .where(image.contentId.in(itemIds)
                                .and(image.contentGb.eq(ContentCode.ITEM))
                                .and(image.deleteYn.isNull().or(image.deleteYn.ne("Y"))))
                        .orderBy(image.id.asc())
                        .fetch();

                // Image를 Item별로 그룹핑
                Map<Long, List<ImageDto>> imagesByItem = images.stream()
                        .collect(Collectors.groupingBy(img -> (Long) img.getItemId()));

                // Item에 Image 매핑
                items.forEach(itemDto -> {
                    List<ImageDto> itemImages = imagesByItem.getOrDefault(itemDto.getItemId(), List.of());
                    itemDto.setImageList(itemImages);
                });
            }

            // Project에 Item 매핑
            projects.forEach(projectDto -> {
                List<ItemDto> projectItems = itemsByProject.getOrDefault(projectDto.getProjectId(), List.of());
                projectDto.setItemList(projectItems);
            });
        }

        // Company에 Project 매핑
        companies.forEach(companyDto -> {
            List<ProjectDto> companyProjects = projectsByCompany.getOrDefault(companyDto.getCompanyId(), List.of());
            companyDto.setProjectList(companyProjects);
        });

        return companies;
    }

    /**
     * 특정 Company의 포트폴리오 데이터 조회
     */
    public PortfolioDto findPortfolioDataByCompanyId(Long companyId) {
        // Company 조회
        PortfolioDto companyDto = query
                .select(Projections.bean(PortfolioDto.class,
                        company.id.as("companyId"),
                        company.companyName.as("name"),
                        company.companyLogoPath.as("logo"),
                        company.companyIn.stringValue().concat(" ~ ").concat(company.companyOut.stringValue())
                                .as("date")))
                .from(company)
                .where(company.id.eq(companyId)
                        .and(company.deleteYn.isNull().or(company.deleteYn.ne("Y"))))
                .fetchOne();

        if (companyDto == null) {
            return null;
        }

        // Project 조회
        List<ProjectDto> projects = query
                .select(Projections.bean(ProjectDto.class,
                        project.id.as("projectId"),
                        project.projectName.as("name"),
                        project.projectStart.stringValue().concat(" ~ ").concat(project.projectEnd.stringValue())
                                .as("date")))
                .from(project)
                .where(project.company.id.eq(companyId)
                        .and(project.deleteYn.isNull().or(project.deleteYn.ne("Y"))))
                .orderBy(project.projectStart.asc())
                .fetch();

        // 각 Project에 대해 Item과 Image 조회
        projects.forEach(projectDto -> {
            List<ItemDto> items = query
                    .select(Projections.bean(ItemDto.class,
                            item.id.as("itemId"),
                            item.title.as("name"),
                            item.cont))
                    .from(item)
                    .where(item.project.id.eq(projectDto.getProjectId())
                            .and(item.deleteYn.isNull().or(item.deleteYn.ne("Y"))))
                    .orderBy(item.id.asc())
                    .fetch();

            // 각 Item에 대해 Image 조회
            items.forEach(itemDto -> {
                List<ImageDto> images = query
                        .select(Projections.bean(ImageDto.class,
                                image.imagePath.as("img")))
                        .from(image)
                        .where(image.contentId.eq(itemDto.getItemId())
                                .and(image.contentGb.eq(ContentCode.ITEM))
                                .and(image.deleteYn.isNull().or(image.deleteYn.ne("Y"))))
                        .orderBy(image.id.asc())
                        .fetch();
                itemDto.setImageList(images);
            });

            projectDto.setItemList(items);
        });

        companyDto.setProjectList(projects);
        return companyDto;
    }
}
