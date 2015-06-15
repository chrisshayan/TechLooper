package com.techlooper.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.techlooper.entity.SalaryReviewEntity;
import com.techlooper.model.*;
import com.techlooper.repository.JobSearchAPIConfigurationRepository;
import com.techlooper.repository.elasticsearch.SalaryReviewRepository;
import com.techlooper.service.JobQueryBuilder;
import com.techlooper.service.JobStatisticService;
import com.techlooper.service.SalaryReviewService;
import com.techlooper.util.JsonUtils;
import com.techlooper.util.RestTemplateUtils;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.dozer.Mapper;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.elasticsearch.index.query.FilterBuilders.*;
import static org.elasticsearch.index.query.QueryBuilders.filteredQuery;

/**
 * Created by NguyenDangKhoa on 5/18/15.
 */
@Service
public class SalaryReviewServiceImpl implements SalaryReviewService {

    public static final long MIN_SALARY_ACCEPTABLE = 250L;

    public static final long MAX_SALARY_ACCEPTABLE = 5000L;

    @Resource
    private SalaryReviewRepository salaryReviewRepository;

    @Resource
    private JobStatisticService jobStatisticService;

    @Resource
    private JobQueryBuilder jobQueryBuilder;

    @Value("${elasticsearch.index.name}")
    private String elasticSearchIndexName;

    @Resource
    private MimeMessage salaryReviewMailMessage;

    @Resource
    private JavaMailSender mailSender;

    @Resource
    private Template salaryReviewReportTemplateEn;

    @Resource
    private Template salaryReviewReportTemplateVi;

    @Resource
    private MimeMessage getPromotedMailMessage;

    @Resource
    private Template getPromotedTemplateEn;

    @Resource
    private Template getPromotedTemplateVi;

    @Value("${web.baseUrl}")
    private String webBaseUrl;

    @Resource
    private JsonNode vietnamworksConfiguration;

    @Value("${mail.salaryReview.subject.vn}")
    private String salaryReviewSubjectVn;

    @Value("${mail.salaryReview.subject.en}")
    private String salaryReviewSubjectEn;

    @Value("${mail.getPromoted.subject.vn}")
    private String getPromotedSubjectVn;

    @Value("${mail.getPromoted.subject.en}")
    private String getPromotedSubjectEn;

    @Value("${vnw.api.users.createJobAlert.url}")
    private String vnwCreateJobAlertUrl;

    @Resource
    private Mapper dozerMapper;

    @Resource
    private JobSearchAPIConfigurationRepository apiConfiguration;

    @Resource
    private RestTemplate restTemplate;

