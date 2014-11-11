package com.techlooper.service.impl;

import com.techlooper.model.*;
import com.techlooper.service.JobQueryBuilder;
import com.techlooper.service.JobStatisticService;
import com.techlooper.util.EncryptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.MatchQueryBuilder.Operator;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.InternalFilter;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static org.elasticsearch.index.query.QueryBuilders.filteredQuery;
import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

/**
 * Created by chrisshayan on 7/14/14.
 */
@Service
public class VietnamWorksJobStatisticService implements JobStatisticService {

    private static final String[] SEARCH_JOB_FIELDS = new String[]{"jobTitle", "jobDescription", "skillExperience"};

    private static final String ES_VIETNAMWORKS_INDEX = "vietnamworks";

    @Resource
    private TechnicalSkillEnumMap technicalSkillEnumMap;

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    @Resource
    private JobQueryBuilder jobQueryBuilder;

    public Long countPhpJobs() {
        return count(TechnicalTermEnum.PHP);
    }

    public Long countJavaJobs() {
        return count(TechnicalTermEnum.JAVA);
    }

    public Long countDotNetJobs() {
        return count(TechnicalTermEnum.DOTNET);
    }

    public Long countProjectManagerJobs() {
        return count(TechnicalTermEnum.PROJECT_MANAGER);
    }

    public Long countBAJobs() {
        return count(TechnicalTermEnum.BA);
    }

    public Long countQAJobs() {
        return count(TechnicalTermEnum.QA);
    }

    public Long countDBAJobs() {
        return count(TechnicalTermEnum.QA);
    }

    public Long countPythonJobs() {
        return count(TechnicalTermEnum.PYTHON);
    }

    public Long countRubyJobs() {
        return count(TechnicalTermEnum.RUBY);
    }

    /**
     * Counts the matching jobs to relevant {@code TechnicalTermEnum}
     *
     * @param technicalTermEnum a {@code TechnicalTermEnum} to determine which technology search
     *                          must happen.
     * @return a {@code Long} that represents number of matching jobs.
     */
    public Long count(final TechnicalTermEnum technicalTermEnum) {
        final SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(multiMatchQuery(technicalTermEnum, SEARCH_JOB_FIELDS).operator(Operator.AND))
                .withIndices(ES_VIETNAMWORKS_INDEX).withSearchType(SearchType.COUNT).build();
        return elasticsearchTemplate.count(searchQuery);
    }

    public Long countTechnicalJobs() {
        return Stream.of(TechnicalTermEnum.values()).mapToLong(this::count).sum();
    }

    /**
     * Counting number of jobs by technical term and its skill
     *
     * @param technicalTermEnum
     * @param skill
     * @param approvedDate
     * @return number of jobs
     * @see com.techlooper.model.TechnicalTermEnum
     */
    public Long countTechnicalJobsBySkill(TechnicalTermEnum technicalTermEnum, String skill, LocalDate approvedDate) {
        final Optional term = Optional.ofNullable(technicalTermEnum);

        if (term.isPresent()) {
            final String searchCriteria = StringUtils.join(Arrays.asList(term.get(), skill), ' ').trim();
            final QueryBuilder skillSearchQuery = multiMatchQuery(searchCriteria, SEARCH_JOB_FIELDS)
                    .type(MultiMatchQueryBuilder.Type.CROSS_FIELDS)
                    .operator(Operator.AND);
            final FilterBuilder periodQueryFilter = FilterBuilders.rangeFilter("approvedDate").lte(approvedDate.toString());

            final SearchQuery searchQuery = new NativeSearchQueryBuilder()
                    .withQuery(filteredQuery(skillSearchQuery, periodQueryFilter))
                    .withIndices(ES_VIETNAMWORKS_INDEX)
                    .withSearchType(SearchType.COUNT).build();
            return elasticsearchTemplate.count(searchQuery);
        } else {
            return 0L;
        }
    }

    public SkillStatisticResponse countJobsBySkill(TechnicalTermEnum term, PeriodEnum period) {
        NativeSearchQueryBuilder queryBuilder = jobQueryBuilder.getVietnamworksJobQuery();
        queryBuilder.withQuery(jobQueryBuilder.getTechnicalTermsQuery()).withSearchType(SearchType.COUNT);// all technical terms query

        AggregationBuilder technicalTermAggregation = jobQueryBuilder.getTechnicalTermAggregation(term);
        jobQueryBuilder.toSkillAggregations(technicalSkillEnumMap.skillOf(term)).stream()
                .forEach(aggs -> aggs.stream().forEach(technicalTermAggregation::subAggregation));

        queryBuilder.addAggregation(technicalTermAggregation);// technical term aggregation

        final SkillStatisticResponse.Builder skillStatisticResponse = new SkillStatisticResponse.Builder().withJobTerm(term);
        Aggregations aggregations = elasticsearchTemplate.query(queryBuilder.build(), response -> {
            skillStatisticResponse.withTotalTechnicalJobs(response.getHits().getTotalHits());
            return response.getAggregations();
        });

        // term aggregation
        InternalFilter termAggregation = aggregations.get(term.name());
        skillStatisticResponse.withCount(termAggregation.getDocCount());

        final List<SkillStatisticItem> jobSkills = new LinkedList<>();
        termAggregation.getAggregations().asList().stream().map(agg -> (InternalFilter) agg)
                .sorted((bucket1, bucket2) -> bucket1.getName().compareTo(bucket2.getName()))
                .collect(Collectors.groupingBy(bucket -> bucket.getName().split("-")[0], mapping(InternalFilter::getDocCount, toList())))
                .forEach((skillName, docCounts) -> {
                    jobSkills.add(new SkillStatisticItem.Builder()
                            .withSkill(EncryptionUtils.decodeHexa(skillName))
                            .withHistogramData(docCounts)
                            .withCurrentCount(countTechnicalJobsBySkill(term, EncryptionUtils.decodeHexa(skillName), LocalDate.now()))
                            .withPreviousCount(countTechnicalJobsBySkill(term, EncryptionUtils.decodeHexa(skillName),
                                    LocalDate.now().minusDays(period.numberOfDays())))
                            .build());
                });

        return skillStatisticResponse.withJobSkills(jobSkills).build();
    }
}
