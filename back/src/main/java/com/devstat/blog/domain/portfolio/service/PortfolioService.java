package com.devstat.blog.domain.portfolio.service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.devstat.blog.core.annotation.InjectAccountInfo;
import com.devstat.blog.core.aspect.AccountDto;
import com.devstat.blog.core.code.StatusCode;
import com.devstat.blog.core.exception.CmmnException;
import com.devstat.blog.domain.portfolio.code.ContentCode;
import com.devstat.blog.domain.portfolio.dto.CompanyRequestDto;
import com.devstat.blog.domain.portfolio.dto.ItemRequestDto;
import com.devstat.blog.domain.portfolio.dto.PortfolioDto;
import com.devstat.blog.domain.portfolio.dto.ProjectRequestDto;
import com.devstat.blog.domain.portfolio.entity.Company;
import com.devstat.blog.domain.portfolio.entity.Item;
import com.devstat.blog.domain.portfolio.entity.Image;
import com.devstat.blog.domain.portfolio.entity.Project;
import com.devstat.blog.domain.portfolio.repository.CompanyJpa;
import com.devstat.blog.domain.portfolio.repository.CompanyQuery;
import com.devstat.blog.domain.portfolio.repository.ItemJpa;
import com.devstat.blog.domain.portfolio.repository.ItemQuery;
import com.devstat.blog.domain.portfolio.repository.ImageJpa;
import com.devstat.blog.domain.portfolio.repository.ProjectJpa;
import com.devstat.blog.domain.portfolio.repository.ProjectQuery;
import com.devstat.blog.domain.portfolio.repository.PortfolioQueryRepository;
import com.devstat.blog.utility.ItemCheck;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PortfolioService {

    @Value("${blog.portfolio.path}")
    private String uploadPath;

    private final CompanyJpa companyJpa;
    private final CompanyQuery companyQuery;
    private final ItemJpa itemJpa;
    private final ItemQuery itemQuery;
    private final ImageJpa imageJpa;
    private final ProjectJpa projectJpa;
    private final ProjectQuery projectQuery;
    private final PortfolioQueryRepository portfolioQueryRepository;

    /**
     * 포트폴리오 전체 정보 조회 (계층적 구조)
     */
    public List<PortfolioDto> getProtfolioInfo(AccountDto accountDto) {
        return portfolioQueryRepository.findCompletePortfolioData();
    }

    /**
     * 특정 회사의 포트폴리오 정보 조회
     */
    public PortfolioDto getPortfolioInfoByCompanyId(Long companyId) {
        return portfolioQueryRepository.findPortfolioDataByCompanyId(companyId);
    }

    @Transactional(readOnly = false)
    public StatusCode insertCompany(CompanyRequestDto dto, MultipartFile logo) {

        if (ItemCheck.isEmpty(logo)) {
            return StatusCode.LOGO_EMPTY;
        }

        Path path = Paths.get(uploadPath, logo.getOriginalFilename());

        try {
            logo.transferTo(path.toFile());
        } catch (Exception e) {
            return StatusCode.LOGO_SAVE_FAIL;
        }

        Company company = Company.of(dto.getCompanyName(), path.toString(), dto.getCompanyIn(), dto.getCompanyOut());

        companyJpa.save(company);

        return StatusCode.SUCCESS;
    }

    @InjectAccountInfo
    @Transactional(readOnly = false)
    public StatusCode updateCompany(AccountDto accountDto, Long id, CompanyRequestDto dto, MultipartFile logo) {

        Company findCompany = companyJpa.findById(id, accountDto.getAccountId())
                .orElseThrow(() -> new CmmnException(StatusCode.COMPANY_NOT_FOUND));

        if (!ItemCheck.isEmpty(logo)) {
            findCompany.update(dto.getCompanyName(), dto.getCompanyIn(), dto.getCompanyOut());
        } else {
            File file = new File(findCompany.getCompanyLogoPath());

            if (file.exists()) {
                file.delete();
            }

            Path path = Paths.get(uploadPath, logo.getOriginalFilename());

            try {
                logo.transferTo(path.toFile());
            } catch (Exception e) {
                throw new CmmnException(StatusCode.LOGO_SAVE_FAIL);
            }

            findCompany.update(dto.getCompanyName(), path.toString(), dto.getCompanyIn(), dto.getCompanyOut());
        }

        return StatusCode.SUCCESS;

    }

    @InjectAccountInfo
    @Transactional(readOnly = false)
    public StatusCode deleteCompany(AccountDto accountDto, Long id) {

        Company findCompany = companyJpa.findById(id, accountDto.getAccountId())
                .orElseThrow(() -> new CmmnException(StatusCode.COMPANY_NOT_FOUND));

        findCompany.delete();

        return StatusCode.SUCCESS;

    }

    @InjectAccountInfo
    @Transactional(readOnly = false)
    public StatusCode insertItem(AccountDto accountDto, ItemRequestDto dto, List<MultipartFile> images) {
        Project findProject = projectJpa.findById(dto.getProjectId(), accountDto.getAccountId()).orElseThrow(() -> new CmmnException(StatusCode.PROJECT_NOT_FOUND));


        Item item = Item.of(findProject, dto.getTitle(), dto.getCont());
        itemJpa.save(item);

        if (images != null && !images.isEmpty()) {
            saveItemImages(item.getId(), images);
        }

        return StatusCode.SUCCESS;
    }

    @InjectAccountInfo
    @Transactional(readOnly = false)
    public StatusCode updateItem(AccountDto accountDto, Long id, ItemRequestDto dto, List<MultipartFile> images) {

        Item findItem = itemJpa.findById(id, accountDto.getAccountId())
                .orElseThrow(() -> new CmmnException(StatusCode.ITEM_NOT_FOUND));

        findItem.update(dto.getTitle(), dto.getCont());

        if (images != null && !images.isEmpty()) {
            List<Image> existingImages = imageJpa.findByContentIdAndContentGb(id, ContentCode.ITEM,
                    accountDto.getAccountId());
            for (Image image : existingImages) {
                File file = new File(image.getImagePath());
                if (file.exists()) {
                    file.delete();
                }
            }
            imageJpa.deleteAll(existingImages);
            saveItemImages(id, images);
        }

        return StatusCode.SUCCESS;
    }

    @InjectAccountInfo
    @Transactional(readOnly = false)
    public StatusCode deleteItem(AccountDto accountDto, Long id) {

        Item findItem = itemJpa.findById(id, accountDto.getAccountId())
                .orElseThrow(() -> new CmmnException(StatusCode.ITEM_NOT_FOUND));

        findItem.delete();

        List<Image> images = imageJpa.findByContentIdAndContentGb(id, ContentCode.ITEM,
                accountDto.getAccountId());
        for (Image image : images) {
            File file = new File(image.getImagePath());
            if (file.exists()) {
                file.delete();
            }
        }
        imageJpa.deleteAll(images);

        return StatusCode.SUCCESS;
    }

    @InjectAccountInfo
    @Transactional(readOnly = false)
    public StatusCode insertProject(AccountDto accountDto, ProjectRequestDto dto) {
        Company findCompanay = companyJpa.findById(dto.getCompanyId(), accountDto.getAccountId())
                .orElseThrow(() -> new CmmnException(StatusCode.COMPANY_NOT_FOUND));

        Project project = Project.of(findCompanay, dto.getProjectName(), dto.getProjectStart(), dto.getProjectEnd());
        projectJpa.save(project);

        return StatusCode.SUCCESS;
    }

    @InjectAccountInfo
    @Transactional(readOnly = false)
    public StatusCode updateProject(AccountDto accountDto, Long id, ProjectRequestDto dto) {

        Project findProject = projectJpa.findById(id, accountDto.getAccountId())
                .orElseThrow(() -> new CmmnException(StatusCode.PROJECT_NOT_FOUND));

        findProject.update(dto.getProjectName(), dto.getProjectStart(), dto.getProjectEnd());

        return StatusCode.SUCCESS;
    }

    @InjectAccountInfo
    @Transactional(readOnly = false)
    public StatusCode deleteProject(AccountDto accountDto, Long id) {

        Project findProject = projectJpa.findById(id, accountDto.getAccountId())
                .orElseThrow(() -> new CmmnException(StatusCode.PROJECT_NOT_FOUND));

        findProject.delete();

        return StatusCode.SUCCESS;
    }

    private void saveItemImages(Long itemId, List<MultipartFile> images) {
        for (MultipartFile imageFile : images) {
            if (!ItemCheck.isEmpty(imageFile)) {
                Path path = Paths.get(uploadPath, imageFile.getOriginalFilename());
                try {
                    imageFile.transferTo(path.toFile());
                    Image image = Image.of(itemId, ContentCode.ITEM, path.toString());
                    imageJpa.save(image);
                } catch (Exception e) {
                    throw new CmmnException(StatusCode.IMAGE_SAVE_FAIL);
                }
            }
        }
    }

}
