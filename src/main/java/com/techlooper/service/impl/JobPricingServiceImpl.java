package com.techlooper.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.techlooper.entity.GetPromotedEntity;
import com.techlooper.entity.JobEntity;
import com.techlooper.entity.PriceJobEntity;
import com.techlooper.model.*;
import com.techlooper.repository.elasticsearch.GetPromotedRepository;
import com.techlooper.repository.elasticsearch.PriceJobReportRepository;
import com.techlooper.service.*;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.elasticsearch.action.search.SearchType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Created by NguyenDangKhoa on 5/18/15.
 */
@Service
public class JobPricingServiceImpl implements JobPricingService {

    private static final int MINIMUM_NUMBER_OF_JOBS = 10;

    private static final double[] percents = new double[]{10D, 25D, 50D, 75D, 90D};

    @Resource
    private GetPromotedRepository getPromotedRepository;

    @Resource
    private JobStatisticService jobStatisticService;

    @Resource
    private MimeMessage getPromotedMailMessage;

    @Value("${web.baseUrl}")
    private String webBaseUrl;

    @Resource
    private JsonNode vietnamworksConfiguration;

    @Resource
    private EmailService emailService;

    @Resource
    private JobSearchService jobSearchService;

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    @Resource
    private JobQueryBuilder jobQueryBuilder;

    @Resource
    private PriceJobReportRepository priceJobReportRepository;

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

    @Override
    public void priceJob(PriceJobEntity priceJobEntity) {
        NativeSearchQueryBuilder queryBuilder = jobQueryBuilder.getSearchQueryForPriceJobReport(priceJobEntity);

        Set<JobEntity> jobs = new HashSet<>(getJobSearchResult(queryBuilder));

        // In case total number of jobs search result is less than 10, add more jobs from search by skills
        if (jobs.size() > 0 && jobs.size() < MINIMUM_NUMBER_OF_JOBS) {
            jobs.addAll(searchMoreJobBySkills(priceJobEntity.getSkills(), priceJobEntity.getJobCategories()));
        }

        calculateSalaryPercentile(priceJobEntity, jobs);
        priceJobReportRepository.save(priceJobEntity);
    }

    @Override
    public boolean savePriceJobSurvey(PriceJobSurvey priceJobSurvey) {
        PriceJobEntity priceJobEntity = priceJobReportRepository.findOne(priceJobSurvey.getPriceJobId());
        if (priceJobEntity != null) {
            priceJobEntity.setPriceJobSurvey(priceJobSurvey);
            priceJobReportRepository.save(priceJobEntity);
            return true;
        }
        return false;
    }

    private List<JobEntity> searchMoreJobBySkills(List<String> skills, List<Long> jobCategories) {
        List<JobEntity> jobs = new ArrayList<>();
        if (skills != null && !skills.isEmpty()) {
            NativeSearchQueryBuilder jobSearchQueryBySkill =
                    jobQueryBuilder.getJobSearchQueryBySkill(skills, jobCategories);
            List<JobEntity> jobBySkills = getJobSearchResult(jobSearchQueryBySkill);
            jobs.addAll(jobBySkills);
        }
        return jobs;
    }

    private void calculateSalaryPercentile(PriceJobEntity priceJobEntity, Set<JobEntity> jobs) {
        PriceJobReport priceJobReport = new PriceJobReport();

        double[] salaries = extractSalariesFromJob(jobs);
        List<SalaryRange> salaryRanges = new ArrayList<>();
        Percentile percentile = new Percentile();
        for (double percent : percents) {
            salaryRanges.add(new SalaryRange(percent, Math.floor(percentile.evaluate(salaries, percent))));
        }
        priceJobReport.setPriceJobSalaries(salaryRanges.stream().distinct().collect(toList()));

        // Calculate salary percentile rank for user based on list of salary percentiles from above result
        double targetPay = salaryRanges.stream().filter(
                salaryRange -> salaryRange.getPercent() == 50D).findFirst().get().getPercentile();
        priceJobReport.setTargetPay(Math.floor(targetPay));

        double averagePay = salaryRanges.stream().mapToDouble(
                salaryRange -> salaryRange.getPercentile()).average().getAsDouble();
        priceJobReport.setAverageSalary(averagePay);

        priceJobEntity.setPriceJobReport(priceJobReport);
        priceJobEntity.setCreatedDateTime(new Date().getTime());
    }

    private double[] extractSalariesFromJob(Set<JobEntity> jobs) {
        return jobs.stream().mapToDouble(job ->
                jobSearchService.getAverageSalary(job.getSalaryMin(), job.getSalaryMax())).toArray();
    }

    private List<JobEntity> getJobSearchResult(NativeSearchQueryBuilder queryBuilder) {
        queryBuilder.withSearchType(SearchType.DEFAULT);
        long totalJobs = elasticsearchTemplate.count(queryBuilder.build());
        long totalPage = totalJobs % 100 == 0 ? totalJobs / 100 : totalJobs / 100 + 1;
        int pageIndex = 0;
        List<JobEntity> jobs = new ArrayList<>();
        while (pageIndex < totalPage) {
            queryBuilder.withPageable(new PageRequest(pageIndex, 100));
            jobs.addAll(elasticsearchTemplate.queryForPage(queryBuilder.build(), JobEntity.class).getContent());
            pageIndex++;
        }
        return jobs;
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
}
