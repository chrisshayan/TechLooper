package com.techlooper.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.techlooper.entity.JobEntity;
import com.techlooper.entity.SalaryReviewEntity;
import com.techlooper.model.*;
import com.techlooper.repository.elasticsearch.SalaryReviewRepository;
import com.techlooper.repository.elasticsearch.VietnamworksJobRepository;
import com.techlooper.service.EmailService;
import com.techlooper.service.JobSearchService;
import com.techlooper.service.SalaryReviewService;
import com.techlooper.service.SuggestionService;
import com.techlooper.util.DateTimeUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.dozer.Mapper;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.elasticsearch.index.query.FilterBuilders.*;
import static org.elasticsearch.index.query.QueryBuilders.filteredQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

/**
 * Created by NguyenDangKhoa on 5/18/15.
 */
@Service
public class SalaryReviewServiceImpl implements SalaryReviewService {

    private static final int TWO_PERCENTILES = 2;

    private static final int LIMIT_NUMBER_OF_JOBS_FOR_SALARY_REVIEW = 1000;

    private static final double[] percents = new double[]{10D, 25D, 50D, 75D, 90D};

    @Resource
    private MimeMessage salaryReviewMailMessage;

    @Value("${web.baseUrl}")
    private String webBaseUrl;

    @Resource
    private JsonNode vietnamworksConfiguration;

    @Resource
    private Mapper dozerMapper;

    @Resource
    private EmailService emailService;

    @Resource
    private JobSearchService jobSearchService;

    @Resource
    private SalaryReviewRepository salaryReviewRepository;

    @Resource
    private VietnamworksJobRepository vietnamworksJobRepository;

    @Resource
    private SuggestionService suggestionService;

