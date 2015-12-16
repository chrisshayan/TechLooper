package com.techlooper.service.impl;

import com.techlooper.entity.EmployerEntity;
import com.techlooper.entity.ProjectEntity;
import com.techlooper.entity.ProjectRegistrantEntity;
import com.techlooper.model.*;
import com.techlooper.repository.elasticsearch.CompanySearchResultRepository;
import com.techlooper.repository.elasticsearch.ProjectRegistrantRepository;
import com.techlooper.repository.elasticsearch.ProjectRepository;
import com.techlooper.service.CompanyService;
import com.techlooper.service.EmailService;
import com.techlooper.service.ProjectService;
import com.techlooper.util.DataUtils;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.*;

import static com.techlooper.util.DateTimeUtils.*;
import static org.elasticsearch.index.query.FilterBuilders.queryFilter;
import static org.elasticsearch.index.query.QueryBuilders.*;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Resource
    private ProjectRepository projectRepository;

    @Resource
    private CompanySearchResultRepository companySearchResultRepository;

    @Resource
    private ProjectRegistrantRepository projectRegistrantRepository;

    @Resource
    private Mapper dozerMapper;

    @Resource
    private MimeMessage applyJobMailMessage;

    @Value("${web.baseUrl}")
    private String webBaseUrl;

    @Value("${mail.techlooper.mailingList}")
    private String techlooperMailingList;

    @Resource
    private CompanyService companyService;

    @Resource
    private EmailService emailService;

    @Override
    public ProjectEntity saveProject(ProjectDto projectDto) {
        ProjectEntity projectEntity = dozerMapper.map(projectDto, ProjectEntity.class);
        projectEntity.setProjectId(currentDateTime());
        projectEntity.setCreatedDate(currentDate());
        projectEntity = projectRepository.save(projectEntity);
        sendEmailAlertEmployerPostJob(projectEntity);
        sendEmailAlertTechloopiesPostJob(projectEntity);
        return projectEntity;
    }

    @Override
    public void sendEmailAlertEmployerPostJob(ProjectEntity projectEntity) {
        List<String> subjectVariableValues = Arrays.asList(projectEntity.getProjectTitle());
        String recipientAddress = projectEntity.getAuthorEmail();

        EmailRequestModel emailRequestModel = new EmailRequestModel.Builder()
                .withTemplateName(EmailTemplateNameEnum.PROJECT_ALERT_EMPLOYER_POST_JOB.name())
                .withLanguage(projectEntity.getLang())
                .withTemplateModel(buildAlertEmployerApplyJobEmailTemplateModel(projectEntity, null))
                .withMailMessage(applyJobMailMessage)
                .withRecipientAddresses(recipientAddress)
                .withSubjectVariableValues(subjectVariableValues).build();
        emailService.sendMail(emailRequestModel);
    }

    @Override
    public void sendEmailAlertTechloopiesPostJob(ProjectEntity projectEntity) {
        EmailRequestModel emailRequestModel = new EmailRequestModel.Builder()
                .withTemplateName(EmailTemplateNameEnum.PROJECT_ALERT_TECHLOOPIES_APPLY_JOB.name())
                .withLanguage(Language.en)
                .withTemplateModel(buildAlertEmployerApplyJobEmailTemplateModel(projectEntity, null))
                .withMailMessage(applyJobMailMessage)
                .withRecipientAddresses(techlooperMailingList).build();
        emailService.sendMail(emailRequestModel);
    }

    @Override
    public Long countTotalNumberOfProjects() {
        return projectRepository.count();
    }

    @Override
    public Long countTotalNumberOfSkills() {
        Long total = 0L;
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("project");
        searchQueryBuilder.withQuery(matchAllQuery());
        List<ProjectEntity> projects = DataUtils.getAllEntities(projectRepository, searchQueryBuilder);
        for (ProjectEntity project : projects) {
            total += project.getSkills().size();
        }

        return total;
    }

    @Override
    public List<ProjectDto> listProject() {
        List<ProjectDto> projects = new ArrayList<>();
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("project");
        searchQueryBuilder.withQuery(matchAllQuery());
        searchQueryBuilder.withSort(SortBuilders.fieldSort("projectId").order(SortOrder.DESC));
        List<ProjectEntity> projectEntities = DataUtils.getAllEntities(projectRepository, searchQueryBuilder);
        for (ProjectEntity projectEntity : projectEntities) {
            ProjectDto projectDto = dozerMapper.map(projectEntity, ProjectDto.class);
            EmployerDto employerDto = companyService.findByUserName(projectDto.getAuthorEmail());

            if (employerDto != null) {
                projectDto.setCompanyName(employerDto.getCompanyName());
                projectDto.setCompanyLogoURL(employerDto.getCompanyLogoURL());
            }
            projects.add(projectDto);
        }
        return projects;
    }

    @Override
    public ProjectDetailDto getProjectDetail(Long projectId) {
        ProjectDetailDto projectDetail = new ProjectDetailDto();
        ProjectEntity projectEntity = projectRepository.findOne(projectId);
        ProjectDto project = dozerMapper.map(projectEntity, ProjectDto.class);
        project.setNumberOfApplications(getNumberOfRegistrants(projectId));
        projectDetail.setProject(project);

        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withQuery(nestedQuery("employers",
                matchPhraseQuery("employers.userName", project.getAuthorEmail())));
        List<EmployerEntity> employerEntities = DataUtils.getAllEntities(companySearchResultRepository, searchQueryBuilder);

        if (!employerEntities.isEmpty()) {
            EmployerEntity employerEntity = employerEntities.get(0);
            EmployerDto employerDto = dozerMapper.map(employerEntity, EmployerDto.class);
            if (!employerEntity.getEmployers().isEmpty()) {
                Employer employer = employerEntity.getEmployers().get(0);
                employerDto.setCreatedDate(date2String(employer.getCreatedDate(), BASIC_DATE_PATTERN));
            }
            projectDetail.setCompany(employerDto);
        }
        return projectDetail;
    }

    @Override
    public long joinProject(ProjectRegistrantDto projectRegistrantDto) {
        Long projectId = projectRegistrantDto.getProjectId();
        boolean isExist = checkIfProjectRegistrantExist(projectId, projectRegistrantDto.getRegistrantEmail());

        if (!isExist) {
            ProjectRegistrantEntity projectRegistrantEntity = dozerMapper.map(projectRegistrantDto, ProjectRegistrantEntity.class);
            ProjectEntity projectEntity = projectRepository.findOne(projectId);
            sendEmailAlertJobSeekerApplyJob(projectEntity, projectRegistrantEntity);
            sendEmailAlertEmployerApplyJob(projectEntity, projectRegistrantEntity);
            projectRegistrantEntity.setMailSent(Boolean.TRUE);
            projectRegistrantEntity.setProjectRegistrantId(new Date().getTime());
            projectRegistrantRepository.save(projectRegistrantEntity);
        }
        return getNumberOfRegistrants(projectId);
    }

    @Override
    public void sendEmailAlertJobSeekerApplyJob(ProjectEntity projectEntity, ProjectRegistrantEntity projectRegistrantEntity) {
        List<String> subjectVariableValues = Arrays.asList(projectEntity.getProjectTitle());
        String recipientAddress = projectRegistrantEntity.getRegistrantEmail();

        EmailRequestModel emailRequestModel = new EmailRequestModel.Builder()
                .withTemplateName(EmailTemplateNameEnum.PROJECT_ALERT_EMPLOYER_APPLY_JOB.name())
                .withLanguage(projectRegistrantEntity.getLang())
                .withTemplateModel(buildAlertEmployerApplyJobEmailTemplateModel(projectEntity, projectRegistrantEntity))
                .withMailMessage(applyJobMailMessage)
                .withRecipientAddresses(recipientAddress)
                .withSubjectVariableValues(subjectVariableValues).build();
        emailService.sendMail(emailRequestModel);
    }

    @Override
    public void sendEmailAlertEmployerApplyJob(ProjectEntity projectEntity, ProjectRegistrantEntity projectRegistrantEntity) {
        List<String> subjectVariableValues = Arrays.asList(projectEntity.getProjectTitle());
        String recipientAddress = projectEntity.getAuthorEmail();

        EmailRequestModel emailRequestModel = new EmailRequestModel.Builder()
                .withTemplateName(EmailTemplateNameEnum.PROJECT_ALERT_EMPLOYER_APPLY_JOB.name())
                .withLanguage(projectRegistrantEntity.getLang())
                .withTemplateModel(buildAlertEmployerApplyJobEmailTemplateModel(projectEntity, projectRegistrantEntity))
                .withMailMessage(applyJobMailMessage)
                .withRecipientAddresses(recipientAddress)
                .withSubjectVariableValues(subjectVariableValues).build();
        emailService.sendMail(emailRequestModel);
    }

    private Map<String, Object> buildAlertEmployerApplyJobEmailTemplateModel(
            ProjectEntity projectEntity, ProjectRegistrantEntity projectRegistrantEntity) {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("webBaseUrl", webBaseUrl);
        templateModel.put("projectTitle", projectEntity.getProjectTitle());
        templateModel.put("projectDescription", projectEntity.getProjectDescription());
        templateModel.put("skills", StringUtils.join(projectEntity.getSkills(), "<br/>"));
        templateModel.put("payMethod", projectEntity.getPayMethod());
        templateModel.put("estimatedEndDate", projectEntity.getEstimatedEndDate());
        templateModel.put("budget", projectEntity.getBudget());
        templateModel.put("estimatedDuration", projectEntity.getEstimatedDuration());
        templateModel.put("estimatedWorkload", projectEntity.getEstimatedWorkload());
        templateModel.put("hourlyRate", projectEntity.getHourlyRate());
        templateModel.put("numberOfHires", projectEntity.getNumberOfHires());
        templateModel.put("projectId", projectEntity.getProjectId().toString());
        templateModel.put("projectAlias", projectEntity.getProjectTitle().replaceAll("\\W", "-"));
        templateModel.put("authorEmail", projectEntity.getAuthorEmail());

        if (projectRegistrantEntity != null) {
            templateModel.put("registrantFirstName", projectRegistrantEntity.getRegistrantFirstName());
            templateModel.put("registrantLastName", projectRegistrantEntity.getRegistrantLastName());
            templateModel.put("registrantEmail", projectRegistrantEntity.getRegistrantEmail());
            templateModel.put("resumeLink", projectRegistrantEntity.getResumeLink());
        }

        return templateModel;
    }

    @Override
    public Long getNumberOfRegistrants(Long projectId) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withSearchType(SearchType.COUNT);
        searchQueryBuilder.withFilter(FilterBuilders.termFilter("projectId", projectId));
        return projectRegistrantRepository.search(searchQueryBuilder.build()).getTotalElements();
    }

    public boolean checkIfProjectRegistrantExist(Long projectId, String email) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withQuery(boolQuery()
                .must(matchPhraseQuery("registrantEmail", email))
                .must(termQuery("projectId", projectId))
                .must(termQuery("mailSent", true)));

        long total = projectRegistrantRepository.search(searchQueryBuilder.build()).getTotalElements();
        return (total > 0);
    }

    @Override
    public List<ProjectDto> findProjectByOwner(String ownerEmail) {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder().withTypes("project");
        QueryStringQueryBuilder query = queryStringQuery(ownerEmail).defaultField("authorEmail");
        queryBuilder.withFilter(queryFilter(query));
        List<ProjectEntity> projectEntities = DataUtils.getAllEntities(projectRepository, queryBuilder);
        List<ProjectDto> projects = new ArrayList<>();
        for (ProjectEntity projectEntity : projectEntities) {
            ProjectDto projectDto = dozerMapper.map(projectEntity, ProjectDto.class);
            projectDto.setNumberOfApplications(countRegistrantsByProjectId(projectEntity.getProjectId()));
            projects.add(projectDto);
        }
        return projects;
    }

    @Override
    public Long countRegistrantsByProjectId(Long projectId) {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder().withTypes("projectRegistrant");
        queryBuilder.withFilter(queryFilter(termQuery("projectId", projectId))).withSearchType(SearchType.COUNT);
        return projectRegistrantRepository.search(queryBuilder.build()).getTotalElements();
    }
}
