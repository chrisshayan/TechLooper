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
import com.techlooper.util.DataUtils;
import com.techlooper.util.DateTimeUtils;
import freemarker.template.Template;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.StringWriter;
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

    private final static Logger LOGGER = LoggerFactory.getLogger(ChallengeSubmissionServiceImpl.class);

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

    @Value("${mail.confirmUserSubmission.subject.vi}")
    private String confirmUserSubmissionMailSubjectVi;

    @Value("${mail.confirmUserSubmission.subject.en}")
    private String confirmUserSubmissionMailSubjectEn;

    @Resource
    private Template confirmUserSubmissionMailTemplateVi;

    @Resource
    private Template confirmUserSubmissionMailTemplateEn;

    @Resource
    private MimeMessage confirmUserSubmissionMailMessage;

    @Value("${web.baseUrl}")
    private String webBaseUrl;

    @Resource
    private JavaMailSender mailSender;

    @Value("${mail.techlooper.reply_to}")
    private String techlooperReplyTo;

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

        try {
            sendConfirmationEmailToRegistrant(challenge, registrant, challengeSubmissionEntity);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return challengeSubmissionRepository.save(challengeSubmissionEntity);
    }

    private void sendConfirmationEmailToRegistrant(ChallengeEntity challenge,
                                                   ChallengeRegistrantEntity registrant, ChallengeSubmissionEntity challengeSubmissionEntity) throws Exception {
        String mailSubject = registrant.getLang() == Language.vi ? confirmUserSubmissionMailSubjectVi :
                confirmUserSubmissionMailSubjectEn;
        Address[] recipientAddresses = InternetAddress.parse(registrant.getRegistrantEmail());
        Template template = registrant.getLang() == Language.vi ? confirmUserSubmissionMailTemplateVi :
                confirmUserSubmissionMailTemplateEn;
        confirmUserSubmissionMailMessage.setRecipients(Message.RecipientType.TO, recipientAddresses);
        confirmUserSubmissionMailMessage.setReplyTo(InternetAddress.parse(techlooperReplyTo));
        StringWriter stringWriter = new StringWriter();

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("webBaseUrl", webBaseUrl);
        templateModel.put("submissionUrl", challengeSubmissionEntity.getSubmissionURL());
        templateModel.put("currentPhase", challengeSubmissionEntity.getSubmissionPhase());
        templateModel.put("challengeId", String.valueOf(challenge.getChallengeId()));
        templateModel.put("challengeAlias", challenge.getChallengeName().replaceAll("\\W", "-"));
        templateModel.put("challengeName", challenge.getChallengeName());

        template.process(templateModel, stringWriter);
        mailSubject = String.format(mailSubject, challenge.getChallengeName());
        confirmUserSubmissionMailMessage.setSubject(MimeUtility.encodeText(mailSubject, "UTF-8", null));
        confirmUserSubmissionMailMessage.setText(stringWriter.toString(), "UTF-8", "html");

        stringWriter.flush();
        confirmUserSubmissionMailMessage.saveChanges();
        mailSender.send(confirmUserSubmissionMailMessage);
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
