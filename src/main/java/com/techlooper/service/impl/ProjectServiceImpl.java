package com.techlooper.service.impl;

import com.techlooper.entity.EmployerEntity;
import com.techlooper.entity.ProjectEntity;
import com.techlooper.entity.ProjectRegistrantEntity;
import com.techlooper.model.*;
import com.techlooper.repository.elasticsearch.CompanySearchResultRepository;
import com.techlooper.repository.elasticsearch.ProjectRegistrantRepository;
import com.techlooper.repository.elasticsearch.ProjectRepository;
import com.techlooper.service.CompanyService;
import com.techlooper.service.ProjectService;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.FilterBuilders.queryFilter;
import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Created by NguyenDangKhoa on 7/10/15.
 */
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

    @Resource
    private JavaMailSender mailSender;

    @Value("${web.baseUrl}")
    private String webBaseUrl;

    @Resource
    private Template alertJobSeekerApplyJobMailTemplateEn;

    @Resource
    private Template alertJobSeekerApplyJobMailTemplateVi;

    @Resource
    private Template alertEmployerApplyJobMailTemplateEn;

    @Resource
    private Template alertEmployerApplyJobMailTemplateVi;

    @Resource
    private Template alertEmployerPostJobMailTemplateVi;

    @Resource
    private Template alertEmployerPostJobMailTemplateEn;

    @Resource
    private Template alertTechloopiesPostJobMailTemplateEn;

    @Value("${mail.alertJobSeekerApplyJob.subject.en}")
    private String alertJobSeekerApplyJobMailSubjectEn;

    @Value("${mail.alertJobSeekerApplyJob.subject.vn}")
    private String alertJobSeekerApplyJobMailSubjectVn;

    @Value("${mail.alertEmployerApplyJob.subject.en}")
    private String alertEmployerApplyJobMailSubjectEn;

    @Value("${mail.alertEmployerApplyJob.subject.vn}")
    private String alertEmployerApplyJobMailSubjectVn;

    @Value("${mail.alertEmployerPostJob.subject.vn}")
    private String alertEmployerPostJobMailSubjectVn;

    @Value("${mail.alertEmployerPostJob.subject.en}")
    private String alertEmployerPostJobMailSubjectEn;

    @Value("${mail.alertTechloopiesPostJob.subject.en}")
    private String alertTechloopiesPostJobMailSubjectEn;

    @Value("${mail.postChallenge.techloopies.mailList}")
    private String techloopiesMailList;

    @Value("${mail.techlooper.reply_to}")
    private String mailTechlooperReplyTo;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Value("${elasticsearch.userimport.index.name}")
    private String techlooperIndex;

    @Resource
    private CompanyService companyService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectServiceImpl.class);

    @Override
    public ProjectEntity saveProject(ProjectDto projectDto) {
        ProjectEntity projectEntity = dozerMapper.map(projectDto, ProjectEntity.class);
        Date currentDate = new Date();
        projectEntity.setProjectId(currentDate.getTime());
        projectEntity.setCreatedDate(simpleDateFormat.format(currentDate));
        projectEntity = projectRepository.save(projectEntity);

        if (projectEntity != null) {
            try {
                sendEmailAlertEmployerPostJob(projectEntity);
                sendEmailAlertTechloopiesPostJob(projectEntity);
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        }

        return projectEntity;
    }

    @Override
    public void sendEmailAlertEmployerPostJob(ProjectEntity projectEntity)
            throws MessagingException, IOException, TemplateException {
        Template template = projectEntity.getLang() == Language.vi ?
                alertEmployerPostJobMailTemplateVi : alertEmployerPostJobMailTemplateEn;
        String mailSubject = projectEntity.getLang() == Language.vi ?
                alertEmployerPostJobMailSubjectVn : alertEmployerPostJobMailSubjectEn;
        mailSubject = String.format(mailSubject, projectEntity.getProjectTitle());
        Address[] emailAddress = InternetAddress.parse(projectEntity.getAuthorEmail());
        sendEmailAlertApplyJob(projectEntity, null, mailSubject, emailAddress, template, false);
    }

    @Override
    public void sendEmailAlertTechloopiesPostJob(ProjectEntity projectEntity)
            throws MessagingException, IOException, TemplateException {
        Address[] emailAddress = InternetAddress.parse(techloopiesMailList);
        sendEmailAlertApplyJob(projectEntity, null, alertTechloopiesPostJobMailSubjectEn,
                emailAddress, alertTechloopiesPostJobMailTemplateEn, false);
    }

    @Override
    public Long countTotalNumberOfProjects() {
        return projectRepository.count();
    }

    @Override
    public Long countTotalNumberOfSkills() {
        Long total = 0L;
        Iterator<ProjectEntity> projectIterator = projectRepository.findAll().iterator();
        while (projectIterator.hasNext()) {
            ProjectEntity projectEntity = projectIterator.next();
            if (!projectEntity.getSkills().isEmpty()) {
                total += projectEntity.getSkills().size();
            }
        }
        return total;
    }

    @Override
    public List<ProjectDto> listProject() {
        List<ProjectDto> projects = new ArrayList<>();
        Iterator<ProjectEntity> projectIterator = projectRepository.findAll().iterator();
        while (projectIterator.hasNext()) {
            ProjectEntity projectEntity = projectIterator.next();
            ProjectDto projectDto = dozerMapper.map(projectEntity, ProjectDto.class);
            EmployerDto employerDto = companyService.findByUserName(projectDto.getAuthorEmail());

            if (employerDto != null) {
                projectDto.setCompanyName(employerDto.getCompanyName());
                projectDto.setCompanyLogoURL(employerDto.getCompanyLogoURL());
            }
            projects.add(projectDto);
        }

        return projects.stream().sorted((project1, project2) -> {
            if (project2.getProjectId() > project1.getProjectId()) {
                return 1;
            } else if (project2.getProjectId() < project1.getProjectId()) {
                return -1;
            }
            return 0;
        }).collect(Collectors.toList());
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
        List<EmployerEntity> employerEntities = companySearchResultRepository.search(searchQueryBuilder.build()).getContent();

        if (!employerEntities.isEmpty()) {
            EmployerEntity employerEntity = employerEntities.get(0);
            EmployerDto employerDto = dozerMapper.map(employerEntity, EmployerDto.class);
            if (!employerEntity.getEmployers().isEmpty()) {
                Employer employer = employerEntity.getEmployers().get(0);
                employerDto.setCreatedDate(simpleDateFormat.format(employer.getCreatedDate()));
            }
            projectDetail.setCompany(employerDto);
        }
        return projectDetail;
    }

    @Override
    public long joinProject(ProjectRegistrantDto projectRegistrantDto) throws MessagingException, IOException, TemplateException {
        Long projectId = projectRegistrantDto.getProjectId();
        boolean isExist = checkIfProjectRegistrantExist(projectId, projectRegistrantDto.getRegistrantEmail());

        if (!isExist) {
            ProjectRegistrantEntity projectRegistrantEntity = dozerMapper.map(projectRegistrantDto, ProjectRegistrantEntity.class);
            ProjectEntity projectEntity = projectRepository.findOne(projectId);

            try {
                sendEmailAlertJobSeekerApplyJob(projectEntity, projectRegistrantEntity);
                sendEmailAlertEmployerApplyJob(projectEntity, projectRegistrantEntity);
                projectRegistrantEntity.setMailSent(Boolean.TRUE);
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
            }

            projectRegistrantEntity.setProjectRegistrantId(new Date().getTime());
            projectRegistrantRepository.save(projectRegistrantEntity);
        }
        return getNumberOfRegistrants(projectId);
    }

    @Override
    public void sendEmailAlertJobSeekerApplyJob(ProjectEntity projectEntity, ProjectRegistrantEntity projectRegistrantEntity)
            throws MessagingException, IOException, TemplateException {
        Template template = projectRegistrantEntity.getLang() == Language.vi ?
                alertJobSeekerApplyJobMailTemplateVi : alertJobSeekerApplyJobMailTemplateEn;
        String mailSubject = projectRegistrantEntity.getLang() == Language.vi ?
                alertJobSeekerApplyJobMailSubjectVn : alertJobSeekerApplyJobMailSubjectEn;
        mailSubject = String.format(mailSubject, projectEntity.getProjectTitle());
        Address[] emailAddress = InternetAddress.parse(projectRegistrantEntity.getRegistrantEmail());
        sendEmailAlertApplyJob(projectEntity, projectRegistrantEntity, mailSubject, emailAddress, template, false);
    }

    @Override
    public void sendEmailAlertEmployerApplyJob(ProjectEntity projectEntity, ProjectRegistrantEntity projectRegistrantEntity)
            throws MessagingException, IOException, TemplateException {
        Template template = projectRegistrantEntity.getLang() == Language.vi ?
                alertEmployerApplyJobMailTemplateVi : alertEmployerApplyJobMailTemplateEn;
        String mailSubject = projectRegistrantEntity.getLang() == Language.vi ?
                alertEmployerApplyJobMailSubjectVn : alertEmployerApplyJobMailSubjectEn;
        mailSubject = String.format(mailSubject, projectEntity.getProjectTitle());
        Address[] emailAddress = InternetAddress.parse(projectEntity.getAuthorEmail());
        sendEmailAlertApplyJob(projectEntity, projectRegistrantEntity, mailSubject, emailAddress, template, true);
    }

    @Override
    public Long getNumberOfRegistrants(Long projectId) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withSearchType(SearchType.COUNT);
        searchQueryBuilder.withFilter(FilterBuilders.termFilter("projectId", projectId));
        return projectRegistrantRepository.search(searchQueryBuilder.build()).getTotalElements();
    }

    private void sendEmailAlertApplyJob(ProjectEntity projectEntity, ProjectRegistrantEntity projectRegistrantEntity,
                                        String mailSubject, Address[] recipientAddresses, Template template, boolean hasReplyTo)
            throws MessagingException, IOException, TemplateException {
        applyJobMailMessage.setRecipients(Message.RecipientType.TO, recipientAddresses);

        if (hasReplyTo) {
            applyJobMailMessage.setReplyTo(InternetAddress.parse(projectRegistrantEntity.getRegistrantEmail()));
        } else {
            applyJobMailMessage.setReplyTo(InternetAddress.parse(mailTechlooperReplyTo));
        }

        StringWriter stringWriter = new StringWriter();
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

        template.process(templateModel, stringWriter);
        String formatMailSubject = String.format(mailSubject, projectEntity.getProjectTitle());
        applyJobMailMessage.setSubject(MimeUtility.encodeText(formatMailSubject, "UTF-8", null));
        applyJobMailMessage.setText(stringWriter.toString(), "UTF-8", "html");

        stringWriter.flush();
        applyJobMailMessage.saveChanges();
        mailSender.send(applyJobMailMessage);
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


    public Collection<ProjectDto> findByOwner(String owner) {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder().withIndices(techlooperIndex).withTypes("project");
        QueryStringQueryBuilder query = queryStringQuery(owner).defaultField("authorEmail");
        queryBuilder.withFilter(queryFilter(query));

        int pageIndex = 0;
        Set<ProjectDto> projects = new HashSet<>();
        while (true) {
            queryBuilder.withPageable(new PageRequest(pageIndex++, 100));
            FacetedPage<ProjectEntity> page = projectRepository.search(queryBuilder.build());
            if (!page.hasContent()) {
                break;
            }
            page.spliterator().forEachRemaining(project -> {
                ProjectDto projectDto = dozerMapper.map(project, ProjectDto.class);
                projectDto.setNumberOfApplications(countRegistrantsByProjectId(project.getProjectId()));
                projects.add(projectDto);
            });
        }
        return projects;
    }

    public Collection<ProjectRegistrantDto> findRegistrantsByProjectId(Long projectId) {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder().withIndices(techlooperIndex).withTypes("projectRegistrant");
        queryBuilder.withFilter(queryFilter(termQuery("projectId", projectId)));

        int pageIndex = 0;
        Set<ProjectRegistrantDto> registrants = new HashSet<>();
        while (true) {
            queryBuilder.withPageable(new PageRequest(pageIndex++, 100));
            FacetedPage<ProjectRegistrantEntity> page = projectRegistrantRepository.search(queryBuilder.build());
            if (!page.hasContent()) {
                break;
            }
            page.spliterator().forEachRemaining(registrant -> registrants.add(dozerMapper.map(registrant, ProjectRegistrantDto.class)));
        }
        return registrants;
    }

    public Long countRegistrantsByProjectId(Long projectId) {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder().withIndices(techlooperIndex).withTypes("projectRegistrant");
        queryBuilder.withFilter(queryFilter(termQuery("projectId", projectId))).withSearchType(SearchType.COUNT);
        return projectRegistrantRepository.search(queryBuilder.build()).getTotalElements();
    }
}