    @Override
    public List<SalaryReviewEntity> searchSalaryReview(SalaryReviewEntity salaryReviewEntity) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MONTH, -6);
        QueryBuilder queryBuilder = filteredQuery(jobQueryBuilder.jobTitleQueryBuilder(salaryReviewEntity.getJobTitle()),
                boolFilter().must(termsFilter("jobCategories", salaryReviewEntity.getJobCategories()))
                        .must(rangeFilter("netSalary").from(MIN_SALARY_ACCEPTABLE).to(MAX_SALARY_ACCEPTABLE))
                        .must(rangeFilter("createdDateTime").from(now.getTimeInMillis())));

        List<SalaryReviewEntity> salaryReviewEntities = new ArrayList<>();
        FacetedPage<SalaryReviewEntity> salaryReviewFirstPage = salaryReviewRepository.search(queryBuilder, new PageRequest(0, 100));
        salaryReviewEntities.addAll(salaryReviewFirstPage.getContent());

        int totalPage = salaryReviewFirstPage.getTotalPages();
        int pageIndex = 1;
        while (pageIndex < totalPage) {
            salaryReviewEntities.addAll(salaryReviewRepository.search(queryBuilder, new PageRequest(pageIndex, 100)).getContent());
            pageIndex++;
        }
        return salaryReviewEntities;
    }

    public void sendSalaryReviewReportEmail(EmailRequest emailRequest) throws IOException, TemplateException, MessagingException {
        SalaryReviewEntity salaryReviewEntity = salaryReviewRepository.findOne(emailRequest.getSalaryReviewId());
        salaryReviewEntity.setEmail(emailRequest.getEmail());
        salaryReviewRepository.save(salaryReviewEntity);

        if (!salaryReviewEntity.getSalaryReport().getPercentRank().isNaN()) {
            salaryReviewMailMessage.setRecipients(Message.RecipientType.TO, emailRequest.getEmail());
            StringWriter stringWriter = new StringWriter();
            Template template = emailRequest.getLang() == Language.vi ? salaryReviewReportTemplateVi : salaryReviewReportTemplateEn;

            Map<String, Object> templateModel = new HashMap<>();

            templateModel.put("id", Base64.getEncoder().encodeToString(salaryReviewEntity.getCreatedDateTime().toString().getBytes()));
            templateModel.put("salaryReview", salaryReviewEntity);
            templateModel.put("webBaseUrl", webBaseUrl);

            String configLang = "lang_" + emailRequest.getLang().getValue();
            templateModel.put("jobLevel", vietnamworksConfiguration.findPath(salaryReviewEntity.getJobLevelIds().toString()).get(configLang).asText());
            templateModel.put("jobSkills", salaryReviewEntity.getSkills().stream().collect(Collectors.joining(" | ")));

            JsonNode categories = vietnamworksConfiguration.findPath("categories");
            List<String> categoryIds = categories.findValuesAsText("category_id");
            List<String> list = new ArrayList<>();
            salaryReviewEntity.getJobCategories().stream()
                    .map(aLong -> aLong.toString())
                    .forEach(jobCategory -> list.add(categories.get(categoryIds.indexOf(jobCategory)).get(configLang).asText()));
            templateModel.put("jobCategories", list.stream().collect(Collectors.joining(" | ")));

            JsonNode locations = vietnamworksConfiguration.findPath("locations");
            List<String> locationIds = vietnamworksConfiguration.findValuesAsText("location_id");
            templateModel.put("location", locations.get(locationIds.indexOf(salaryReviewEntity.getLocationId().toString())).get(configLang).asText());

            templateModel.put("date", new SimpleDateFormat("dd/MM/yyyy").format(new Date(salaryReviewEntity.getCreatedDateTime())));

            List<SalaryRange> lessSalaryRanges = new ArrayList<>();
            List<SalaryRange> moreSalaryRanges = new ArrayList<>();
            List<SalaryRange> salaryRanges = salaryReviewEntity.getSalaryReport().getSalaryRanges();
            salaryRanges.forEach(salaryRange -> {
                if (salaryRange.getPercent() > salaryReviewEntity.getSalaryReport().getPercentRank()) {
                    moreSalaryRanges.add(salaryRange);
                } else if (salaryRange.getPercent() < salaryReviewEntity.getSalaryReport().getPercentRank()) {
                    lessSalaryRanges.add(salaryRange);
                }
            });

            lessSalaryRanges.sort((sala, salb) -> salb.getPercent().compareTo(sala.getPercent()));
            moreSalaryRanges.sort((sala, salb) -> salb.getPercent().compareTo(sala.getPercent()));

            templateModel.put("lessSalaryRanges", lessSalaryRanges);
            templateModel.put("moreSalaryRanges", moreSalaryRanges);

            template.process(templateModel, stringWriter);

            String emailSubject = String.format("vn".equalsIgnoreCase(emailRequest.getLang().getValue()) ?
                    salaryReviewSubjectVn : salaryReviewSubjectEn, salaryReviewEntity.getJobTitle());
            salaryReviewMailMessage.setSubject(MimeUtility.encodeText(emailSubject, "UTF-8", null));
            salaryReviewMailMessage.setText(stringWriter.toString(), "UTF-8", "html");

            stringWriter.flush();

            mailSender.send(salaryReviewMailMessage);
        }
    }

    public void createVnwJobAlert(VnwJobAlertRequest vnwJobAlertRequest) {
        // Save job alert email
        SalaryReviewEntity salaryReviewEntity = salaryReviewRepository.findOne(vnwJobAlertRequest.getSalaryReviewId());
        if (salaryReviewEntity != null) {
            salaryReviewEntity.setJobAlertEmail(vnwJobAlertRequest.getEmail());
            salaryReviewRepository.save(salaryReviewEntity);
        }

        // Call Vietnamworks api to register job alert
        VnwJobAlert jobAlert = dozerMapper.map(vnwJobAlertRequest, VnwJobAlert.class);
        String params = JsonUtils.toJSON(jobAlert).orElse(EMPTY);
        HttpEntity<String> requestEntity = RestTemplateUtils.configureHttpRequestEntity(
                MediaType.APPLICATION_JSON, apiConfiguration.getApiKeyName(), apiConfiguration.getApiKeyValue(), params);
        restTemplate.exchange(vnwCreateJobAlertUrl, HttpMethod.POST, requestEntity, String.class);
    }

    @Override
    public void sendTopDemandedSkillsEmail(GetPromotedEmailRequest emailRequest) throws MessagingException, IOException, TemplateException {
        getPromotedMailMessage.setRecipients(Message.RecipientType.TO, emailRequest.getEmail());
        StringWriter stringWriter = new StringWriter();
        Template template = emailRequest.getLang() == Language.vi ? getPromotedTemplateVi : getPromotedTemplateEn;

        Map<String, Object> templateModel = new HashMap<>();
        GetPromotedRequest getPromotedRequest = emailRequest.getGetPromotedRequest();
        String configLang = "lang_" + emailRequest.getLang().getValue();

        templateModel.put("jobTitle", getPromotedRequest.getJobTitle());
        templateModel.put("jobLevel", vietnamworksConfiguration.findPath(getPromotedRequest.getJobLevelId().toString()).get(configLang).asText());
        templateModel.put("jobLevelId", getPromotedRequest.getJobLevelId());
        templateModel.put("webBaseUrl", webBaseUrl);

        JsonNode categories = vietnamworksConfiguration.findPath("categories");
        List<String> categoryIds = categories.findValuesAsText("category_id");
        List<String> list = new ArrayList<>();
        getPromotedRequest.getJobCategoryIds().stream()
                .map(aLong -> aLong.toString())
                .forEach(jobCategory -> list.add(categories.get(categoryIds.indexOf(jobCategory)).get(configLang).asText()));
        templateModel.put("jobCategories", list.stream().collect(Collectors.joining(" | ")));
        templateModel.put("jobCategoryIds", list.stream().collect(Collectors.joining(",")));

        GetPromotedResponse response = jobStatisticService.getTopDemandedSkillsByJobTitle(getPromotedRequest);
        templateModel.put("totalJob", response.getTotalJob());
        templateModel.put("salaryMin", response.getSalaryMin());
        templateModel.put("salaryMax", response.getSalaryMax());
        templateModel.put("topDemandedSkills", response.getTopDemandedSkills());
        SimpleDateFormat simpleDateFormat = emailRequest.getLang() == Language.vi ?
                new SimpleDateFormat("dd/MM/yyyy") : new SimpleDateFormat("MM/dd/yyyy");
        templateModel.put("sentDate", simpleDateFormat.format(new Date()));
        templateModel.put("language", emailRequest.getLang().getValue());

        template.process(templateModel, stringWriter);

        String emailSubject = emailRequest.getLang() == Language.vi ? getPromotedSubjectVn : getPromotedSubjectEn;
        emailSubject = String.format(emailSubject, getPromotedRequest.getJobTitle());
        getPromotedMailMessage.setSubject(MimeUtility.encodeText(emailSubject, "UTF-8", null));
        getPromotedMailMessage.setText(stringWriter.toString(), "UTF-8", "html");

        stringWriter.flush();
        mailSender.send(getPromotedMailMessage);
    }
}
