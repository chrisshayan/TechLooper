package com.techlooper.service.impl;

import com.techlooper.entity.ChallengeEntity;
import com.techlooper.entity.ChallengeRegistrantEntity;
import com.techlooper.entity.ChallengeSubmissionEntity;
import com.techlooper.entity.ChallengeSubmissionEntity.ChallengeSubmissionEntityBuilder;
import com.techlooper.model.*;
import com.techlooper.repository.elasticsearch.ChallengeSubmissionRepository;
import com.techlooper.service.ChallengeRegistrantService;
import com.techlooper.service.ChallengeService;
import com.techlooper.service.ChallengeSubmissionService;
import com.techlooper.service.EmailService;
import com.techlooper.util.DataUtils;
import com.techlooper.util.DateTimeUtils;
import org.dozer.Mapper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.joda.time.DateTime;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.techlooper.model.ChallengePhaseEnum.*;
import static com.techlooper.util.DateTimeUtils.yesterdayDate;
import static org.elasticsearch.index.query.FilterBuilders.*;
import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.elasticsearch.search.sort.SortBuilders.fieldSort;

/**
 * Created by phuonghqh on 10/9/15.
 */
@Service
public class ChallengeSubmissionServiceImpl implements ChallengeSubmissionService {

    @Resource
    private ChallengeService challengeService;

    @Resource
    private ChallengeRegistrantService challengeRegistrantService;

    @Resource
    private Mapper dozerMapper;

    @Resource
    private ChallengeSubmissionRepository challengeSubmissionRepository;

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    private final List<ChallengePhaseEnum> CHALLENGE_PHASES = Arrays.asList(REGISTRATION, IDEA, UIUX, PROTOTYPE, FINAL);

    @Resource
    private MimeMessage confirmUserSubmissionMailMessage;

    @Value("${web.baseUrl}")
    private String webBaseUrl;

    @Resource
    private EmailService emailService;

    public ChallengeSubmissionEntity submitMyResult(ChallengeSubmissionDto challengeSubmissionDto) {
        final Long challengeId = challengeSubmissionDto.getChallengeId();
        final String registrantEmail = challengeSubmissionDto.getRegistrantEmail();
        ChallengeEntity challenge = challengeService.findChallengeById(challengeId);
        ChallengeRegistrantEntity registrant = challengeRegistrantService.findRegistrantByChallengeIdAndEmail(challengeId, registrantEmail);
        final Long registrantId = registrant.getRegistrantId();
        ChallengePhaseEnum activePhase = registrant.getActivePhase() == null ? ChallengePhaseEnum.REGISTRATION : registrant.getActivePhase();

        ChallengeSubmissionEntity challengeSubmissionEntity = dozerMapper.map(challengeSubmissionDto, ChallengeSubmissionEntity.class);
        ChallengeSubmissionEntityBuilder.challengeSubmissionEntity(challengeSubmissionEntity)
                .withChallengeSubmissionId(DateTime.now().getMillis())
                .withRegistrantId(registrantId)
                .withRegistrantName(String.format("%s %s", registrant.getRegistrantFirstName(), registrant.getRegistrantLastName()))
                .withSubmissionDateTime(DateTimeUtils.currentDate())
                .withSubmissionPhase(activePhase)
                .withIsRead(Boolean.FALSE);

        sendConfirmationEmailToRegistrant(challenge, registrant, challengeSubmissionEntity);
        return challengeSubmissionRepository.save(challengeSubmissionEntity);
    }

    private void sendConfirmationEmailToRegistrant(ChallengeEntity challenge, ChallengeRegistrantEntity registrant,
                                                   ChallengeSubmissionEntity challengeSubmissionEntity) {
        String recipientAddresses = registrant.getRegistrantEmail();
        List<String> subjectVariableValues = Arrays.asList(challenge.getChallengeName());

        EmailRequestModel emailRequestModel = new EmailRequestModel.Builder()
                .withTemplateName(EmailTemplateNameEnum.CHALLENGE_CONFIRM_USER_SUBMISSION.name())
                .withLanguage(registrant.getLang())
                .withTemplateModel(buildConfirmSubmissionEmailTemplateModel(challenge, challengeSubmissionEntity))
                .withMailMessage(confirmUserSubmissionMailMessage)
                .withRecipientAddresses(recipientAddresses)
                .withSubjectVariableValues(subjectVariableValues).build();
        emailService.sendMail(emailRequestModel);
    }

    private Map<String, Object> buildConfirmSubmissionEmailTemplateModel(
            ChallengeEntity challenge, ChallengeSubmissionEntity challengeSubmissionEntity) {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("webBaseUrl", webBaseUrl);
        templateModel.put("submissionUrl", challengeSubmissionEntity.getSubmissionURL());
        templateModel.put("currentPhase", challengeSubmissionEntity.getSubmissionPhase());
        templateModel.put("challengeId", String.valueOf(challenge.getChallengeId()));
        templateModel.put("challengeAlias", challenge.getChallengeName().replaceAll("\\W", "-"));
        templateModel.put("challengeName", challenge.getChallengeName());
        return templateModel;
    }

