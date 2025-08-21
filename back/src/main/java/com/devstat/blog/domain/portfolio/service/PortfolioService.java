package com.devstat.blog.domain.portfolio.service;

import com.devstat.blog.core.annotation.InjectAccountInfo;
import com.devstat.blog.core.aspect.AccountDto;
import com.devstat.blog.core.code.StatusCode;
import com.devstat.blog.core.exception.CmmnException;
import com.devstat.blog.domain.portfolio.code.ContentCode;
import com.devstat.blog.domain.portfolio.dto.*;
import com.devstat.blog.domain.portfolio.entity.Company;
import com.devstat.blog.domain.portfolio.entity.Image;
import com.devstat.blog.domain.portfolio.entity.Item;
import com.devstat.blog.domain.portfolio.entity.Project;
import com.devstat.blog.domain.portfolio.repository.*;
import com.devstat.blog.utility.ItemCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PortfolioService {

    @Value("${blog.portfolio.path}")
    private String blogPortfolioPath;

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
    public List<PortfolioDto> getPortfolioInfo(String alias) {
        return portfolioQueryRepository.findCompletePortfolioData(alias);
    }

    @InjectAccountInfo
    public List<PortfolioDto> getPortfolioInfo(AccountDto accountDto) {
        return portfolioQueryRepository.findCompletePortfolioData(accountDto);
    }

    @InjectAccountInfo
    @Transactional(readOnly = false)
    public StatusCode insertCompany(AccountDto accountDto, CompanyRequestDto dto) {
        String[] split = dto.getDate().split(" ~ ");

        LocalDate companyIn = LocalDate.parse(split[0]);
        LocalDate companyOut = null;

        if (split.length != 1) {
            companyOut = LocalDate.parse(split[1]);
        }

        Company company = Company.of(dto.getCompanyName(), companyIn, companyOut);
        companyJpa.save(company);

        return StatusCode.SUCCESS;
    }

    @InjectAccountInfo
    @Transactional(readOnly = false)
    public StatusCode updateCompany(AccountDto accountDto, Long id, CompanyRequestDto dto) {

        Company findCompany = companyJpa.findById(id, accountDto.getAccountId())
                .orElseThrow(() -> new CmmnException(StatusCode.COMPANY_NOT_FOUND));

        String[] split = dto.getDate().split(" ~ ");

        LocalDate companyIn = LocalDate.parse(split[0]);
        LocalDate companyOut = null;

        if (split.length != 1) {
            companyOut = LocalDate.parse(split[1]);
        }

        findCompany.update(dto.getCompanyName(), companyIn, companyOut);

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
    public StatusCode insertItem(AccountDto accountDto, ItemRequestDto dto) {
        Project findProject = projectJpa.findById(dto.getProjectId(), accountDto.getAccountId()).orElseThrow(() -> new CmmnException(StatusCode.PROJECT_NOT_FOUND));


        Item item = Item.of(findProject, dto.getTitle(), dto.getCont());
        itemJpa.save(item);

        return StatusCode.SUCCESS;
    }

    @InjectAccountInfo
    @Transactional(readOnly = false)
    public StatusCode updateItem(AccountDto accountDto, Long id, ItemRequestDto dto) {

        Item findItem = itemJpa.findById(id, accountDto.getAccountId())
                .orElseThrow(() -> new CmmnException(StatusCode.ITEM_NOT_FOUND));

        findItem.update(dto.getTitle(), dto.getCont());

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
        Company findCompany = companyJpa.findById(dto.getCompanyId(), accountDto.getAccountId())
                .orElseThrow(() -> new CmmnException(StatusCode.COMPANY_NOT_FOUND));

        String[] split = dto.getDate().split(" ~ ");

        LocalDate projectStart = LocalDate.parse(split[0]);
        LocalDate projectEnd = null;

        if (split.length != 1) {
            projectEnd = LocalDate.parse(split[1]);
        }

        Project project = Project.of(findCompany, dto.getProjectName(), projectStart, projectEnd);
        projectJpa.save(project);

        return StatusCode.SUCCESS;
    }

    @InjectAccountInfo
    @Transactional(readOnly = false)
    public StatusCode updateProject(AccountDto accountDto, Long id, ProjectRequestDto dto) {

        Project findProject = projectJpa.findById(id, accountDto.getAccountId())
                .orElseThrow(() -> new CmmnException(StatusCode.PROJECT_NOT_FOUND));

        String[] split = dto.getDate().split(" ~ ");

        LocalDate projectStart = LocalDate.parse(split[0]);
        LocalDate projectEnd = null;

        if (split.length != 1) {
            projectEnd = LocalDate.parse(split[1]);
        }

        findProject.update(dto.getProjectName(), projectStart, projectEnd);

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

    @Transactional
    @InjectAccountInfo
    public ImageDto saveItemImages(AccountDto accountDto, Long contentId, String contentGb, MultipartFile image) {
        Path path = Paths.get(blogPortfolioPath, accountDto.getAccountId(), contentId + "_" + contentGb, image.getOriginalFilename());

        try {
            if (!path.getParent().toFile().exists()) {
                path.getParent().toFile().mkdirs();
            }

            image.transferTo(path.toFile());
        } catch (IOException e) {
            throw new CmmnException(StatusCode.IMAGE_SAVE_FAIL);
        }

        Image saveImage = Image.of(contentId, ContentCode.valueOf(contentGb), path.toString());
        imageJpa.save(saveImage);

        ImageDto imageDto = new ImageDto();
        imageDto.setImageId(saveImage.getId());
        imageDto.setItemId(saveImage.getContentId());
        imageDto.setImg(saveImage.getImagePath());

        return imageDto;
    }

    @Transactional
    @InjectAccountInfo
    public StatusCode deleteImage(AccountDto accountDto, Long id) {
        Image findImage = imageJpa.findById(id, accountDto.getAccountId()).orElseThrow(() -> new CmmnException(StatusCode.IMAGE_NOT_FOUND));

        imageJpa.delete(findImage);

        return StatusCode.SUCCESS;
    }
}