    @Override
    public void sendSalaryReviewReportEmail(SalaryReviewEmailRequest salaryReviewEmailRequest) {
        Optional<SalaryReviewEntity> salaryReviewEntityOptional = Optional.ofNullable(
                salaryReviewRepository.findOne(salaryReviewEmailRequest.getSalaryReviewId()));
        if (salaryReviewEntityOptional.isPresent()) {
            SalaryReviewEntity salaryReviewEntity = salaryReviewEntityOptional.get();
            salaryReviewEntity.setEmail(salaryReviewEmailRequest.getEmail());
            salaryReviewRepository.save(salaryReviewEntity);

            boolean hasSalaryReport = !salaryReviewEntity.getSalaryReport().getPercentRank().isNaN();
            if (hasSalaryReport) {
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

    @Override
    public List<SimilarSalaryReview> getSimilarSalaryReview(SimilarSalaryReviewRequest request) {
        NativeSearchQueryBuilder searchQueryBuilder = getSimilarSalaryReviewQueryBuilder(request);
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
                similarSalaryReview2.getNetSalary() - similarSalaryReview1.getNetSalary()).collect(toList());
        return similarSalaryReviews;
    }

    @Override
    public SalaryReviewResultDto reviewSalary(SalaryReviewDto salaryReviewDto) {
        SalaryReviewJobRepository jobRepository = new SalaryReviewJobRepository();
        SalaryReviewCondition salaryReviewCondition = dozerMapper.map(salaryReviewDto, SalaryReviewCondition.class);

        List<String> normalizedJobTitleCandidates = suggestionService.searchJobTitles(salaryReviewCondition.getJobTitle());
        String standardJobTitle = chooseTheBestJobTitle(normalizedJobTitleCandidates);
        if (StringUtils.isNotEmpty(standardJobTitle)) {
            salaryReviewCondition.setJobTitle(standardJobTitle);
        }

        JobSearchStrategy searchBySalaryStrategy = new JobSearchBySalaryStrategy(salaryReviewCondition, vietnamworksJobRepository);
        jobRepository.addStrategy(searchBySalaryStrategy);

        JobSearchStrategy searchBySimilarSalaryReviewStrategy = new SimilarSalaryReviewSearchStrategy(
                salaryReviewCondition, salaryReviewRepository);
        jobRepository.addStrategy(searchBySimilarSalaryReviewStrategy);

        if (jobRepository.isNotEmpty() && jobRepository.isNotEnough()) {
            JobSearchStrategy searchBySkillStrategy = new JobSearchBySkillStrategy(vietnamworksJobRepository, salaryReviewCondition);
            jobRepository.addStrategy(searchBySkillStrategy);
        }

        if (jobRepository.isNotEmpty() && jobRepository.isNotEnough()) {
            JobSearchStrategy searchByTitleStrategy = new JobSearchByTitleStrategy(vietnamworksJobRepository, salaryReviewCondition);
            jobRepository.addStrategy(searchByTitleStrategy);
        }

        Set<JobEntity> jobForReview = jobRepository.getJobs().stream().limit(LIMIT_NUMBER_OF_JOBS_FOR_SALARY_REVIEW).collect(toSet());

        SalaryReviewResultDto salaryReviewResult = generateSalaryReport(salaryReviewDto, jobForReview);
        return salaryReviewResult;
    }

    @Override
    public void saveSalaryReviewResult(SalaryReviewResultDto salaryReviewResult) {
        salaryReviewRepository.save(dozerMapper.map(salaryReviewResult, SalaryReviewEntity.class));
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

    @Override
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

    private String chooseTheBestJobTitle(List<String> normalizedJobTitleCandidates) {
        List<String> result = new ArrayList<>();
        for (String normalizedJobTitleCandidate : normalizedJobTitleCandidates) {
            result.add(StringUtils.trim(normalizedJobTitleCandidate.replaceAll("\\d+.*", " ")));
        }
        Optional<String> jobTitleOptional = result.stream().distinct().sorted(
                (source, destination) -> source.length() - destination.length()).findFirst();
        if (jobTitleOptional.isPresent()) {
            return jobTitleOptional.get();
        }
        return null;
    }

    private SalaryReviewResultDto generateSalaryReport(SalaryReviewDto salaryReviewDto, Set<JobEntity> jobs) {
        SalaryReport salaryReport = new SalaryReport();
        salaryReport.setNetSalary(salaryReviewDto.getNetSalary());

        double[] salaries = extractSalariesFromJob(jobs);
        Map<Double, Double> salaryPercentileMap = new HashMap<>();
        Percentile percentile = new Percentile();
        for (double percent : percents) {
            salaryPercentileMap.put(Math.floor(percentile.evaluate(salaries, percent)), percent);
        }

        List<SalaryRange> salaryRanges = new ArrayList<>();
        for (Map.Entry<Double, Double> entry : salaryPercentileMap.entrySet()) {
            salaryRanges.add(new SalaryRange(entry.getValue(), entry.getKey()));
        }
        salaryRanges = salaryRanges.stream().sorted((source, destination) ->
                Double.valueOf(source.getPercent() - destination.getPercent()).intValue()).collect(toList());
        salaryReport.setSalaryRanges(salaryRanges);

        // Calculate salary percentile rank for user based on list of salary percentiles from above result
        double percentRank = Double.NaN;
        if (salaryReport.getNetSalary() != null) {
            percentRank = calculatePercentPosition(salaryReport);
        }
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
        List<SalaryRange> noDuplicatedSalaryRanges = salaryReport.getSalaryRanges();
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
        templateModel.put("jobSkills", salaryReviewEntity.getSkills().stream().collect(joining(" | ")));
        JsonNode categories = vietnamworksConfiguration.findPath("categories");
        List<String> categoryIds = categories.findValuesAsText("category_id");
        List<String> list = new ArrayList<>();
        final String language = configLang;
        salaryReviewEntity.getJobCategories().stream()
                .map(aLong -> aLong.toString())
                .forEach(jobCategory -> list.add(categories.get(categoryIds.indexOf(jobCategory)).get(language).asText()));
        templateModel.put("jobCategories", list.stream().collect(joining(" | ")));

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

    private NativeSearchQueryBuilder getSimilarSalaryReviewQueryBuilder(SimilarSalaryReviewRequest request) {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (CollectionUtils.isNotEmpty(request.getSkills())) {
            request.getSkills().stream().forEach(skill -> boolQueryBuilder.should(matchQuery("skills", skill).minimumShouldMatch("100%")));
        }
        if (StringUtils.isNotEmpty(request.getJobTitle())) {
            boolQueryBuilder.must(matchQuery("jobTitle", request.getJobTitle()).minimumShouldMatch("100%"));
        }

        BoolFilterBuilder boolFilterBuilder = boolFilter();
        if (CollectionUtils.isNotEmpty(request.getJobLevelIds())) {
            boolFilterBuilder.should(termsFilter("jobLevelIds", request.getJobLevelIds()));
        }
        if (request.getLocationId() != null) {
            boolFilterBuilder.should(termFilter("locationId", request.getLocationId()));
        }
        if (request.getCompanySizeId() != null) {
            boolFilterBuilder.should(termFilter("companySizeId", request.getCompanySizeId()));
        }
        if (CollectionUtils.isNotEmpty(request.getJobCategories())) {
            boolFilterBuilder.must(termsFilter("jobCategories", request.getJobCategories()));
        }
        // ES Range Query From Clause (i.e greater or equal), we just want greater, not equal. So plus 1 to criterion
        if (request.getNetSalary() != null && request.getNetSalary() > 0) {
            boolFilterBuilder.must(rangeFilter("netSalary").from(request.getNetSalary() + 1));
        }

        queryBuilder.withQuery(filteredQuery(boolQueryBuilder, boolFilterBuilder));
        queryBuilder.withPageable(new PageRequest(0, 3));
        return queryBuilder;
    }

}
