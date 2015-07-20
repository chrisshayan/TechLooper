package com.techlooper.service.impl;

import com.techlooper.entity.EmployerEntity;
import com.techlooper.entity.ProjectEntity;
import com.techlooper.entity.ProjectRegistrantEntity;
import com.techlooper.model.*;
import com.techlooper.repository.elasticsearch.CompanySearchResultRepository;
import com.techlooper.repository.elasticsearch.ProjectRegistrantRepository;
import com.techlooper.repository.elasticsearch.ProjectRepository;
import com.techlooper.service.ProjectService;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${mail.alertJobSeekerApplyJob.subject.en}")
    private String alertJobSeekerApplyJobMailSubjectEn;

    @Value("${mail.alertJobSeekerApplyJob.subject.vn}")
    private String alertJobSeekerApplyJobMailSubjectVn;

    @Value("${mail.alertEmployerApplyJob.subject.en}")
    private String alertEmployerApplyJobMailSubjectEn;

    @Value("${mail.alertEmployerApplyJob.subject.vn}")
    private String alertEmployerApplyJobMailSubjectVn;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public ProjectEntity saveProject(ProjectDto projectDto) {
        ProjectEntity projectEntity = dozerMapper.map(projectDto, ProjectEntity.class);
        Date currentDate = new Date();
        projectEntity.setProjectId(currentDate.getTime());
        projectEntity.setCreatedDate(simpleDateFormat.format(currentDate));
        return projectRepository.save(projectEntity);
    }

    @Override
    public List<ProjectDto> listProject() {
        List<ProjectDto> projects = new ArrayList<>();
        Iterator<ProjectEntity> projectIterator = projectRepository.findAll().iterator();
        while (projectIterator.hasNext()) {
            ProjectEntity projectEntity = projectIterator.next();
            ProjectDto projectDto = dozerMapper.map(projectEntity, ProjectDto.class);
            projects.add(projectDto);
        }
        return projects;
    }

    @Override
    public ProjectDetailDto getProjectDetail(Long projectId) {
        ProjectDetailDto projectDetail = new ProjectDetailDto();
        ProjectEntity projectEntity = projectRepository.findOne(projectId);
        ProjectDto project = dozerMapper.map(projectEntity, ProjectDto.class);
        projectDetail.setProject(project);

        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withQuery(QueryBuilders.nestedQuery("employers",
                QueryBuilders.matchPhraseQuery("employers.userName", project.getAuthorEmail())));
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
            //sendEmailAlertJobSeekerApplyJob(projectEntity, projectRegistrantEntity);
            //sendEmailAlertEmployerApplyJob(projectEntity, projectRegistrantEntity);
            projectRegistrantEntity.setMailSent(Boolean.TRUE);
            projectRegistrantEntity.setProjectRegistrantId(new Date().getTime());
            projectRegistrantRepository.save(projectRegistrantEntity);
        }
        return 1;
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
        sendEmailAlertApplyJob(projectEntity, mailSubject, emailAddress, template);
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
        sendEmailAlertApplyJob(projectEntity, mailSubject, emailAddress, template);
    }

    private void sendEmailAlertApplyJob(ProjectEntity projectEntity, String mailSubject, Address[] recipientAddresses, Template template)
            throws MessagingException, IOException, TemplateException {
        applyJobMailMessage.setRecipients(Message.RecipientType.TO, recipientAddresses);
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
        templateModel.put("projectId", projectEntity.getProjectId());
        templateModel.put("projectAlias", projectEntity.getProjectTitle().replaceAll("\\W", "-"));

        template.process(templateModel, stringWriter);
        mailSubject = String.format(mailSubject, projectEntity.getProjectTitle());
        applyJobMailMessage.setSubject(MimeUtility.encodeText(mailSubject, "UTF-8", null));
        applyJobMailMessage.setText(stringWriter.toString(), "UTF-8", "html");

        stringWriter.flush();
        applyJobMailMessage.saveChanges();
        mailSender.send(applyJobMailMessage);
    }

    public boolean checkIfProjectRegistrantExist(Long projectId, String email) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withQuery(QueryBuilders.boolQuery()
                .must(QueryBuilders.matchPhraseQuery("registrantEmail", email))
                .must(QueryBuilders.termQuery("projectId", projectId))
                .must(QueryBuilders.termQuery("mailSent", true)));

        long total = projectRegistrantRepository.search(searchQueryBuilder.build()).getTotalElements();
        return (total > 0);
    }

}
