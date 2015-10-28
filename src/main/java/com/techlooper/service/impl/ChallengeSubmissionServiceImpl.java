package com.techlooper.service.impl;

import com.techlooper.entity.ChallengeRegistrantDto;
import com.techlooper.entity.ChallengeRegistrantEntity;
import com.techlooper.entity.ChallengeSubmissionEntity;
import com.techlooper.entity.ChallengeSubmissionEntity.ChallengeSubmissionEntityBuilder;
import com.techlooper.model.ChallengePhaseEnum;
import com.techlooper.model.ChallengeSubmissionDto;
import com.techlooper.model.ChallengeSubmissionPhaseItem;
import com.techlooper.repository.elasticsearch.ChallengeSubmissionRepository;
import com.techlooper.service.ChallengeService;
import com.techlooper.service.ChallengeSubmissionService;
import com.techlooper.util.DateTimeUtils;
import org.dozer.Mapper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.joda.time.DateTime;
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

/**
 * Created by phuonghqh on 10/9/15.
 */
@Service
public class ChallengeSubmissionServiceImpl implements ChallengeSubmissionService {

    @Resource
    private ChallengeService challengeService;

    @Resource
    private Mapper dozerMapper;

    @Resource
    private ChallengeSubmissionRepository challengeSubmissionRepository;

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    private final List<ChallengePhaseEnum> CHALLENGE_PHASES = Arrays.asList(REGISTRATION, IDEA, UIUX, PROTOTYPE, FINAL);

    public ChallengeSubmissionEntity submitMyResult(ChallengeSubmissionDto challengeSubmissionDto) {
        ChallengeRegistrantEntity registrant = challengeService.findRegistrantByChallengeIdAndEmail(
                challengeSubmissionDto.getChallengeId(), challengeSubmissionDto.getRegistrantEmail());
        if (registrant == null) {
            registrant = challengeService.joinChallengeEntity(dozerMapper.map(challengeSubmissionDto, ChallengeRegistrantDto.class));
        }
        ChallengePhaseEnum activePhase = registrant.getActivePhase() == null ? ChallengePhaseEnum.REGISTRATION : registrant.getActivePhase();

        ChallengeSubmissionEntity challengeSubmissionEntity = dozerMapper.map(challengeSubmissionDto, ChallengeSubmissionEntity.class);
        ChallengeSubmissionEntityBuilder.challengeSubmissionEntity(challengeSubmissionEntity)
                .withChallengeSubmissionId(DateTime.now().getMillis())
                .withRegistrantId(registrant.getRegistrantId())
                .withRegistrantName(String.format("%s %s", registrant.getRegistrantFirstName(), registrant.getRegistrantLastName()))
                .withSubmissionDateTime(DateTimeUtils.currentDate())
                .withSubmissionPhase(activePhase);

        return challengeSubmissionRepository.save(challengeSubmissionEntity);
    }

    @Override
    public Map<ChallengePhaseEnum, ChallengeSubmissionPhaseItem> countNumberOfSubmissionsByPhase(Long challengeId) {
        Map<ChallengePhaseEnum, ChallengeSubmissionPhaseItem> numberOfSubmissionsByPhase = new HashMap<>();

        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withIndices("techlooper")
            .withTypes("challengeSubmission").withSearchType(SearchType.COUNT);
        searchQueryBuilder.withQuery(QueryBuilders.termQuery("challengeId", challengeId));
        searchQueryBuilder.addAggregation(AggregationBuilders.terms("sumOfSubmissions").field("submissionPhase"));
        Aggregations aggregations = elasticsearchTemplate.query(searchQueryBuilder.build(), SearchResponse::getAggregations);

        Terms terms = aggregations.get("sumOfSubmissions");
        for (ChallengePhaseEnum phaseEnum : CHALLENGE_PHASES) {
            Terms.Bucket bucket = terms.getBucketByKey(phaseEnum.getValue());
            if (bucket != null) {
                numberOfSubmissionsByPhase.put(phaseEnum, new ChallengeSubmissionPhaseItem(phaseEnum, bucket.getDocCount()));
            } else {
                bucket = terms.getBucketByKey(phaseEnum.getValue().toLowerCase());
                if (bucket != null) {
                    numberOfSubmissionsByPhase.put(phaseEnum, new ChallengeSubmissionPhaseItem(phaseEnum, bucket.getDocCount()));
                } else {
                    numberOfSubmissionsByPhase.put(phaseEnum, new ChallengeSubmissionPhaseItem(phaseEnum, 0L));
                }
            }
        }
        return numberOfSubmissionsByPhase;
    }
}
