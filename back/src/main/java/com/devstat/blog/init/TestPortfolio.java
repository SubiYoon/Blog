package com.devstat.blog.init;

import com.devstat.blog.domain.portfolio.code.ContentCode;
import com.devstat.blog.domain.portfolio.entity.Company;
import com.devstat.blog.domain.portfolio.entity.Image;
import com.devstat.blog.domain.portfolio.entity.Item;
import com.devstat.blog.domain.portfolio.entity.Project;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
//@Profile("dev")
@Qualifier("portfolio")
@RequiredArgsConstructor
public class TestPortfolio {

    private final EntityManager em;

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void initPortfolioData() {
        // 첫 번째 회사 - 테크 스타트업
        Company company1 = Company.of(
                "TechCorp",
                LocalDate.of(2020, 3, 1),
                LocalDate.of(2022, 12, 31)
        );
        em.persist(company1);

        // 두 번째 회사 - 대기업
        Company company2 = Company.of(
                "Samsung Electronics",
                LocalDate.of(2023, 1, 15),
                null
        );
        em.persist(company2);

        em.flush();

        // 회사 로고 이미지 추가
        Image companyLogo1 = Image.of(company1.getId(), ContentCode.COMPANY, 
                "https://logo.clearbit.com/techcorp.com");
        em.persist(companyLogo1);

        Image companyLogo2 = Image.of(company2.getId(), ContentCode.COMPANY, 
                "https://logo.clearbit.com/samsung.com");
        em.persist(companyLogo2);

        em.flush();

        // 첫 번째 회사의 프로젝트들
        Project project1 = Project.of(company1,
                "E-Commerce 플랫폼 개발",
                LocalDate.of(2020, 3, 15),
                LocalDate.of(2021, 8, 30)
        );
        em.persist(project1);

        Project project2 = Project.of(company1,
                "모바일 앱 리뉴얼",
                LocalDate.of(2021, 9, 1),
                LocalDate.of(2022, 11, 15)
        );
        em.persist(project2);

        // 두 번째 회사의 프로젝트
        Project project3 = Project.of(company2,
                "AI 기반 추천 시스템",
                LocalDate.of(2023, 2, 1),
                LocalDate.of(2024, 6, 30)
        );
        em.persist(project3);

        em.flush();

        // 첫 번째 프로젝트의 아이템들
        Item item1 = Item.of(project1,
                "백엔드 API 서버 개발",
                "Spring Boot와 MySQL을 사용하여 RESTful API 서버를 개발했습니다. " +
                "JWT 기반 인증 시스템과 Redis를 활용한 캐싱 시스템을 구현했습니다."
        );
        em.persist(item1);

        Item item2 = Item.of(project1,
                "프론트엔드 개발",
                "React와 TypeScript를 사용하여 반응형 웹 애플리케이션을 개발했습니다. " +
                "Styled-components와 Material-UI를 활용한 UI/UX 디자인을 구현했습니다."
        );
        em.persist(item2);

        // 두 번째 프로젝트의 아이템들
        Item item3 = Item.of(project2,
                "앱 성능 최적화",
                "React Native 기반 모바일 앱의 성능을 30% 향상시켰습니다. " +
                "이미지 최적화, 코드 스플리팅, 메모리 누수 개선 등을 수행했습니다."
        );
        em.persist(item3);

        Item item4 = Item.of(project2,
                "사용자 경험 개선",
                "사용자 피드백을 바탕으로 UI/UX를 개선하여 사용자 만족도를 25% 증가시켰습니다. " +
                "A/B 테스트를 통한 최적화와 접근성 향상 작업을 진행했습니다."
        );
        em.persist(item4);

        // 세 번째 프로젝트의 아이템들
        Item item5 = Item.of(project3,
                "머신러닝 모델 개발",
                "Python과 TensorFlow를 사용하여 개인화 추천 알고리즘을 개발했습니다. " +
                "협업 필터링과 컨텐츠 기반 필터링을 결합한 하이브리드 추천 시스템을 구축했습니다."
        );
        em.persist(item5);

        Item item6 = Item.of(project3,
                "실시간 데이터 파이프라인",
                "Apache Kafka와 Apache Spark를 활용하여 실시간 데이터 처리 파이프라인을 구축했습니다. " +
                "대용량 데이터의 실시간 분석과 추천 결과 생성 시스템을 개발했습니다."
        );
        em.persist(item6);

        em.flush();

        // 이미지 데이터 추가
        Image image1 = Image.of(item1.getId(), ContentCode.ITEM, 
                "https://images.unsplash.com/photo-1555066931-4365d14bab8c?w=800&h=600&fit=crop");
        em.persist(image1);

        Image image2 = Image.of(item2.getId(), ContentCode.ITEM, 
                "https://images.unsplash.com/photo-1547658719-da2b51169166?w=800&h=600&fit=crop");
        em.persist(image2);

        Image image3 = Image.of(item3.getId(), ContentCode.ITEM, 
                "https://images.unsplash.com/photo-1512941937669-90a1b58e7e9c?w=800&h=600&fit=crop");
        em.persist(image3);

        Image image4 = Image.of(item4.getId(), ContentCode.ITEM, 
                "https://images.unsplash.com/photo-1581291518633-83b4ebd1d83e?w=800&h=600&fit=crop");
        em.persist(image4);

        Image image5 = Image.of(item5.getId(), ContentCode.ITEM, 
                "https://images.unsplash.com/photo-1555949963-aa79dcee981c?w=800&h=600&fit=crop");
        em.persist(image5);

        Image image6 = Image.of(item6.getId(), ContentCode.ITEM, 
                "https://images.unsplash.com/photo-1558494949-ef010cbdcc31?w=800&h=600&fit=crop");
        em.persist(image6);

        em.flush();

        System.out.println("✅ Portfolio 샘플 데이터 생성 완료");
        System.out.println("🏢 생성된 회사: " + company1.getCompanyName() + ", " + company2.getCompanyName());
        System.out.println("📂 생성된 프로젝트: " + project1.getProjectName() + ", " + 
                          project2.getProjectName() + ", " + project3.getProjectName());
        System.out.println("📋 생성된 아이템: 총 6개");
        System.out.println("🖼️ 생성된 이미지: 총 6개");
    }
}