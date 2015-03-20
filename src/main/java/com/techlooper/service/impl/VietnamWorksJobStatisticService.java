package com.techlooper.service.impl;

import com.techlooper.model.*;
import com.techlooper.service.JobQueryBuilder;
import com.techlooper.service.JobStatisticService;
import com.techlooper.util.EncryptionUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.InternalFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by chrisshayan on 7/14/14.
 */
@Service
public class VietnamWorksJobStatisticService implements JobStatisticService {

    private static final String ALL_TERMS = "allTerms";

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    @Resource
    private JobQueryBuilder jobQueryBuilder;

    @Value("${elasticsearch.index.name}")
    private String elasticSearchIndexName;

    public Long count(final TechnicalTerm term) {
        final SearchQuery searchQuery = jobQueryBuilder.getVietnamworksJobCountQuery()
                .withFilter(jobQueryBuilder.getTechnicalTermQueryNotExpired(term))
                .build();
        return elasticsearchTemplate.count(searchQuery);
    }

    public SkillStatisticResponse countJobsBySkill(TechnicalTerm term, HistogramEnum... histogramEnums) {
        NativeSearchQueryBuilder queryBuilder = jobQueryBuilder.getVietnamworksJobCountQuery();
        queryBuilder.withFilter(jobQueryBuilder.getTechnicalTermsQuery());// all technical terms query

        queryBuilder.addAggregation(getTermsAggregationNotExpired(term));// technical terms agg which has not expired

        AggregationBuilder technicalTermAggregation = jobQueryBuilder.getTechnicalTermAggregation(term);// technical term agg including expired jobs
        getAggregationsSkillNotExpired(term, histogramEnums).stream()
                .forEach(aggs -> aggs.stream().forEach(technicalTermAggregation::subAggregation));
        queryBuilder.addAggregation(technicalTermAggregation);

        return toSkillStatisticResponse(term, elasticsearchTemplate.query(queryBuilder.build(), SearchResponse::getAggregations));
    }

    @Override
    public Long countJobsBySkillWithinPeriod(String skill, HistogramEnum period) {
        final SearchQuery searchQuery = jobQueryBuilder.getVietnamworksJobCountQuery()
                .withFilter(jobQueryBuilder.getTechnicalTermQueryAvailableWithinPeriod(skill, period))
                .build();
        return elasticsearchTemplate.count(searchQuery);
    }

    /**
     * @param term         See more {@link com.techlooper.model.TechnicalTerm}
     * @param aggregations See more {@link org.elasticsearch.search.aggregations.Aggregations}
     * @return Returns an instance of {@link com.techlooper.model.SkillStatisticResponse} which is having detail information for each technical term.
     */
    private SkillStatisticResponse toSkillStatisticResponse(TechnicalTerm term, Aggregations aggregations) {
        final SkillStatisticResponse.Builder skillStatisticResponse =
                new SkillStatisticResponse.Builder().withLabel(term.getLabel());
        InternalFilter allTermsResponse = aggregations.get(ALL_TERMS);
        skillStatisticResponse.withTotalTechnicalJobs(allTermsResponse.getDocCount());
        skillStatisticResponse.withCount(((InternalFilter) allTermsResponse.getAggregations().get(term.getKey())).getDocCount());

        InternalFilter termAggregation = aggregations.get(term.getKey());
        Map<String, List<Long>> skillHistogramsMap = termAggregation.getAggregations().asList().stream().map(agg -> (InternalFilter) agg)
                .sorted((bucket1, bucket2) -> bucket1.getName().compareTo(bucket2.getName()))
                .collect(Collectors.groupingBy(bucket -> bucket.getName().substring(0, bucket.getName().lastIndexOf("-")),
                        Collectors.mapping(InternalFilter::getDocCount, Collectors.toList())));

        List<SkillStatistic> skillStatistics = getSkillStatisticsByName(skillHistogramsMap);
        skillStatistics.stream().forEach(skillStat -> {
            Skill skill = term.getSkillByName(skillStat.getSkillName());
            skillStat.setLogoUrl(skill.getLogoUrl());
            skillStat.setWebSite(skill.getWebSite());
            skillStat.setUsefulLinks(skill.getUsefulLinks());
        });

        return skillStatisticResponse.withSkills(skillStatistics)
                .withLogoUrl(term.getLogoUrl())
                .withWebSite(term.getWebSite())
                .withUsefulLinks(term.getUsefulLinks())
                .build();
    }

    /**
     * Loads data for each skill
     *
     * @param skillHistogramsMap key is skillName like spring and the value list of duration like 7 days or 30 days
     * @return An instance of {@link com.techlooper.model.SkillStatistic}
     */
    private List<SkillStatistic> getSkillStatisticsByName(Map<String, List<Long>> skillHistogramsMap) {
        Map<String, SkillStatistic.Builder> skillStatisticMap = new HashMap<>();
        skillHistogramsMap.forEach((bucketName, docCounts) -> {
            String[] skillNameParts = bucketName.split("-");
            String readableSkillName = EncryptionUtils.decodeHexa(skillNameParts[0]);
            SkillStatistic.Builder skill = Optional.ofNullable(skillStatisticMap.get(readableSkillName)).orElseGet(SkillStatistic.Builder::new);
            skillStatisticMap.put(readableSkillName, skill.withSkillName(readableSkillName));
            skill.withHistogram(new Histogram.Builder().withName(HistogramEnum.valueOf(skillNameParts[1])).withValues(docCounts).build());
        });
        return skillStatisticMap.keySet().stream().map(key -> skillStatisticMap.get(key).build()).collect(Collectors.toList());
    }

    /**
     * Builds a filter to use on Elastic Search
     *
     * @param term See more at {@link com.techlooper.model.TechnicalTerm}
     * @return The builder to be used for filtering the data on ES
     */
    private FilterAggregationBuilder getTermsAggregationNotExpired(TechnicalTerm term) {
        FilterAggregationBuilder aggregation = AggregationBuilders.filter(ALL_TERMS).filter(jobQueryBuilder.getTechnicalTermsQueryNotExpired());
        aggregation.subAggregation(jobQueryBuilder.getTechnicalTermAggregation(term));
        return aggregation;
    }

    /**
     * @param term           See more at {@link com.techlooper.model.TechnicalTerm}
     * @param histogramEnums See more at {@link com.techlooper.model.HistogramEnum}
     * @return List of filters.
     */
    private List<List<FilterAggregationBuilder>> getAggregationsSkillNotExpired(TechnicalTerm term, HistogramEnum... histogramEnums) {
        List<List<FilterAggregationBuilder>> list = new ArrayList<>();
        Arrays.stream(histogramEnums)
                .forEach(histogramEnum -> list.addAll(jobQueryBuilder.toSkillAggregations(term.getSkills(), histogramEnum)));
        return list;
    }
}
