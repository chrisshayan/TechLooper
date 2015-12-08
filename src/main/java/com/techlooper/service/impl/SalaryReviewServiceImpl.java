package com.techlooper.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.techlooper.entity.JobEntity;
import com.techlooper.entity.SalaryReviewEntity;
import com.techlooper.model.*;
import com.techlooper.repository.JobSearchAPIConfigurationRepository;
import com.techlooper.repository.elasticsearch.SalaryReviewRepository;
import com.techlooper.repository.elasticsearch.VietnamworksJobRepository;
import com.techlooper.service.EmailService;
import com.techlooper.service.JobQueryBuilder;
import com.techlooper.service.JobSearchService;
import com.techlooper.service.SalaryReviewService;
import com.techlooper.util.DataUtils;
import com.techlooper.util.DateTimeUtils;
import com.techlooper.util.JsonUtils;
import com.techlooper.util.RestTemplateUtils;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.dozer.Mapper;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.RangeFilterBuilder;
import org.elasticsearch.index.query.TermsFilterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.elasticsearch.index.query.FilterBuilders.*;
import static org.elasticsearch.index.query.QueryBuilders.filteredQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

/**
 * Created by NguyenDangKhoa on 5/18/15.
 */
@Service
public class SalaryReviewServiceImpl implements SalaryReviewService {

    public static final long MIN_SALARY_ACCEPTABLE = 250L;

    public static final long MAX_SALARY_ACCEPTABLE = 5000L;

    private static final int TWO_PERCENTILES = 2;

    private static final double[] percents = new double[]{10D, 25D, 50D, 75D, 90D};

    @Resource
    private MimeMessage salaryReviewMailMessage;

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

    @Resource
    private JobSearchService jobSearchService;

    @Resource
    private SalaryReviewRepository salaryReviewRepository;

    @Resource
    private JobQueryBuilder jobQueryBuilder;

    @Resource
    private VietnamworksJobRepository vietnamworksJobRepository;

    @Override
    public List<SalaryReviewEntity> findSalaryReview(SalaryReviewDto salaryReviewDto) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("salaryReview");
        MatchQueryBuilder jobTitleQuery = matchQuery("jobTitle", salaryReviewDto.getJobTitle()).minimumShouldMatch("100%");
        TermsFilterBuilder jobCategoriesFilter = termsFilter("jobCategories", salaryReviewDto.getJobCategories());
        RangeFilterBuilder netSalaryFilter = rangeFilter("netSalary").from(MIN_SALARY_ACCEPTABLE).to(MAX_SALARY_ACCEPTABLE);

        Long sixMonthsAgo = DateTimeUtils.minusPeriod(6, ChronoUnit.MONTHS);
        RangeFilterBuilder sixMonthsAgoFilter = rangeFilter("createdDateTime").from(sixMonthsAgo);

        searchQueryBuilder.withQuery(filteredQuery(jobTitleQuery, boolFilter().must(jobCategoriesFilter)
                .must(netSalaryFilter)
                .must(sixMonthsAgoFilter)));

