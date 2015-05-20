package com.techlooper.service.impl;

import com.techlooper.entity.JobEntity;
import com.techlooper.entity.SalaryReview;
import com.techlooper.entity.userimport.UserImportEntity;
import com.techlooper.model.*;
import com.techlooper.repository.elasticsearch.SalaryReviewRepository;
import com.techlooper.repository.talentsearch.query.GithubTalentSearchQuery;
import com.techlooper.service.*;
import com.techlooper.util.ExcelUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.InternalNested;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.elasticsearch.index.query.FilterBuilders.boolFilter;
import static org.elasticsearch.index.query.QueryBuilders.filteredQuery;

/**
 * Created by NguyenDangKhoa on 3/19/15.
 */
@Service
public class UserEvaluationServiceImpl implements UserEvaluationService {

    private static final int MINIMUM_NUMBER_OF_JOBS = 10;

    @Resource
    private JobStatisticService jobStatisticService;

    @Resource
    private JobSearchService jobSearchService;

    @Resource
    private ElasticsearchTemplate elasticsearchTemplateUserImport;

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    @Resource(name = "GITHUBTalentSearchQuery")
    private GithubTalentSearchQuery githubTalentSearchQuery;

    @Resource
    private SalaryReviewRepository salaryReviewRepository;

    @Resource
    private JobQueryBuilder jobQueryBuilder;

    @Resource
    private Environment environment;

    @Resource
    private SalaryReviewService salaryReviewService;

    private static final double[] percents = new double[]{10D, 25D, 50D, 75D, 90D};

    public long score(UserImportEntity user, Map<String, Long> totalJobPerSkillMap) {
        long score = 0L;

        // TODO Check for users.noreply.github.com
        if (StringUtils.isNotEmpty(user.getEmail()) && !user.getEmail().contains("missing.com")) {
            score += 10;
        }

        Map<String, Object> profile = (Map<String, Object>) user.getProfiles().get(SocialProvider.GITHUB);
        Integer numberOfRepos = (Integer) profile.get("numberOfRepositories");
        score += numberOfRepos * 10;

        List<String> skills = (List<String>) profile.get("skills");
        for (String skill : skills) {
            score += totalJobPerSkillMap.get(skill.toLowerCase()) != null ? totalJobPerSkillMap.get(skill.toLowerCase()) : 0L;
        }

        return score;
    }

    public double rate(UserImportEntity user, Map<String, Long> totalJobPerSkillMap, Long totalITJobs) {
        long score = 0L;

        Map<String, Object> profile = (Map<String, Object>) user.getProfiles().get(SocialProvider.GITHUB);
        List<String> skills = (List<String>) profile.get("skills");
        for (String skill : skills) {
            score += totalJobPerSkillMap.get(skill.toLowerCase()) != null ? totalJobPerSkillMap.get(skill.toLowerCase()) : 0L;
        }

        if (totalITJobs > 0) {
            double rate = score * 5D / totalITJobs;
            if (rate >= 5D) {
                rate = 5D;
            } else {
                rate = Math.ceil(rate * 2) / 2;
            }

            return rate;
        } else {
            return 0D;
        }
    }

    public Map<String, Integer> rank(UserImportEntity user) {
        Map<String, Integer> resultMap = new HashMap<>();
        Map<String, Object> profile = (Map<String, Object>) user.getProfiles().get(SocialProvider.GITHUB);
        List<String> skills = (List<String>) profile.get("skills");
        for (String skill : skills) {
            List<String> userIds = elasticsearchTemplateUserImport.queryForIds(
                    githubTalentSearchQuery.getSearchBySkillQuery(skill, "score"));
            OptionalInt rank = IntStream.range(0, userIds.size())
                    .filter(index -> userIds.get(index).equals(user.getEmail())).findFirst();
            if (rank.isPresent()) {
                resultMap.put(skill, rank.getAsInt());
            } else {
                resultMap.put(skill, userIds.size());
            }
        }
        List<String> allUserIds = elasticsearchTemplateUserImport.queryForIds(
                githubTalentSearchQuery.sortUser("score"));
        OptionalInt overallRank = IntStream.range(0, allUserIds.size())
                .filter(index -> allUserIds.get(index).equals(user.getEmail())).findFirst();
        if (overallRank.isPresent()) {
            resultMap.put("Overall", overallRank.getAsInt());
        } else {
            resultMap.put("Overall", allUserIds.size());
        }
        return resultMap;
    }

