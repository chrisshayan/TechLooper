package com.techlooper.service.impl;

import com.techlooper.entity.JobOfferInfo;
import com.techlooper.entity.userimport.UserImportEntity;
import com.techlooper.model.HistogramEnum;
import com.techlooper.model.JobOfferEvaluation;
import com.techlooper.model.JobOfferRange;
import com.techlooper.model.SocialProvider;
import com.techlooper.repository.elasticsearch.JobOfferInfoRepository;
import com.techlooper.repository.talentsearch.query.GithubTalentSearchQuery;
import com.techlooper.service.JobQueryBuilder;
import com.techlooper.service.JobStatisticService;
import com.techlooper.service.UserEvaluationService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.InternalNested;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.metrics.percentiles.InternalPercentiles;
import org.elasticsearch.search.aggregations.metrics.percentiles.PercentilesBuilder;
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

    @Resource
    private JobStatisticService jobStatisticService;

    @Resource
    private ElasticsearchTemplate elasticsearchTemplateUserImport;

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    @Resource(name = "GITHUBTalentSearchQuery")
    private GithubTalentSearchQuery githubTalentSearchQuery;

    @Resource
    private JobOfferInfoRepository jobOfferInfoRepository;

    @Resource
    private JobQueryBuilder jobQueryBuilder;

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

    public JobOfferEvaluation evaluateJobOffer(JobOfferInfo jobOfferInfo) {
        NativeSearchQueryBuilder queryBuilder = jobQueryBuilder.getVietnamworksJobCountQuery();

        QueryBuilder jobTitleQueryBuilder = jobQueryBuilder.jobTitleQueryBuilder(jobOfferInfo.getJobTitle());
        FilterBuilder jobLevelFilterBuilder = jobQueryBuilder.getJobLevelsFilterBuilder(jobOfferInfo.getJobLevelIds());
        FilterBuilder jobIndustriesFilterBuilder = jobQueryBuilder.getJobIndustriesFilterBuilder(jobOfferInfo.getJobCategories());
        FilterBuilder approvedDateRangeFilterBuilder = jobQueryBuilder.getRangeFilterBuilder("approvedDate", "now-6M/M", null);
        FilterBuilder salaryMinRangeFilterBuilder = jobQueryBuilder.getRangeFilterBuilder("salaryMin",
                VietnamWorksJobStatisticService.LOWER_BOUND_SALARY_ENTRY_LEVEL, null);
        FilterBuilder salaryMaxRangeFilterBuilder = jobQueryBuilder.getRangeFilterBuilder("salaryMax",
                VietnamWorksJobStatisticService.LOWER_BOUND_SALARY_ENTRY_LEVEL, null);

        PercentilesBuilder salaryPercentileAggregationBuilder = jobQueryBuilder.salaryPercentileAggregationBuilder(percents);

        queryBuilder.withQuery(filteredQuery(jobTitleQueryBuilder,
                boolFilter().must(approvedDateRangeFilterBuilder)
                        .must(jobIndustriesFilterBuilder)
                        .must(jobLevelFilterBuilder)
                        .must(boolFilter().should(salaryMinRangeFilterBuilder).should(salaryMaxRangeFilterBuilder))));

        queryBuilder.addAggregation(salaryPercentileAggregationBuilder);

        Aggregations aggregations = elasticsearchTemplate.query(queryBuilder.build(), SearchResponse::getAggregations);

        jobOfferInfoRepository.save(jobOfferInfo);
        return transformAggregationsToEvaluationReport(jobOfferInfo, aggregations);
    }

    private JobOfferEvaluation transformAggregationsToEvaluationReport(JobOfferInfo jobOfferInfo, Aggregations aggregations) {
        JobOfferEvaluation jobOfferEvaluation = new JobOfferEvaluation();
        jobOfferEvaluation.setTotalPay(jobOfferInfo.getNetSalary());


        InternalPercentiles salaryPercentiles = (InternalPercentiles) aggregations.get("salary_percentile");
        List<JobOfferRange> jobOfferRanges = new ArrayList<>();
        for (double percent : percents) {
            jobOfferRanges.add(new JobOfferRange(percent, salaryPercentiles.value(percent)));
        }
        jobOfferEvaluation.setJobOfferRanges(jobOfferRanges);

        // Calculate salary percentile rank for user based on list of salary percentiles from above result
        JobOfferRange medianPercentile = jobOfferRanges.get(percents.length / 2);
        double percentileRank = jobOfferEvaluation.getTotalPay() * medianPercentile.getPercent() / medianPercentile.getPercentile();
        jobOfferEvaluation.setPercentRank(percentileRank);

        return jobOfferEvaluation;
    }

}