        return DataUtils.getAllEntities(salaryReviewRepository, searchQueryBuilder);
    }

    @Override
    public void sendSalaryReviewReportEmail(SalaryReviewEmailRequest salaryReviewEmailRequest) {
        Optional<SalaryReviewEntity> salaryReviewEntityOptional = Optional.ofNullable(
                salaryReviewRepository.findOne(salaryReviewEmailRequest.getSalaryReviewId()));
        if (salaryReviewEntityOptional.isPresent()) {
            SalaryReviewEntity salaryReviewEntity = salaryReviewEntityOptional.get();
            salaryReviewEntity.setEmail(salaryReviewEmailRequest.getEmail());
            salaryReviewRepository.save(salaryReviewEntity);

            // Send email if the salary review report has result
            if (!salaryReviewEntity.getSalaryReport().getPercentRank().isNaN()) {
                List<String> subjectVariableValues = Arrays.asList(salaryReviewEntity.getJobTitle());
                EmailRequestModel emailRequestModel = new EmailRequestModel.Builder()
                        .withTemplateName(EmailTemplateNameEnum.SALARY_REVIEW.name())
                        .withLanguage(salaryReviewEmailRequest.getLang())
                        .withTemplateModel(buildSalaryReviewEmailTemplateModel(salaryReviewEmailRequest, salaryReviewEntity))
                        .withMailMessage(salaryReviewMailMessage)
                        .withRecipientAddresses(salaryReviewEmailRequest.getEmail())
                        .withSubjectVariableValues(subjectVariableValues).build();
                emailService.sendMail(emailRequestModel);
            }
        }
    }

    private Map<String, Object> buildSalaryReviewEmailTemplateModel(SalaryReviewEmailRequest emailRequest, SalaryReviewEntity salaryReviewEntity) {
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

    @Override
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
    public SalaryReviewResultDto reviewSalary(SalaryReviewDto salaryReviewDto) {
        SalaryReviewJobRepository jobRepository = new SalaryReviewJobRepository();

        JobSearchStrategy searchBySalaryStrategy = new JobSearchBySalaryStrategy(
                salaryReviewDto, jobQueryBuilder, vietnamworksJobRepository);
        jobRepository.addStrategy(searchBySalaryStrategy);

        JobSearchStrategy searchBySimilarSalaryReviewStrategy = new SimilarSalaryReviewSearchStrategy(
                salaryReviewRepository, salaryReviewDto);
        jobRepository.addStrategy(searchBySimilarSalaryReviewStrategy);

        if (jobRepository.isNotEmpty() && jobRepository.isNotEnough()) {
            JobSearchStrategy searchBySkillStrategy = new JobSearchBySkillStrategy(
                    vietnamworksJobRepository, jobQueryBuilder, salaryReviewDto.getSkills(), salaryReviewDto.getJobCategories());
            jobRepository.addStrategy(searchBySkillStrategy);
        }

        if (jobRepository.isNotEmpty() && jobRepository.isNotEnough()) {
            JobSearchStrategy searchByTitleStrategy = new JobSearchByTitleStrategy(
                    vietnamworksJobRepository, jobQueryBuilder, salaryReviewDto.getJobTitle());
            jobRepository.addStrategy(searchByTitleStrategy);
        }

        SalaryReviewResultDto salaryReviewResult = generateSalaryReport(salaryReviewDto, jobRepository.getJobs());
        return salaryReviewResult;
    }

    @Override
    public void saveSalaryReviewResult(SalaryReviewResultDto salaryReviewResult) {
        salaryReviewRepository.save(dozerMapper.map(salaryReviewResult, SalaryReviewEntity.class));
    }

    @Override
    public void deleteSalaryReview(SalaryReviewEntity salaryReviewEntity) {
        salaryReviewRepository.delete(salaryReviewEntity.getCreatedDateTime());
    }

    @Override
    public boolean saveSalaryReviewSurvey(SalaryReviewSurvey salaryReviewSurvey) {
        SalaryReviewEntity salaryReviewEntity = salaryReviewRepository.findOne(salaryReviewSurvey.getSalaryReviewId());
        if (salaryReviewEntity != null) {
            salaryReviewEntity.setSalaryReviewSurvey(salaryReviewSurvey);
            salaryReviewRepository.save(salaryReviewEntity);
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

    private List<JobEntity> searchMoreJobByJobTitle(String jobTitle) {
        NativeSearchQueryBuilder jobSearchQueryByJobTitle = jobQueryBuilder.getJobSearchQueryByJobTitle(jobTitle);
        return getJobSearchResult(jobSearchQueryByJobTitle);
    }

    private SalaryReviewResultDto generateSalaryReport(SalaryReviewDto salaryReviewDto, Set<JobEntity> jobs) {
        SalaryReport salaryReport = new SalaryReport();
        salaryReport.setNetSalary(salaryReviewDto.getNetSalary());

        double[] salaries = extractSalariesFromJob(jobs);
        List<SalaryRange> salaryRanges = new ArrayList<>();
        Percentile percentile = new Percentile();
        for (double percent : percents) {
            salaryRanges.add(new SalaryRange(percent, Math.floor(percentile.evaluate(salaries, percent))));
        }
        salaryReport.setSalaryRanges(salaryRanges);

        // Calculate salary percentile rank for user based on list of salary percentiles from above result
        double percentRank = calculatePercentPosition(salaryReport);
        salaryReport.setPercentRank(Math.floor(percentRank));
        salaryReport.setNumberOfJobs(jobs.size());

        SalaryReviewResultDto salaryReviewResult = dozerMapper.map(salaryReviewDto, SalaryReviewResultDto.class);
        salaryReviewResult.setSalaryReport(salaryReport);
        if (salaryReviewDto.getCreatedDateTime() == null) {
            salaryReviewResult.setCreatedDateTime(DateTimeUtils.currentDateTime());
        } else {
            SalaryReviewEntity currentSalaryReviewEntity = salaryReviewRepository.findOne(salaryReviewDto.getCreatedDateTime());
            salaryReviewResult.setEmail(currentSalaryReviewEntity.getEmail());
            salaryReviewResult.setJobAlertEmail(currentSalaryReviewEntity.getJobAlertEmail());
            salaryReviewResult.setSalaryReviewSurvey(currentSalaryReviewEntity.getSalaryReviewSurvey());
        }

        return salaryReviewResult;
    }

    private double[] extractSalariesFromJob(Set<JobEntity> jobs) {
        return jobs.stream().mapToDouble(job ->
                jobSearchService.getAverageSalary(job.getSalaryMin(), job.getSalaryMax())).toArray();
    }

    private double calculatePercentPosition(SalaryReport salaryReport) {
        //Remove duplicated percentiles if any
        List<SalaryRange> noDuplicatedSalaryRanges = salaryReport.getSalaryRanges().stream().distinct().collect(toList());
        if (noDuplicatedSalaryRanges.size() >= TWO_PERCENTILES) {
            int size = noDuplicatedSalaryRanges.size();
            int i = 0;
            while (i < size && salaryReport.getNetSalary() >= noDuplicatedSalaryRanges.get(i).getPercentile()) {
                i++;
            }

            double position = 0D;
            if (i == 0) {
                SalaryRange firstPercentile = noDuplicatedSalaryRanges.get(0);
                position = salaryReport.getNetSalary() / firstPercentile.getPercentile() * firstPercentile.getPercent();
            } else if (i == size) {
                SalaryRange lastPercentile = noDuplicatedSalaryRanges.get(size - 1);
                position = salaryReport.getNetSalary() / lastPercentile.getPercentile() * lastPercentile.getPercent();
            } else {
                SalaryRange lessPercentile = noDuplicatedSalaryRanges.get(i - 1);
                SalaryRange greaterPercentile = noDuplicatedSalaryRanges.get(i);
                double relativePercentBetweenTwoPercentile = (salaryReport.getNetSalary() - lessPercentile.getPercentile()) /
                        (greaterPercentile.getPercentile() - lessPercentile.getPercentile());
                position = (greaterPercentile.getPercent() - lessPercentile.getPercent()) * relativePercentBetweenTwoPercentile
                        + lessPercentile.getPercent();
            }

            salaryReport.setSalaryRanges(noDuplicatedSalaryRanges);

            position = Math.floor(position);
            if (position == 0D) {
                return 1D;
            } else if (position >= 100D) {
                return 99D;
            } else {
                return position;
            }
        }
        return Double.NaN;
    }

    public List<TopPaidJob> findTopPaidJob(SalaryReviewDto salaryReviewDto) {
        List<TopPaidJob> topPaidJobs = new ArrayList<>();
        List<JobEntity> higherSalaryJobs = jobSearchService.getHigherSalaryJobs(salaryReviewDto);
        Integer netSalary = salaryReviewDto.getNetSalary();
        for (JobEntity jobEntity : higherSalaryJobs) {
            double addedPercent = (jobSearchService.getAverageSalary(jobEntity.getSalaryMin(), jobEntity.getSalaryMax()) - netSalary) /
                    netSalary * 100;
            List<String> skills = jobEntity.getSkills().stream().limit(3).map(skill -> skill.getSkillName()).collect(toList());
            topPaidJobs.add(new TopPaidJob(jobEntity.getId(), jobEntity.getJobTitle(),
                    jobEntity.getCompanyDesc(), Math.ceil(addedPercent), skills));
        }
        return topPaidJobs;
    }

    private void mergeSalaryReviewWithJobList(Set<JobEntity> jobs, List<SalaryReviewEntity> salaryReviewEntities,
                                              SalaryReviewDto salaryReviewDto) {
        if (!salaryReviewEntities.isEmpty() && salaryReviewDto.getCreatedDateTime() != null) {
            salaryReviewEntities.remove(salaryReviewDto);
        }
        for (SalaryReviewEntity entity : salaryReviewEntities) {
            JobEntity jobEntity = new JobEntity();
            jobEntity.setId(entity.getCreatedDateTime().toString());
            jobEntity.setJobTitle(entity.getJobTitle());
            jobEntity.setSalaryMin(entity.getNetSalary().longValue());
            jobEntity.setSalaryMax(entity.getNetSalary().longValue());
            jobs.add(jobEntity);
        }
    }

    private List<JobEntity> getJobSearchResult(NativeSearchQueryBuilder queryBuilder) {
        return DataUtils.getAllEntities(vietnamworksJobRepository, queryBuilder);
    }

}
