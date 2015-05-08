package com.techlooper.service.impl;

import com.techlooper.entity.JobEntity;
import com.techlooper.entity.SalaryReview;
import com.techlooper.entity.userimport.UserImportEntity;
import com.techlooper.model.HistogramEnum;
import com.techlooper.model.SalaryRange;
import com.techlooper.model.SalaryReport;
import com.techlooper.model.SocialProvider;
import com.techlooper.repository.elasticsearch.SalaryReviewRepository;
import com.techlooper.repository.talentsearch.query.GithubTalentSearchQuery;
import com.techlooper.service.JobQueryBuilder;
import com.techlooper.service.JobStatisticService;
import com.techlooper.service.UserEvaluationService;
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

    public SalaryReport evaluateJobOffer(SalaryReview salaryReview) {
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

        // In case total number of jobs search result is less than 10, add more jobs from search by skills
        if (jobs.size() < MINIMUM_NUMBER_OF_JOBS && salaryReview.getSkills() != null && !salaryReview.getSkills().isEmpty()) {
            QueryBuilder skillQueryBuilder = jobQueryBuilder.skillQueryBuilder(salaryReview.getSkills());
            queryBuilder.withQuery(filteredQuery(skillQueryBuilder,
                    boolFilter().must(approvedDateRangeFilterBuilder)
                            .must(jobIndustriesFilterBuilder)
                            .must(boolFilter().should(salaryMinRangeFilterBuilder).should(salaryMaxRangeFilterBuilder))));
            List<JobEntity> jobBySkills = getJobSearchResult(queryBuilder);
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
        salaryReviewRepository.save(salaryReview);
        return salaryReport;
    }

    private SalaryReport transformAggregationsToEvaluationReport(
            SalaryReview salaryReview, List<JobEntity> jobs) {
        SalaryReport salaryReport = new SalaryReport();
        salaryReport.setNetSalary(salaryReview.getNetSalary());

        // Only generate percentile report if total number of jobs is greater than 10
        if (jobs.size() >= MINIMUM_NUMBER_OF_JOBS) {
            double[] salaries = extractSalariesFromJob(jobs);
            List<SalaryRange> salaryRanges = new ArrayList<>();
            Percentile percentile = new Percentile();
            for (double percent : percents) {
                salaryRanges.add(new SalaryRange(percent, Math.floor(percentile.evaluate(salaries, percent))));
            }
            salaryReport.setSalaryRanges(salaryRanges);

            // Calculate salary percentile rank for user based on list of salary percentiles from above result
            double percentRank = calculatePercentRank(salaries, salaryReview.getNetSalary().doubleValue());
            salaryReport.setPercentRank(Math.floor(percentRank));
        } else {
            salaryReport.setPercentRank(Double.NaN);
        }

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
        return jobs.stream().mapToDouble(job -> {
            if (job.getSalaryMin() == 0) {
                return job.getSalaryMax() * 0.75D;
            } else if (job.getSalaryMax() == 0) {
                return job.getSalaryMin() * 1.25D;
            } else {
                return (job.getSalaryMin() + job.getSalaryMax()) / 2;
            }
        }).toArray();
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

}