    public Map<String, Long> getSkillMap() {
        Map<String, Long> skillMap = new HashMap<>();
        Aggregations aggregations =
                elasticsearchTemplateUserImport.query(githubTalentSearchQuery.getSkillStatsQuery(), SearchResponse::getAggregations);
        InternalNested agg = (InternalNested) aggregations.getAsMap().get("skill_list");
        StringTerms stringTerms = (StringTerms) agg.getAggregations().getAsMap().get("skill_list");
        stringTerms.getBuckets().stream().forEach(bucket -> skillMap.put(bucket.getKey(), bucket.getDocCount()));
        //TODO : ES cannot index special language like C++ or C#, we assume it as C-family skill and will handle this issue later
        skillMap.put("c++", skillMap.get("c"));
        skillMap.put("c#", skillMap.get("c"));
        skillMap.put("objective-c", skillMap.get("objective"));
        skillMap.put("objective-c++", skillMap.get("objective"));
        return skillMap;
    }

    public Map<String, Long> getTotalNumberOfJobPerSkill() {
        Map<String, Long> totalJobPerSkillMap = new HashMap<>();
        Map<String, Long> skillMap = getSkillMap();
        skillMap.entrySet().stream().forEach(skillEntry ->
                totalJobPerSkillMap.put(skillEntry.getKey(), jobStatisticService.countJobsBySkillWithinPeriod(
                        skillEntry.getKey(), HistogramEnum.TWO_QUARTERS)));
        return totalJobPerSkillMap;
    }

    public void evaluateJobOffer(SalaryReview salaryReview) {
        NativeSearchQueryBuilder queryBuilder = jobQueryBuilder.getVietnamworksJobCountQuery();

        QueryBuilder jobTitleQueryBuilder = jobQueryBuilder.jobTitleQueryBuilder(salaryReview.getJobTitle());
        //FilterBuilder jobLevelFilterBuilder = jobQueryBuilder.getJobLevelsFilterBuilder(salaryReview.getJobLevelIds());
        FilterBuilder jobIndustriesFilterBuilder = jobQueryBuilder.getJobIndustriesFilterBuilder(salaryReview.getJobCategories());
        FilterBuilder approvedDateRangeFilterBuilder = jobQueryBuilder.getRangeFilterBuilder("approvedDate", "now-6M/M", null);
        FilterBuilder salaryMinRangeFilterBuilder = jobQueryBuilder.getRangeFilterBuilder("salaryMin",
                VietnamWorksJobStatisticService.LOWER_BOUND_SALARY_ENTRY_LEVEL, null);
        FilterBuilder salaryMaxRangeFilterBuilder = jobQueryBuilder.getRangeFilterBuilder("salaryMax",
                VietnamWorksJobStatisticService.LOWER_BOUND_SALARY_ENTRY_LEVEL, null);

        queryBuilder.withQuery(filteredQuery(jobTitleQueryBuilder,
                boolFilter().must(approvedDateRangeFilterBuilder)
                        .must(jobIndustriesFilterBuilder)
                                //                .must(jobLevelFilterBuilder)
                        .must(boolFilter().should(salaryMinRangeFilterBuilder).should(salaryMaxRangeFilterBuilder))));

        // Get the list of jobs search result for user percentile rank calculation
        List<JobEntity> jobs = getJobSearchResult(queryBuilder);

        // In order to make our report more accurate, we should add data from generated report in calculation
        List<SalaryReview> salaryReviews = salaryReviewService.searchSalaryReview(
                salaryReview.getJobTitle(), salaryReview.getJobCategories());
        mergeSalaryReviewWithJobList(jobs, salaryReviews);

        // In case total number of jobs search result is less than 10, add more jobs from search by skills
        if (jobs.size() > 0 && jobs.size() < MINIMUM_NUMBER_OF_JOBS && salaryReview.getSkills() != null && !salaryReview.getSkills().isEmpty()) {
            QueryBuilder skillQueryBuilder = jobQueryBuilder.skillQueryBuilder(salaryReview.getSkills());
            queryBuilder.withQuery(filteredQuery(skillQueryBuilder,
                    boolFilter().must(approvedDateRangeFilterBuilder)
                            .must(jobIndustriesFilterBuilder)
                            .must(boolFilter().should(salaryMinRangeFilterBuilder).should(salaryMaxRangeFilterBuilder))));
            List<JobEntity> jobBySkills = jobSearchService.getJobSearchResult(queryBuilder);
            Set<JobEntity> noDuplicatedJobs = new HashSet<>(jobs);
            noDuplicatedJobs.addAll(jobBySkills);
            jobs = new ArrayList<>(noDuplicatedJobs);
        }

        // It's only enabled on test scope for checking whether our calculation is right or wrong
        boolean isExportToExcel = environment.getProperty("salaryEvaluation.exportResultToExcel", Boolean.class) != null ?
                environment.getProperty("salaryEvaluation.exportResultToExcel", Boolean.class) : false;
        if (isExportToExcel) {
            ExcelUtils.exportSalaryReport(jobs);
        }

        SalaryReport salaryReport = transformAggregationsToEvaluationReport(salaryReview, jobs);
        salaryReview.setSalaryReport(salaryReport);
        salaryReview.setCreatedDateTime(new Date().getTime());

        // Save user salary information should happen only in production environment
        boolean allowToSave = environment.getProperty("salaryEvaluation.allowToSave", Boolean.class) != null ?
                environment.getProperty("salaryEvaluation.allowToSave", Boolean.class) : false;
        if (allowToSave) {
            salaryReviewRepository.save(salaryReview);
        }

        // get top 3 higher salary jobs
        List<TopPaidJob> topPaidJobs = findTopPaidJob(jobSearchService.getHigherSalaryJobs(salaryReview), salaryReview.getNetSalary());
        salaryReview.setTopPaidJobs(topPaidJobs);
    }

