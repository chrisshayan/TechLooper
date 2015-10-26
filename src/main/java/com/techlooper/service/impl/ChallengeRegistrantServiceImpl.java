package com.techlooper.service.impl;

import com.techlooper.model.ChallengePhaseEnum;
import com.techlooper.model.ChallengeRegistrantPhaseItem;
import com.techlooper.service.ChallengeRegistrantService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.techlooper.model.ChallengePhaseEnum.*;

@Service
public class ChallengeRegistrantServiceImpl implements ChallengeRegistrantService {

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    private final List<ChallengePhaseEnum> CHALLENGE_PHASES = Arrays.asList(IDEA, UIUX, PROTOTYPE, FINAL);

    @Override
    public Map<ChallengePhaseEnum, ChallengeRegistrantPhaseItem> countNumberOfRegistrantsByPhase(Long challengeId) {
        Map<ChallengePhaseEnum, ChallengeRegistrantPhaseItem> numberOfRegistrantsByPhase = new HashMap<>();

        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withIndices("techlooper")
                .withTypes("challengeRegistrant").withSearchType(SearchType.COUNT);
        searchQueryBuilder.withQuery(QueryBuilders.termQuery("challengeId", challengeId));

        Long numberOfRegistrants = elasticsearchTemplate.count(searchQueryBuilder.build());
        numberOfRegistrantsByPhase.put(REGISTRATION, new ChallengeRegistrantPhaseItem(REGISTRATION, numberOfRegistrants));

        searchQueryBuilder.addAggregation(AggregationBuilders.terms("sumOfRegistrants").field("activePhase"));
        Aggregations aggregations = elasticsearchTemplate.query(searchQueryBuilder.build(), SearchResponse::getAggregations);
        Terms terms = aggregations.get("sumOfRegistrants");
        for (ChallengePhaseEnum phaseEnum : CHALLENGE_PHASES) {
            Terms.Bucket bucket = terms.getBucketByKey(phaseEnum.getValue());
            if (bucket != null) {
                numberOfRegistrantsByPhase.put(phaseEnum, new ChallengeRegistrantPhaseItem(phaseEnum, bucket.getDocCount()));
            } else {
                bucket = terms.getBucketByKey(phaseEnum.getValue().toLowerCase());
                if (bucket != null) {
                    numberOfRegistrantsByPhase.put(phaseEnum, new ChallengeRegistrantPhaseItem(phaseEnum, bucket.getDocCount()));
                } else {
                    numberOfRegistrantsByPhase.put(phaseEnum, new ChallengeRegistrantPhaseItem(phaseEnum, 0L));
                }
            }
        }
        return numberOfRegistrantsByPhase;
    }
}
