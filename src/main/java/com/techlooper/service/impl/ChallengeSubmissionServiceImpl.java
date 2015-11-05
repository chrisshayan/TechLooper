package com.techlooper.service.impl;

import com.techlooper.entity.ChallengeRegistrantDto;
import com.techlooper.entity.ChallengeRegistrantEntity;
import com.techlooper.entity.ChallengeSubmissionEntity;
import com.techlooper.entity.ChallengeSubmissionEntity.ChallengeSubmissionEntityBuilder;
import com.techlooper.model.*;
import com.techlooper.repository.elasticsearch.ChallengeSubmissionRepository;
import com.techlooper.service.ChallengeService;
import com.techlooper.service.ChallengeSubmissionService;
import com.techlooper.util.DateTimeUtils;
import freemarker.template.Template;
import org.dozer.Mapper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.joda.time.DateTime;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
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

/**
 * Created by phuonghqh on 10/9/15.
 */
@Service
public class ChallengeSubmissionServiceImpl implements ChallengeSubmissionService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ChallengeSubmissionServiceImpl.class);

    @Resource
    private ChallengeService challengeService;

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
        ChallengeDto challengeDto = challengeService.findChallengeById(
                challengeSubmissionDto.getChallengeId(), challengeSubmissionDto.getRegistrantEmail());
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
        try {
            sendConfirmationEmailToRegistrant(challengeDto, registrant, challengeSubmissionEntity);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return challengeSubmissionRepository.save(challengeSubmissionEntity);
    }

    private void sendConfirmationEmailToRegistrant(ChallengeDto challengeDto,
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
        templateModel.put("challengeId", String.valueOf(challengeDto.getChallengeId()));
        templateModel.put("challengeAlias", challengeDto.getChallengeName().replaceAll("\\W", "-"));
        templateModel.put("challengeName", challengeDto.getChallengeName());

        template.process(templateModel, stringWriter);
        mailSubject = String.format(mailSubject, challengeDto.getChallengeName());
        confirmUserSubmissionMailMessage.setSubject(MimeUtility.encodeText(mailSubject, "UTF-8", null));
        confirmUserSubmissionMailMessage.setText(stringWriter.toString(), "UTF-8", "html");

        stringWriter.flush();
        confirmUserSubmissionMailMessage.saveChanges();
        mailSender.send(confirmUserSubmissionMailMessage);
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
