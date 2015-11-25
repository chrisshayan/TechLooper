package com.techlooper.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.techlooper.entity.GetPromotedEntity;
import com.techlooper.entity.SalaryReviewEntity;
import com.techlooper.model.*;
import com.techlooper.repository.JobSearchAPIConfigurationRepository;
import com.techlooper.repository.elasticsearch.GetPromotedRepository;
import com.techlooper.repository.elasticsearch.SalaryReviewRepository;
import com.techlooper.service.EmailService;
import com.techlooper.service.JobQueryBuilder;
import com.techlooper.service.JobStatisticService;
import com.techlooper.service.SalaryReviewService;
import com.techlooper.util.JsonUtils;
import com.techlooper.util.RestTemplateUtils;
import org.dozer.Mapper;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.text.DateFormat;
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
    private GetPromotedRepository getPromotedRepository;

    @Resource
    private JobStatisticService jobStatisticService;

    @Resource
    private JobQueryBuilder jobQueryBuilder;

    @Resource
    private MimeMessage salaryReviewMailMessage;

    @Resource
    private MimeMessage getPromotedMailMessage;

    @Value("${web.baseUrl}")
    private String webBaseUrl;

    @Resource
    private JsonNode vietnamworksConfiguration;

    @Value("${vnw.api.users.createJobAlert.url}")
    private String vnwCreateJobAlertUrl;

    @Resource
    private Mapper dozerMapper;

    @Resource
    private JobSearchAPIConfigurationRepository apiConfiguration;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private EmailService emailService;

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

    public void sendSalaryReviewReportEmail(EmailRequest emailRequest) {
        SalaryReviewEntity salaryReviewEntity = salaryReviewRepository.findOne(emailRequest.getSalaryReviewId());
        salaryReviewEntity.setEmail(emailRequest.getEmail());
        salaryReviewRepository.save(salaryReviewEntity);

        if (!salaryReviewEntity.getSalaryReport().getPercentRank().isNaN()) {
            List<String> subjectVariableValues = Arrays.asList(salaryReviewEntity.getJobTitle());
            EmailRequestModel emailRequestModel = new EmailRequestModel.Builder()
                    .withTemplateName(EmailTemplateNameEnum.SALARY_REVIEW.name())
                    .withLanguage(emailRequest.getLang())
                    .withTemplateModel(buildSalaryReviewEmailTemplateModel(emailRequest, salaryReviewEntity))
                    .withMailMessage(salaryReviewMailMessage)
                    .withRecipientAddresses(emailRequest.getEmail())
                    .withSubjectVariableValues(subjectVariableValues).build();
            emailService.sendMail(emailRequestModel);
        }
    }

    private Map<String, Object> buildSalaryReviewEmailTemplateModel(EmailRequest emailRequest, SalaryReviewEntity salaryReviewEntity) {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("id", Base64.getEncoder().encodeToString(salaryReviewEntity.getCreatedDateTime().toString().getBytes()));
        templateModel.put("salaryReview", salaryReviewEntity);
        templateModel.put("webBaseUrl", webBaseUrl);
        String configLang = "lang_en";
        if (emailRequest.getLang() == Language.vi) {
            configLang = "lang_vn";
        }
        templateModel.put("jobLevel", vietnamworksConfiguration.findPath(salaryReviewEntity.getJobLevelIds().toString()).get(configLang).asText());
        templateModel.put("jobSkills", salaryReviewEntity.getSkills().stream().collect(Collectors.joining(" | ")));
        JsonNode categories = vietnamworksConfiguration.findPath("categories");
        List<String> categoryIds = categories.findValuesAsText("category_id");
        List<String> list = new ArrayList<>();
        final String language = configLang;
        salaryReviewEntity.getJobCategories().stream()
                .map(aLong -> aLong.toString())
                .forEach(jobCategory -> list.add(categories.get(categoryIds.indexOf(jobCategory)).get(language).asText()));
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

        lessSalaryRanges.sort((sourceSalary, destinationSalary) -> destinationSalary.getPercent().compareTo(sourceSalary.getPercent()));
        moreSalaryRanges.sort((sourceSalary, destinationSalary) -> destinationSalary.getPercent().compareTo(sourceSalary.getPercent()));

        templateModel.put("lessSalaryRanges", lessSalaryRanges);
        templateModel.put("moreSalaryRanges", moreSalaryRanges);

        return templateModel;
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
    public void sendTopDemandedSkillsEmail(long getPromotedId, GetPromotedEmailRequest emailRequest) {
        GetPromotedRequest getPromotedRequest = emailRequest.getGetPromotedRequest();
        List<String> subjectVariableValues = Arrays.asList(getPromotedRequest.getJobTitle());
        EmailRequestModel emailRequestModel = new EmailRequestModel.Builder()
                .withTemplateName(EmailTemplateNameEnum.HOW_TO_GET_PROMOTED.name())
                .withLanguage(emailRequest.getLang())
                .withTemplateModel(buildGetPromotedEmailTemplateModel(getPromotedId, emailRequest))
                .withMailMessage(getPromotedMailMessage)
                .withRecipientAddresses(emailRequest.getEmail())
                .withSubjectVariableValues(subjectVariableValues).build();
        emailService.sendMail(emailRequestModel);
    }

    private Map<String, Object> buildGetPromotedEmailTemplateModel(long getPromotedId, GetPromotedEmailRequest emailRequest) {
        Map<String, Object> templateModel = new HashMap<>();
        GetPromotedRequest getPromotedRequest = emailRequest.getGetPromotedRequest();
        String configLang = "lang_en";
        if (emailRequest.getLang() == Language.vi) {
            configLang = "lang_en";
        }
        templateModel.put("webBaseUrl", webBaseUrl);

        templateModel.put("getPromotedId", String.valueOf(getPromotedId));
        templateModel.put("jobTitle", getPromotedRequest.getJobTitle());

        List<Integer> jobLevelIds = getPromotedRequest.getJobLevelIds();
        if (jobLevelIds != null && !jobLevelIds.isEmpty()) {
            templateModel.put("jobLevel", vietnamworksConfiguration.findPath(jobLevelIds.toString()).get(configLang).asText());
        } else {
            templateModel.put("jobLevel", null);
        }

        List<Long> jobCategoryIds = getPromotedRequest.getJobCategoryIds();
        if (jobCategoryIds != null && !jobCategoryIds.isEmpty()) {
            JsonNode categories = vietnamworksConfiguration.findPath("categories");
            List<String> categoryIds = categories.findValuesAsText("category_id");
            final String language = configLang;
            List<String> list = new ArrayList<>();
            jobCategoryIds.stream().map(aLong -> aLong.toString())
                    .forEach(jobCategory -> list.add(categories.get(categoryIds.indexOf(jobCategory)).get(language).asText()));
            templateModel.put("jobCategories", list.stream().collect(Collectors.joining(" | ")));
        } else {
            templateModel.put("jobCategories", null);
        }

        GetPromotedResponse response = jobStatisticService.getTopDemandedSkillsByJobTitle(getPromotedRequest);
        templateModel.put("totalJob", response.getTotalJob());
        templateModel.put("salaryMin", Math.ceil(response.getSalaryMin()));
        templateModel.put("salaryMax", Math.ceil(response.getSalaryMax()));
        templateModel.put("topDemandedSkills", response.getTopDemandedSkills());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        templateModel.put("sentDate", dateFormat.format(Calendar.getInstance().getTime()));
        templateModel.put("language", emailRequest.getLang().getValue().toLowerCase());
        return templateModel;
    }

    @Override
    public long saveGetPromotedInformation(GetPromotedEmailRequest getPromotedEmailRequest) {
        Long getPromotedId = getPromotedEmailRequest.getGetPromotedId();
        if (getPromotedId != null && getPromotedId > 0) {
            GetPromotedEntity result = getPromotedRepository.findOne(getPromotedId);
            if (result != null) {
                result.setEmail(getPromotedEmailRequest.getEmail());
                getPromotedRepository.save(result);
            }
            return getPromotedId;
        } else {
            GetPromotedRequest getPromotedRequest = getPromotedEmailRequest.getGetPromotedRequest();
            GetPromotedEntity getPromotedEntity = new GetPromotedEntity();
            getPromotedEntity.setCreatedDateTime(new Date().getTime());
            getPromotedEntity.setJobTitle(getPromotedRequest.getJobTitle());
            getPromotedEntity.setJobLevelIds(getPromotedRequest.getJobLevelIds());
            getPromotedEntity.setJobCategoryIds(getPromotedRequest.getJobCategoryIds());
            getPromotedEntity.setEmail(getPromotedEmailRequest.getEmail());
            getPromotedEntity.setHasResult(getPromotedEmailRequest.getHasResult());
            getPromotedEntity.setCampaign(getPromotedRequest.getCampaign());

            GetPromotedResponse getPromotedResponse = jobStatisticService.getTopDemandedSkillsByJobTitle(getPromotedRequest);
            getPromotedEntity.setGetPromotedResult(getPromotedResponse);

            GetPromotedEntity result = getPromotedRepository.save(getPromotedEntity);
            if (result != null) {
                return result.getCreatedDateTime();
            } else {
                return -1L;
            }
        }
    }

    @Override
    public GetPromotedEntity getPromotedEntity(Long id) {
        return getPromotedRepository.findOne(id);
    }

    @Override
    public List<SimilarSalaryReview> getSimilarSalaryReview(SimilarSalaryReviewRequest request) {
        NativeSearchQueryBuilder searchQueryBuilder = jobQueryBuilder.getSimilarSalaryReview(request);
        List<SalaryReviewEntity> similarSalaryReviewEntities = salaryReviewRepository.search(searchQueryBuilder.build()).getContent();

        List<SimilarSalaryReview> similarSalaryReviews = new ArrayList<>();
        for (SalaryReviewEntity salaryReviewEntity : similarSalaryReviewEntities) {
            SimilarSalaryReview similarSalaryReview = dozerMapper.map(salaryReviewEntity, SimilarSalaryReview.class);
            double addedPercent = salaryReviewEntity.getNetSalary().doubleValue();
            if (request.getNetSalary() != 0) {
                addedPercent = ((double) (salaryReviewEntity.getNetSalary() - request.getNetSalary())) / request.getNetSalary();
            }
            similarSalaryReview.setAddedPercent(Math.ceil(addedPercent * 100));
            similarSalaryReviews.add(similarSalaryReview);
        }

        similarSalaryReviews = similarSalaryReviews.stream().sorted((similarSalaryReview1, similarSalaryReview2) ->
                similarSalaryReview2.getNetSalary() - similarSalaryReview1.getNetSalary()).collect(Collectors.toList());
        return similarSalaryReviews;
    }

    @Override
    public long saveGetPromotedSurvey(GetPromotedSurveyRequest getPromotedSurveyRequest) {
        GetPromotedSurvey getPromotedSurvey = getPromotedSurveyRequest.getGetPromotedSurvey();
        GetPromotedEntity getPromotedEntity = getPromotedRepository.findOne(getPromotedSurvey.getGetPromotedId());
        if (getPromotedEntity != null) {
            getPromotedEntity.setGetPromotedSurvey(getPromotedSurvey);
            GetPromotedEntity result = getPromotedRepository.save(getPromotedEntity);
            return result.getCreatedDateTime();
        } else {
            GetPromotedRequest getPromotedRequest = getPromotedSurveyRequest.getGetPromotedRequest();
            getPromotedEntity = new GetPromotedEntity();
            getPromotedEntity.setCreatedDateTime(new Date().getTime());
            getPromotedEntity.setJobTitle(getPromotedRequest.getJobTitle());
            getPromotedEntity.setJobLevelIds(getPromotedRequest.getJobLevelIds());
            getPromotedEntity.setJobCategoryIds(getPromotedRequest.getJobCategoryIds());
            getPromotedSurvey.setGetPromotedId(getPromotedEntity.getCreatedDateTime());
            getPromotedEntity.setGetPromotedSurvey(getPromotedSurvey);
            getPromotedEntity.setCampaign(getPromotedRequest.getCampaign());

            GetPromotedResponse getPromotedResponse = jobStatisticService.getTopDemandedSkillsByJobTitle(getPromotedRequest);
            getPromotedEntity.setGetPromotedResult(getPromotedResponse);
            if (getPromotedResponse.getTotalJob() > 0) {
                getPromotedEntity.setHasResult(true);
            } else {
                getPromotedEntity.setHasResult(false);
            }
            GetPromotedEntity result = getPromotedRepository.save(getPromotedEntity);
            return result.getCreatedDateTime();
        }
    }
}