    @Override
    public Map<ChallengePhaseEnum, ChallengeSubmissionPhaseItem> countNumberOfSubmissionsByPhase(
            Long challengeId, Boolean isRead) {
        Map<ChallengePhaseEnum, ChallengeSubmissionPhaseItem> numberOfSubmissionsByPhase = new HashMap<>();
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withIndices("techlooper")
                .withTypes("challengeSubmission").withSearchType(SearchType.COUNT);
        searchQueryBuilder.withQuery(termQuery("challengeId", challengeId));

        AbstractAggregationBuilder aggregationBuilder = AggregationBuilders.terms("sumOfSubmissions").field("submissionPhase");
        if (isRead != null) {
            aggregationBuilder = AggregationBuilders.filter("filterByReadOrUnread").filter(termFilter("isRead", isRead))
                    .subAggregation(AggregationBuilders.terms("sumOfSubmissions").field("submissionPhase"));
        }

        searchQueryBuilder.addAggregation(aggregationBuilder);
        Aggregations aggregations = elasticsearchTemplate.query(searchQueryBuilder.build(), SearchResponse::getAggregations);

        Terms terms = aggregations.get("sumOfSubmissions");
        if (isRead != null) {
            Filter filter = aggregations.get("filterByReadOrUnread");
            terms = filter.getAggregations().get("sumOfSubmissions");
        }

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

            // in case of submission phases is empty (previous releases), we should count them as REGISTRATION phase
            if (phaseEnum == REGISTRATION && isRead == null) {
                BoolFilterBuilder boolFilterBuilder = boolFilter();
                boolFilterBuilder.must(termFilter("challengeId", challengeId));
                boolFilterBuilder.must(missingFilter("submissionPhase"));
                searchQueryBuilder.withQuery(filteredQuery(matchAllQuery(), boolFilterBuilder));
                long registrationSubmissionPhase = elasticsearchTemplate.count(searchQueryBuilder.build());
                ChallengeSubmissionPhaseItem registrationPhaseItem = numberOfSubmissionsByPhase.get(phaseEnum);
                if (registrationPhaseItem != null) {
                    registrationPhaseItem.setSubmission(registrationPhaseItem.getSubmission() + registrationSubmissionPhase);
                    numberOfSubmissionsByPhase.put(phaseEnum, registrationPhaseItem);
                }
            }
        }
        return numberOfSubmissionsByPhase;
    }

    @Override
    public void markChallengeSubmissionAsRead(ChallengeSubmissionDto challengeSubmissionDto) {
        ChallengeSubmissionEntity challengeSubmissionEntity =
                challengeSubmissionRepository.findOne(challengeSubmissionDto.getChallengeSubmissionId());
        if (challengeSubmissionEntity != null) {
            challengeSubmissionEntity.setIsRead(challengeSubmissionDto.getIsRead());
            challengeSubmissionRepository.save(challengeSubmissionEntity);
        }
    }

    @Override
    public ChallengeSubmissionEntity findChallengeSubmissionByRegistrantPhase(Long registrantId, ChallengePhaseEnum phase) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("challengeSubmission");

        searchQueryBuilder.withQuery(filteredQuery(matchAllQuery(), boolFilter()
                .must(termFilter("registrantId", registrantId))
                .must(termFilter("submissionPhase", phase))));

        List<ChallengeSubmissionEntity> submissionEntities = DataUtils.getAllEntities(
                challengeSubmissionRepository, searchQueryBuilder);

        if (!submissionEntities.isEmpty()) {
            return submissionEntities.get(0);
        }
        return null;
    }

    @Override
    public List<ChallengeSubmissionEntity> findChallengeSubmissionByRegistrant(Long registrantId) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("challengeSubmission");
        searchQueryBuilder.withQuery(filteredQuery(matchAllQuery(), termFilter("registrantId", registrantId)));
        return DataUtils.getAllEntities(challengeSubmissionRepository, searchQueryBuilder);
    }

    @Override
    public List<ChallengeSubmissionEntity> findChallengeSubmissionWithinPeriod(
            Long challengeId, Long currentDateTime, TimePeriodEnum period) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("challengeSubmission");

        BoolQueryBuilder boolQueryBuilder = boolQuery();
        boolQueryBuilder.must(termQuery("challengeId", challengeId));

        boolQueryBuilder.must(rangeQuery("submissionDateTime").from(yesterdayDate()));
        searchQueryBuilder.withQuery(boolQueryBuilder);
        searchQueryBuilder.withSort(fieldSort("challengeSubmissionId").order(SortOrder.DESC));

        return DataUtils.getAllEntities(challengeSubmissionRepository, searchQueryBuilder);
    }

}