    private SalaryReport transformAggregationsToEvaluationReport(
            SalaryReview salaryReview, List<JobEntity> jobs) {
        SalaryReport salaryReport = new SalaryReport();
        salaryReport.setNetSalary(salaryReview.getNetSalary());

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

        return salaryReport;
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

    private double[] extractSalariesFromJob(List<JobEntity> jobs) {
        return jobs.stream().mapToDouble(job ->
                jobSearchService.getAverageSalary(job.getSalaryMin(), job.getSalaryMax())).toArray();
    }

    //Percentile Ranking Reference : http://www.regentsprep.org/regents/math/algebra/AD6/quartiles.htm
    private double calculatePercentRank(double[] salaries, double evaluatedSalary) {
        long countSalaryBelow = Arrays.stream(salaries).filter(salary -> salary < evaluatedSalary).count();
        double percentRank = (countSalaryBelow * 1.0) / salaries.length * 100;
        if (percentRank == 0D) {
            return 1D;
        } else if (percentRank == 100D) {
            return 99D;
        }
        return percentRank;
    }


    private double calculatePercentPosition(SalaryReport salaryReport) {
        //Remove duplicated percentiles if any
        List<SalaryRange> noDuplicatedSalaryRanges = salaryReport.getSalaryRanges().stream().distinct().collect(Collectors.toList());
        if (noDuplicatedSalaryRanges.size() >= 2) {
            SalaryRange basedPercentile;
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

    private List<TopPaidJob> findTopPaidJob(List<JobEntity> higherSalaryJobs, Integer netSalary) {
        List<TopPaidJob> topPaidJobs = new ArrayList<>();
        for (JobEntity jobEntity : higherSalaryJobs) {
            double addedPercent = (jobSearchService.getAverageSalary(jobEntity.getSalaryMin(), jobEntity.getSalaryMax()) - netSalary) /
                    netSalary * 100;
            topPaidJobs.add(new TopPaidJob(jobEntity.getId(), jobEntity.getJobTitle(), jobEntity.getCompanyDesc(), Math.ceil(addedPercent)));
        }
        return topPaidJobs;
    }

    private void mergeSalaryReviewWithJobList(List<JobEntity> jobs, List<SalaryReview> salaryReviews) {
        for(SalaryReview salaryReview : salaryReviews) {
            JobEntity jobEntity = new JobEntity();
            jobEntity.setId(salaryReview.getCreatedDateTime().toString());
            jobEntity.setJobTitle(salaryReview.getJobTitle());
            jobEntity.setSalaryMin(salaryReview.getNetSalary().longValue());
            jobEntity.setSalaryMax(salaryReview.getNetSalary().longValue());
            jobs.add(jobEntity);
        }
    }

    public void deleteSalaryReview(SalaryReview salaryReview) {
        salaryReviewRepository.delete(salaryReview.getCreatedDateTime());
    }

    public boolean saveSalaryReviewSurvey(SalaryReviewSurvey salaryReviewSurvey) {
        SalaryReview salaryReview = salaryReviewRepository.findOne(salaryReviewSurvey.getSalaryReviewId());
        if (salaryReview != null) {
            salaryReview.setSalaryReviewSurvey(salaryReviewSurvey);
            salaryReviewRepository.save(salaryReview);
            return true;
        }
        return false;
    }

}
