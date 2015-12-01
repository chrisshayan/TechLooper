package com.techlooper.service.impl;

import com.techlooper.dto.DashBoardInfo;
import com.techlooper.dto.EmailSettingDto;
import com.techlooper.entity.EmailSettingEntity;
import com.techlooper.entity.vnw.VnwCompany;
import com.techlooper.entity.vnw.VnwUser;
import com.techlooper.model.EmployerDto;
import com.techlooper.repository.elasticsearch.EmailSettingRepository;
import com.techlooper.repository.vnw.VnwCompanyRepo;
import com.techlooper.repository.vnw.VnwUserRepo;
import com.techlooper.service.ChallengeService;
import com.techlooper.service.CompanyService;
import com.techlooper.service.EmployerService;
import com.techlooper.service.ProjectService;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by phuonghqh on 8/11/15.
 */
@Service
public class EmployerServiceImpl implements EmployerService {

    @Resource
    private ChallengeService challengeService;

    @Resource
    private ProjectService projectService;

    @Resource
    private VnwUserRepo vnwUserRepo;

    @Resource
    private VnwCompanyRepo vnwCompanyRepo;

    @Resource
    private EmailSettingRepository emailSettingRepository;

    @Resource
    private CompanyService companyService;

    @Resource
    private Mapper dozerMapper;

    @Value("${mail.techlooper.reply_to}")
    private String mailTechlooperReplyTo;

    public DashBoardInfo getDashboardInfo(String owner) {
        VnwUser user = vnwUserRepo.findByUsernameIgnoreCase(owner);
        String email = user.getEmail();
        return DashBoardInfo.DashBoardInfoBuilder.dashBoardInfo()
                .withProjects(projectService.findByOwner(email))
                .withChallenges(challengeService.findChallengeByOwner(email))
//      .withChallenges(challengeService.findInProgressChallenges(email))
                .build();
    }

    @Override
    public VnwCompany findCompanyById(Long companyId) {
        return vnwCompanyRepo.findOne(companyId);
    }

    @Override
    public VnwUser findEmployerByUsername(String employerUsername) {
        return vnwUserRepo.findByUsernameIgnoreCase(employerUsername);
    }

    @Override
    public EmailSettingDto saveEmployerEmailSetting(EmailSettingDto emailSettingDto) {
        EmailSettingEntity emailSettingEntity = dozerMapper.map(emailSettingDto, EmailSettingEntity.class);
        emailSettingRepository.save(emailSettingEntity);
        return emailSettingDto;
    }

    @Override
    public EmailSettingDto findEmployerEmailSetting(String employerEmail) {
        EmailSettingEntity emailSettingEntity = emailSettingRepository.findOne(employerEmail);
        EmailSettingDto emailSettingDto = new EmailSettingDto();
        if (emailSettingEntity != null) {
            emailSettingDto = dozerMapper.map(emailSettingEntity, EmailSettingDto.class);
        } else {
            emailSettingDto.setEmployerEmail(employerEmail);
            emailSettingDto.setReplyEmail(mailTechlooperReplyTo);
            EmployerDto employerDto = companyService.findByUserName(employerEmail);
            if (employerDto != null) {
                StringBuilder emailSignatureBuilder = new StringBuilder("");
                if (StringUtils.isNotEmpty(employerDto.getCompanyName())) {
                    emailSignatureBuilder.append(employerDto.getCompanyName());
                }
                if (StringUtils.isNotEmpty(employerDto.getAddress())) {
                    emailSignatureBuilder.append("<br/>").append(employerDto.getAddress());
                }
                emailSettingDto.setEmailSignature(emailSignatureBuilder.toString());
            }
        }
        return emailSettingDto;
    }
}
