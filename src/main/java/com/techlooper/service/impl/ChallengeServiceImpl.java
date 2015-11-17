package com.techlooper.service.impl;

import com.techlooper.dto.EmailSettingDto;
import com.techlooper.entity.*;
import com.techlooper.model.*;
import com.techlooper.repository.elasticsearch.ChallengeRegistrantRepository;
import com.techlooper.repository.elasticsearch.ChallengeRepository;
import com.techlooper.service.*;
import com.techlooper.util.DataUtils;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.sum.SumBuilder;
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
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.techlooper.model.ChallengePhaseEnum.*;
import static com.techlooper.util.DateTimeUtils.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.elasticsearch.index.query.FilterBuilders.*;
import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.elasticsearch.search.aggregations.AggregationBuilders.sum;

/**
 * Created by NguyenDangKhoa on 6/29/15.
 */
@Service
public class ChallengeServiceImpl implements ChallengeService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ChallengeServiceImpl.class);

    private final static ChallengePhaseEnum CHALLENGE_TIMELINE[] = {FINAL, PROTOTYPE, UIUX, IDEA, REGISTRATION};

    @Resource
    private ElasticsearchTemplate elasticsearchTemplateUserImport;

    @Resource
    private MimeMessage postChallengeMailMessage;

    @Resource
    private Template postChallengeMailTemplateEn;

    @Resource
    private Template postChallengeUpdateMailTemplateEn;

    @Resource
    private Template postChallengeMailTemplateVi;

    @Value("${mail.postChallenge.subject.vn}")
    private String postChallengeMailSubjectVn;

    @Value("${mail.postChallenge.subject.en}")
    private String postChallengeMailSubjectEn;

    @Value("${mail.postChallenge.techloopies.mailSubject}")
    private String postChallengeTechloopiesMailSubject;

    @Value("${mail.postChallenge.techloopies.updateMailSubject}")
    private String postChallengeTechloopiesUpdateMailSubject;

    @Value("${mail.postChallenge.techloopies.mailList}")
    private String postChallengeTechloopiesMailList;

    @Value("${web.baseUrl}")
    private String webBaseUrl;

    @Resource
    private Template confirmUserJoinChallengeMailTemplateEn;

    @Resource
    private Template confirmUserJoinChallengeMailTemplateVi;

    @Value("${mail.confirmUserJoinChallenge.subject.vn}")
    private String confirmUserJoinChallengeMailSubjectVn;

    @Value("${mail.confirmUserJoinChallenge.subject.en}")
    private String confirmUserJoinChallengeMailSubjectEn;

    @Value("${mail.alertEmployerChallenge.subject.vn}")
    private String alertEmployerChallengeMailSubjectVn;

    @Value("${mail.alertEmployerChallenge.subject.en}")
    private String alertEmployerChallengeMailSubjectEn;

    @Value("${mail.techlooper.reply_to}")
    private String mailTechlooperReplyTo;

    @Resource
    private JavaMailSender mailSender;

    @Resource
    private ChallengeRepository challengeRepository;

    @Resource
    private ChallengeRegistrantRepository challengeRegistrantRepository;

    @Resource
    private Mapper dozerMapper;

    @Value("${elasticsearch.userimport.index.name}")
    private String techlooperIndex;

    @Value("${mail.notifyChallengeTimelineRegistration.subject.vn}")
    private String notifyChallengeTimelineRegistrationMailSubjectVn;

    @Value("${mail.notifyChallengeTimelineRegistration.subject.en}")
    private String notifyChallengeTimelineRegistrationMailSubjectEn;

    @Value("${mail.notifyChallengeTimelineInProgress.subject.vn}")
    private String notifyChallengeTimelineInProgressMailSubjectVn;

    @Value("${mail.notifyChallengeTimelineInProgress.subject.en}")
    private String notifyChallengeTimelineInProgressMailSubjectEn;

    @Value("${mail.dailyChallengeSummary.subject.en}")
    private String dailyChallengeSummaryMailSubjectEn;

    @Value("${mail.dailyChallengeSummary.subject.vi}")
    private String dailyChallengeSummaryMailSubjectVi;

    @Value("${mail.notifyChallengePhaseClosed.subject.en}")
    private String notifyChallengePhaseClosedMailSubjectEn;

    @Value("${mail.notifyChallengePhaseClosed.subject.vi}")
    private String notifyChallengePhaseClosedMailSubjectVi;

    @Resource
    private Template notifyChallengeTimelineMailTemplateVi;

    @Resource
    private Template notifyChallengeTimelineMailTemplateEn;

    @Resource
    private Template notifyChallengeSubmissionMailTemplateVi;

    @Resource
    private Template notifyChallengeSubmissionMailTemplateEn;

    @Resource
    private Template dailyChallengeSummaryMailTemplateVi;

    @Resource
    private Template dailyChallengeSummaryMailTemplateEn;

    @Resource
    private Template notifyChallengePhaseClosedMailTemplateVi;

    @Resource
    private Template notifyChallengePhaseClosedMailTemplateEn;

    @Resource
    private EmailService emailService;

    @Resource
    private EmployerService employerService;

    @Resource
    private CurrencyService currencyService;

    @Resource
    private ChallengeRegistrantService challengeRegistrantService;

    @Resource
    private ChallengeSubmissionService challengeSubmissionService;

    public ChallengeEntity savePostChallenge(ChallengeDto challengeDto) throws Exception {
        ChallengeEntity challengeEntity = dozerMapper.map(challengeDto, ChallengeEntity.class);
        if (challengeDto.getChallengeId() == null) {
            challengeEntity.setChallengeId(new Date().getTime());
        }

        challengeEntity.setCriteria(DataUtils.defaultChallengeCriterias(challengeEntity.getLang()));
        return challengeRepository.save(challengeEntity);
    }

    public void sendPostChallengeEmailToEmployer(ChallengeEntity challengeEntity)
            throws MessagingException, IOException, TemplateException {
        String mailSubject = challengeEntity.getLang() == Language.vi ? postChallengeMailSubjectVn : postChallengeMailSubjectEn;
        Address[] recipientAddresses = getRecipientAddresses(challengeEntity, true);
        Template template = challengeEntity.getLang() == Language.vi ? postChallengeMailTemplateVi : postChallengeMailTemplateEn;
        sendPostChallengeEmail(challengeEntity, mailSubject, recipientAddresses, template);
    }

    public void sendPostChallengeEmailToTechloopies(ChallengeEntity challengeEntity, Boolean isNewChallenge)
            throws MessagingException, IOException, TemplateException {
        String mailSubject = isNewChallenge ? postChallengeTechloopiesMailSubject :
                String.format(postChallengeTechloopiesUpdateMailSubject, challengeEntity.getChallengeName());
        Template mailTemplate = isNewChallenge ? postChallengeMailTemplateEn : postChallengeUpdateMailTemplateEn;
        Address[] recipientAddresses = InternetAddress.parse(postChallengeTechloopiesMailList);
        sendPostChallengeEmail(challengeEntity, mailSubject, recipientAddresses, mailTemplate);
    }

    @Override
    public void sendEmailNotifyRegistrantAboutChallengeTimeline(ChallengeEntity challengeEntity,
                                                                ChallengeRegistrantEntity challengeRegistrantEntity, ChallengePhaseEnum challengePhase, boolean isSpecificDayNotification) throws Exception {
        String mailSubject = getNotifyRegistrantChallengeTimelineSubject(challengeRegistrantEntity, challengePhase);
        Address[] recipientAddresses = InternetAddress.parse(challengeRegistrantEntity.getRegistrantEmail());
        Template template = challengeRegistrantEntity.getLang() == Language.vi ?
                notifyChallengeTimelineMailTemplateVi : notifyChallengeTimelineMailTemplateEn;

        if (isSpecificDayNotification) {
            template = challengeRegistrantEntity.getLang() == Language.vi ?
                    notifyChallengeSubmissionMailTemplateVi : notifyChallengeSubmissionMailTemplateEn;
        }

        postChallengeMailMessage.setRecipients(Message.RecipientType.TO, recipientAddresses);
        postChallengeMailMessage.setReplyTo(InternetAddress.parse(mailTechlooperReplyTo));
        StringWriter stringWriter = new StringWriter();

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("challengeEntity", challengeEntity);
        templateModel.put("webBaseUrl", webBaseUrl);
        templateModel.put("technologies", StringUtils.join(challengeEntity.getTechnologies(), "<br/>"));
        templateModel.put("receivedEmails", StringUtils.join(challengeEntity.getReceivedEmails(), "<br/>"));
        templateModel.put("firstPlaceReward", challengeEntity.getFirstPlaceReward() != null ? challengeEntity.getFirstPlaceReward() : 0);
        templateModel.put("secondPlaceReward", challengeEntity.getSecondPlaceReward() != null ? challengeEntity.getSecondPlaceReward() : 0);
        templateModel.put("thirdPlaceReward", challengeEntity.getThirdPlaceReward() != null ? challengeEntity.getThirdPlaceReward() : 0);
        templateModel.put("challengeId", challengeEntity.getChallengeId().toString());
        templateModel.put("challengeNameAlias", challengeEntity.getChallengeName().replaceAll("\\W", "-"));

        int numberOfDays = 0;
        if (challengePhase == REGISTRATION) {
            numberOfDays = daysBetween(currentDate(), challengeEntity.getRegistrationDateTime()) + 1;
        } else if (challengePhase == IN_PROGRESS) {
            numberOfDays = daysBetween(currentDate(), challengeEntity.getSubmissionDateTime()) + 1;
        } else if (challengePhase == IDEA) {
            numberOfDays = daysBetween(currentDate(), challengeEntity.getIdeaSubmissionDateTime()) + 1;
        } else if (challengePhase == UIUX) {
            numberOfDays = daysBetween(currentDate(), challengeEntity.getUxSubmissionDateTime()) + 1;
        } else if (challengePhase == PROTOTYPE) {
            numberOfDays = daysBetween(currentDate(), challengeEntity.getPrototypeSubmissionDateTime()) + 1;
        } else if (challengePhase == IN_PROGRESS) {
            numberOfDays = daysBetween(currentDate(), challengeEntity.getSubmissionDateTime()) + 1;
        } else if (challengePhase == FINAL) {
            numberOfDays = daysBetween(currentDate(), challengeEntity.getSubmissionDateTime()) + 1;
        }

        templateModel.put("numberOfDays", numberOfDays);
        templateModel.put("challengePhase", challengePhase.getValue());
        templateModel.put("challengeRegistrant", challengeRegistrantEntity);

        template.process(templateModel, stringWriter);
        mailSubject = String.format(mailSubject, numberOfDays, challengeEntity.getChallengeName());
        postChallengeMailMessage.setSubject(MimeUtility.encodeText(mailSubject, "UTF-8", null));
        postChallengeMailMessage.setText(stringWriter.toString(), "UTF-8", "html");

        stringWriter.flush();
        postChallengeMailMessage.saveChanges();
        mailSender.send(postChallengeMailMessage);
        LOGGER.info(postChallengeMailMessage.getMessageID() + " has been sent to users " +
                postChallengeMailMessage.getAllRecipients() + " with challengeId = " + challengeEntity.getChallengeId());
    }

    @Override
    public void sendEmailNotifyEmployerWhenPhaseClosed(ChallengeEntity challengeEntity, ChallengePhaseEnum currentPhase,
                                                       ChallengePhaseEnum oldPhase) throws Exception {
        String mailSubject = challengeEntity.getLang() == Language.vi ? notifyChallengePhaseClosedMailSubjectVi :
                notifyChallengePhaseClosedMailSubjectEn;
        Address[] recipientAddresses = getRecipientAddresses(challengeEntity, true);
        Template template = challengeEntity.getLang() == Language.vi ? notifyChallengePhaseClosedMailTemplateVi :
                notifyChallengePhaseClosedMailTemplateEn;

        postChallengeMailMessage.setRecipients(Message.RecipientType.TO, recipientAddresses);
        postChallengeMailMessage.setReplyTo(InternetAddress.parse(mailTechlooperReplyTo));
        StringWriter stringWriter = new StringWriter();

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("challenge", challengeEntity);
        templateModel.put("challengeId", String.valueOf(challengeEntity.getChallengeId()));
        templateModel.put("challengeNameAlias", challengeEntity.getChallengeName().replaceAll("\\W", "-"));
        templateModel.put("webBaseUrl", webBaseUrl);
        templateModel.put("oldPhase", oldPhase.getValue());

        ChallengeDetailDto challengeDetailDto = dozerMapper.map(challengeEntity, ChallengeDetailDto.class);
        calculateChallengePhases(challengeDetailDto);
        templateModel.put("currentPhase", currentPhase.getValue());

        Map<ChallengePhaseEnum, ChallengeRegistrantPhaseItem> numberOfRegistrantByPhase =
                challengeRegistrantService.countNumberOfRegistrantsByPhase(challengeEntity.getChallengeId());
        ChallengeRegistrantPhaseItem currentPhaseRegistrants = numberOfRegistrantByPhase.get(currentPhase);
        if (currentPhaseRegistrants != null) {
            templateModel.put("currentPhaseRegistrants", currentPhaseRegistrants.getRegistration());
        }

        template.process(templateModel, stringWriter);

        if (challengeEntity.getLang() == Language.vi) {
            mailSubject = String.format(mailSubject, oldPhase.getVi(), challengeEntity.getChallengeName());
        } else {
            mailSubject = String.format(mailSubject, oldPhase.getEn(), challengeEntity.getChallengeName());
        }
        postChallengeMailMessage.setSubject(MimeUtility.encodeText(mailSubject, "UTF-8", null));
        postChallengeMailMessage.setText(stringWriter.toString(), "UTF-8", "html");

        stringWriter.flush();
        postChallengeMailMessage.saveChanges();
        mailSender.send(postChallengeMailMessage);
    }

    private String getNotifyRegistrantChallengeTimelineSubject(
            ChallengeRegistrantEntity challengeRegistrantEntity, ChallengePhaseEnum challengePhase) {
        if (challengeRegistrantEntity.getLang() == Language.vi) {
            if (challengePhase == REGISTRATION) {
                return notifyChallengeTimelineRegistrationMailSubjectVn;
            } else {
                return notifyChallengeTimelineInProgressMailSubjectVn;
            }
        } else {
            if (challengePhase == REGISTRATION) {
                return notifyChallengeTimelineRegistrationMailSubjectEn;
            } else {
                return notifyChallengeTimelineInProgressMailSubjectEn;
            }
        }
    }

    public void sendApplicationEmailToContestant(ChallengeEntity challengeEntity, ChallengeRegistrantEntity challengeRegistrantEntity) throws MessagingException, IOException, TemplateException {
        Template template = challengeRegistrantEntity.getLang() == Language.vi ?
                confirmUserJoinChallengeMailTemplateVi : confirmUserJoinChallengeMailTemplateEn;
        String mailSubject = challengeRegistrantEntity.getLang() == Language.vi ?
                confirmUserJoinChallengeMailSubjectVn : confirmUserJoinChallengeMailSubjectEn;
        mailSubject = String.format(mailSubject, challengeEntity.getChallengeName());
        Address[] emailAddress = InternetAddress.parse(challengeRegistrantEntity.getRegistrantEmail());
        sendContestApplicationEmail(template, mailSubject, emailAddress, challengeEntity, challengeRegistrantEntity, false);
    }

    public long joinChallenge(ChallengeRegistrantDto challengeRegistrantDto) {
        ChallengeRegistrantEntity entity = joinChallengeEntity(challengeRegistrantDto);
        if (entity != null) {
            return challengeRegistrantService.getNumberOfRegistrants(challengeRegistrantDto.getChallengeId());
        }
        return 0;
    }

    public ChallengeRegistrantEntity joinChallengeEntity(ChallengeRegistrantDto challengeRegistrantDto) {
        Long challengeId = challengeRegistrantDto.getChallengeId();
        boolean isExist = checkIfChallengeRegistrantExist(challengeId, challengeRegistrantDto.getRegistrantEmail());

        if (!isExist) {
            ChallengeRegistrantEntity challengeRegistrantEntity = dozerMapper.map(challengeRegistrantDto, ChallengeRegistrantEntity.class);
            ChallengeEntity challengeEntity = challengeRepository.findOne(challengeId);
            challengeRegistrantEntity.setRegistrantId(new Date().getTime());
            if (challengeEntity.getCriteria().size() > 0) {
                final Set<ChallengeRegistrantCriteria> criteria = new HashSet<>();
                challengeEntity.getCriteria().forEach(cri -> criteria.add(dozerMapper.map(cri, ChallengeRegistrantCriteria.class)));
                challengeRegistrantEntity.setCriteria(criteria);
            }
            challengeRegistrantEntity = challengeRegistrantRepository.save(challengeRegistrantEntity);

            try {
                sendApplicationEmailToContestant(challengeEntity, challengeRegistrantEntity);
                challengeRegistrantEntity.setMailSent(Boolean.TRUE);
                return challengeRegistrantRepository.save(challengeRegistrantEntity);
            } catch (Exception e) {
                LOGGER.debug("Can not send email", e);
                return challengeRegistrantEntity;
            }
        }

        return null;
    }

    public List<ChallengeDetailDto> listChallenges() {
        TermQueryBuilder notExpiredQuery = termQuery("expired", Boolean.TRUE);
        Iterable<ChallengeEntity> challengeIterator = challengeRepository.search(boolQuery().mustNot(notExpiredQuery));
        ArrayList<ChallengeDetailDto> challenges = new ArrayList<>();
        challengeIterator.forEach(challengeEntity -> {
            ChallengeDetailDto challengeDetailDto = dozerMapper.map(challengeEntity, ChallengeDetailDto.class);
            challengeDetailDto.setNumberOfRegistrants(challengeRegistrantService.getNumberOfRegistrants(
                    challengeEntity.getChallengeId()));
            challenges.add(challengeDetailDto);
        });
        return sortChallengesByDescendingStartDate(challenges);
    }

    private void sendContestApplicationEmail(Template template, String mailSubject, Address[] recipientAddresses,
                                             ChallengeEntity challengeEntity, ChallengeRegistrantEntity challengeRegistrantEntity, boolean hasReplyTo)
            throws MessagingException, IOException, TemplateException {
        postChallengeMailMessage.setRecipients(Message.RecipientType.TO, recipientAddresses);

        if (hasReplyTo) {
            postChallengeMailMessage.setReplyTo(InternetAddress.parse(challengeRegistrantEntity.getRegistrantEmail()));
        } else {
            postChallengeMailMessage.setReplyTo(InternetAddress.parse(mailTechlooperReplyTo));
        }

        StringWriter stringWriter = new StringWriter();
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("webBaseUrl", webBaseUrl);
        templateModel.put("challengeName", challengeEntity.getChallengeName());
        templateModel.put("businessRequirement", challengeEntity.getBusinessRequirement());
        templateModel.put("generalNote", challengeEntity.getGeneralNote());
        templateModel.put("technologies", StringUtils.join(challengeEntity.getTechnologies(), "<br/>"));
        templateModel.put("documents", challengeEntity.getDocuments());
        templateModel.put("deliverables", challengeEntity.getDeliverables());
        templateModel.put("receivedEmails", StringUtils.join(challengeEntity.getReceivedEmails(), "<br/>"));
        templateModel.put("reviewStyle", challengeEntity.getReviewStyle());
        templateModel.put("startDate", challengeEntity.getStartDateTime());
        templateModel.put("registrationDate", challengeEntity.getRegistrationDateTime());
        templateModel.put("submissionDate", challengeEntity.getSubmissionDateTime());
        templateModel.put("qualityIdea", challengeEntity.getQualityIdea());
        templateModel.put("firstPlaceReward", challengeEntity.getFirstPlaceReward() != null ? challengeEntity.getFirstPlaceReward() : 0);
        templateModel.put("secondPlaceReward", challengeEntity.getSecondPlaceReward() != null ? challengeEntity.getSecondPlaceReward() : 0);
        templateModel.put("thirdPlaceReward", challengeEntity.getThirdPlaceReward() != null ? challengeEntity.getThirdPlaceReward() : 0);
        templateModel.put("challengeId", challengeEntity.getChallengeId().toString());
        templateModel.put("authorEmail", challengeEntity.getAuthorEmail());
        templateModel.put("challengeOverview", challengeEntity.getChallengeOverview());
        templateModel.put("firstName", challengeRegistrantEntity.getRegistrantFirstName());
        templateModel.put("lastName", challengeRegistrantEntity.getRegistrantLastName());
        templateModel.put("registrantEmail", challengeRegistrantEntity.getRegistrantEmail());
        templateModel.put("challengeNameAlias", challengeEntity.getChallengeName().replaceAll("\\W", "-"));

        template.process(templateModel, stringWriter);
        postChallengeMailMessage.setSubject(MimeUtility.encodeText(mailSubject, "UTF-8", null));
        postChallengeMailMessage.setText(stringWriter.toString(), "UTF-8", "html");

        stringWriter.flush();
        postChallengeMailMessage.saveChanges();
        mailSender.send(postChallengeMailMessage);
    }

    private void sendPostChallengeEmail(ChallengeEntity challengeEntity, String mailSubject,
                                        Address[] recipientAddresses, Template template) throws MessagingException, IOException, TemplateException {
        postChallengeMailMessage.setRecipients(Message.RecipientType.TO, recipientAddresses);
        postChallengeMailMessage.setReplyTo(InternetAddress.parse(mailTechlooperReplyTo));
        StringWriter stringWriter = new StringWriter();

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("webBaseUrl", webBaseUrl);
        templateModel.put("challengeName", challengeEntity.getChallengeName());
        templateModel.put("businessRequirement", challengeEntity.getBusinessRequirement());
        templateModel.put("generalNote", challengeEntity.getGeneralNote());
        templateModel.put("technologies", StringUtils.join(challengeEntity.getTechnologies(), "<br/>"));
        templateModel.put("documents", challengeEntity.getDocuments());
        templateModel.put("deliverables", challengeEntity.getDeliverables());
        templateModel.put("receivedEmails", StringUtils.join(challengeEntity.getReceivedEmails(), "<br/>"));
        templateModel.put("reviewStyle", challengeEntity.getReviewStyle());
        templateModel.put("startDate", challengeEntity.getStartDateTime());
        templateModel.put("registrationDate", challengeEntity.getRegistrationDateTime());
        templateModel.put("submissionDate", challengeEntity.getSubmissionDateTime());
        templateModel.put("qualityIdea", challengeEntity.getQualityIdea());
        templateModel.put("firstPlaceReward", challengeEntity.getFirstPlaceReward() != null ? challengeEntity.getFirstPlaceReward() : 0);
        templateModel.put("secondPlaceReward", challengeEntity.getSecondPlaceReward() != null ? challengeEntity.getSecondPlaceReward() : 0);
        templateModel.put("thirdPlaceReward", challengeEntity.getThirdPlaceReward() != null ? challengeEntity.getThirdPlaceReward() : 0);
        templateModel.put("challengeId", challengeEntity.getChallengeId().toString());
        templateModel.put("authorEmail", challengeEntity.getAuthorEmail());
        templateModel.put("challengeOverview", challengeEntity.getChallengeOverview());
        templateModel.put("challengeNameAlias", challengeEntity.getChallengeName().replaceAll("\\W", "-"));

        template.process(templateModel, stringWriter);
        String formatMailSubject = String.format(mailSubject, challengeEntity.getAuthorEmail(), challengeEntity.getChallengeName());
        postChallengeMailMessage.setSubject(MimeUtility.encodeText(formatMailSubject, "UTF-8", null));
        postChallengeMailMessage.setText(stringWriter.toString(), "UTF-8", "html");

        stringWriter.flush();
        postChallengeMailMessage.saveChanges();
        mailSender.send(postChallengeMailMessage);
    }

    private Address[] getRecipientAddresses(ChallengeEntity challengeEntity, boolean includeAuthor) throws AddressException {
        Set<String> emails = new HashSet<>(challengeEntity.getReceivedEmails());
        if (includeAuthor) {
            emails.add(challengeEntity.getAuthorEmail());
        }
        return InternetAddress.parse(StringUtils.join(emails, ','));
    }

    private List<ChallengeDetailDto> sortChallengesByDescendingStartDate(List<ChallengeDetailDto> challenges) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return challenges.stream().sorted((challenge1, challenge2) -> {
            try {
                if (challenge2.getStartDateTime() == null) {
                    return -1;
                } else if (challenge1.getStartDateTime() == null) {
                    return 1;
                }
                long challenge2StartDate = sdf.parse(challenge2.getStartDateTime()).getTime();
                long challenge1StartDate = sdf.parse(challenge1.getStartDateTime()).getTime();
                if (challenge2StartDate - challenge1StartDate > 0) {
                    return 1;
                } else if (challenge2StartDate - challenge1StartDate < 0) {
                    return -1;
                } else {
                    return 0;
                }
            } catch (ParseException e) {
                return 0;
            }
        }).collect(toList());
    }

    public boolean checkIfChallengeRegistrantExist(Long challengeId, String email) {
        return challengeRegistrantService.findRegistrantByChallengeIdAndEmail(challengeId, email) != null;
    }

    @Override
    public Long getTotalNumberOfChallenges() {
        return challengeRepository.count();
    }

    @Override
    public Double getTotalAmountOfPrizeValues() {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withSearchType(SearchType.COUNT);
        searchQueryBuilder.withQuery(matchAllQuery());

        SumBuilder sumPrizeBuilder = sum("sumPrize").script("doc['firstPlaceReward'].value + doc['secondPlaceReward'].value + doc['thirdPlaceReward'].value");
        searchQueryBuilder.addAggregation(sumPrizeBuilder);

        Aggregations aggregations = elasticsearchTemplateUserImport.query(searchQueryBuilder.build(), SearchResponse::getAggregations);
        Sum sumReponse = aggregations.get("sumPrize");
        return sumReponse != null ? sumReponse.getValue() : 0D;
    }

    @Override
    public ChallengeDetailDto getTheLatestChallenge() {
        return listChallenges().get(0);
    }

    public List<ChallengeDetailDto> listChallenges(String ownerEmail) {
        List<ChallengeDetailDto> result = new ArrayList<>();
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("challenge");
        MatchQueryBuilder authorEmailQuery = matchQuery("authorEmail", ownerEmail).minimumShouldMatch("100%");
        TermQueryBuilder notExpiredQuery = termQuery("expired", Boolean.TRUE);

        searchQueryBuilder.withQuery(boolQuery().must(authorEmailQuery).mustNot(notExpiredQuery));
        List<ChallengeEntity> challengeEntities = DataUtils.getAllEntities(challengeRepository, searchQueryBuilder);

        for (ChallengeEntity challengeEntity : challengeEntities) {
            ChallengeDetailDto challengeDetailDto = dozerMapper.map(challengeEntity, ChallengeDetailDto.class);
            challengeDetailDto.setNumberOfRegistrants(challengeRegistrantService.getNumberOfRegistrants(
                    challengeDetailDto.getChallengeId()));
            result.add(challengeDetailDto);
        }
        return result;
    }

    public boolean delete(Long id, String ownerEmail) {
        ChallengeEntity challenge = challengeRepository.findOne(id);
        if (challenge.getAuthorEmail().equalsIgnoreCase(ownerEmail)) {
            challenge.setExpired(Boolean.TRUE);
            challengeRepository.save(challenge);
            return true;
        }
        return false;
    }

    public ChallengeDto findChallengeById(Long id, String ownerEmail) {
        ChallengeEntity challenge = challengeRepository.findOne(id);
        if (!challenge.getAuthorEmail().equalsIgnoreCase(ownerEmail)) {
            challenge.setCriteria(null);
        }
        return dozerMapper.map(challenge, ChallengeDto.class);
    }

    @Override
    public List<ChallengeEntity> listChallengesByPhase(ChallengePhaseEnum challengePhase) {
        List<ChallengeEntity> challengeEntities = new ArrayList<>();
        // from <= NOW < to
        RangeFilterBuilder fromFilter = rangeFilter(challengePhase.getFromDateTimeField()).lt("now/d");
        RangeFilterBuilder toFilter = rangeFilter(challengePhase.getToDateTimeField()).gte("now/d");
        TermFilterBuilder expiredChallengeFilter = termFilter("expired", Boolean.TRUE);
        BoolFilterBuilder dateTimeRangeFilter = boolFilter().must(fromFilter).must(toFilter).mustNot(expiredChallengeFilter);

        Iterator<ChallengeEntity> challengeIterator =
                challengeRepository.search(filteredQuery(matchAllQuery(), dateTimeRangeFilter)).iterator();
        while (challengeIterator.hasNext()) {
            challengeEntities.add(challengeIterator.next());
        }

        return challengeEntities;
    }

    @Override
    public void sendDailySummaryEmailToChallengeOwner(ChallengeEntity challengeEntity) throws Exception {
        String mailSubject = challengeEntity.getLang() == Language.vi ? dailyChallengeSummaryMailSubjectVi :
                dailyChallengeSummaryMailSubjectEn;
        Address[] recipientAddresses = getRecipientAddresses(challengeEntity, true);
        Template template = challengeEntity.getLang() == Language.vi ?
                dailyChallengeSummaryMailTemplateVi : dailyChallengeSummaryMailTemplateEn;
        postChallengeMailMessage.setRecipients(Message.RecipientType.TO, recipientAddresses);
        Address[] replyToAddresses = InternetAddress.parse(postChallengeTechloopiesMailList);
        postChallengeMailMessage.setReplyTo(replyToAddresses);
        StringWriter stringWriter = new StringWriter();

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("webBaseUrl", webBaseUrl);
        templateModel.put("challengeName", challengeEntity.getChallengeName());
        templateModel.put("challengeId", challengeEntity.getChallengeId().toString());
        templateModel.put("challengeNameAlias", challengeEntity.getChallengeName().replaceAll("\\W", "-"));
        templateModel.put("currentDateTime", String.valueOf(new Date().getTime()));
        templateModel.put("yesterdayDateTime", yesterdayDate());

        Long currentDateTime = new Date().getTime();
        List<ChallengeRegistrantEntity> latestRegistrants = challengeRegistrantService.findChallengeRegistrantWithinPeriod(
                challengeEntity.getChallengeId(), currentDateTime, TimePeriodEnum.TWENTY_FOUR_HOURS);
        templateModel.put("numberOfRegistrants", latestRegistrants.size());
        templateModel.put("latestRegistrants", latestRegistrants);

        List<ChallengeSubmissionEntity> latestSubmissions = challengeSubmissionService.findChallengeSubmissionWithinPeriod(
                challengeEntity.getChallengeId(), currentDateTime, TimePeriodEnum.TWENTY_FOUR_HOURS);
        templateModel.put("numberOfSubmissions", latestSubmissions.size());
        templateModel.put("latestSubmissions", latestSubmissions);

        template.process(templateModel, stringWriter);
        mailSubject = String.format(mailSubject, challengeEntity.getChallengeName());
        postChallengeMailMessage.setSubject(MimeUtility.encodeText(mailSubject, "UTF-8", null));
        postChallengeMailMessage.setText(stringWriter.toString(), "UTF-8", "html");

        stringWriter.flush();
        postChallengeMailMessage.saveChanges();
        mailSender.send(postChallengeMailMessage);
    }

    public boolean isOwnerOfChallenge(String ownerEmail, Long challengeId) {
        ChallengeEntity challenge = challengeRepository.findOne(challengeId);
        return challenge.getAuthorEmail().equalsIgnoreCase(ownerEmail);
    }

    public ChallengeEntity findChallengeIdAndOwnerEmail(Long challengeId, String ownerEmail) {
        ChallengeEntity challenge = challengeRepository.findOne(challengeId);
        if (challenge == null || !challenge.getAuthorEmail().equalsIgnoreCase(ownerEmail)) {
            return null;
        }
        return challenge;
    }

    public boolean sendEmailToDailyChallengeRegistrants(String challengeOwner, Long challengeId, Long now, EmailContent emailContent) {
        if (isOwnerOfChallenge(challengeOwner, challengeId)) {
            List<ChallengeRegistrantEntity> registrants = challengeRegistrantService.findChallengeRegistrantWithinPeriod(challengeId, now, TimePeriodEnum.TWENTY_FOUR_HOURS);
            String csvEmails = registrants.stream().map(ChallengeRegistrantEntity::getRegistrantEmail).distinct().collect(joining(","));
            try {
                emailContent.setRecipients(InternetAddress.parse(csvEmails));
            } catch (AddressException e) {
                LOGGER.debug("Can not parse email address", e);
                return false;
            }
        }
        return emailService.sendEmail(emailContent);
    }

    private void bindEmailTemplateVariables(EmailContent emailContent, ChallengeDto challengeDto, ChallengeRegistrantEntity registrant) {
        String subject = emailContent.getSubject();
        String body = emailContent.getContent();
        EmailSettingDto emailSettingDto = employerService.findEmployerEmailSetting(challengeDto.getAuthorEmail());
        // Process email subject
        subject = processEmailVariables(challengeDto, registrant, emailSettingDto, subject);
        // Process email body
        body = processEmailVariables(challengeDto, registrant, emailSettingDto, body);

        emailContent.setSubject(subject);
        emailContent.setContent(body);
        try {
            emailContent.setReplyTo(InternetAddress.parse(emailSettingDto.getReplyEmail()));
        } catch (AddressException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    private String processEmailVariables(ChallengeDto challengeDto, ChallengeRegistrantEntity registrant,
                                         EmailSettingDto emailSettingDto, String replacementCandidate) {
        String result = replacementCandidate;

        if (result.contains(EmailService.VAR_CONTEST_NAME)) {
            result = result.replace(EmailService.VAR_CONTEST_NAME, challengeDto.getChallengeName());
        }
        if (result.contains(EmailService.VAR_CONTEST_FIRST_PRIZE)) {
            String formatPrize = currencyService.formatCurrency(Double.valueOf(challengeDto.getFirstPlaceReward()), Locale.US);
            result = result.replace(EmailService.VAR_CONTEST_FIRST_PRIZE, formatPrize);
        }
        if (result.contains(EmailService.VAR_CONTESTANT_FIRST_NAME)) {
            result = result.replace(EmailService.VAR_CONTESTANT_FIRST_NAME, registrant.getRegistrantFirstName());
        }
        if (result.contains(EmailService.VAR_MAIL_SIGNATURE)) {
            result = result.replace(EmailService.VAR_MAIL_SIGNATURE, emailSettingDto.getEmailSignature());
        }
        if (result.contains(EmailService.VAR_CONTEST_SECOND_PRIZE) && challengeDto.getSecondPlaceReward() != null) {
            String formatPrize = currencyService.formatCurrency(Double.valueOf(challengeDto.getSecondPlaceReward()), Locale.US);
            formatPrize = StringUtils.isNotEmpty(formatPrize) ? formatPrize : "";
            result = result.replace(EmailService.VAR_CONTEST_SECOND_PRIZE, formatPrize);
        }
        if (result.contains(EmailService.VAR_CONTEST_THIRD_PRIZE) && challengeDto.getThirdPlaceReward() != null) {
            String formatPrize = currencyService.formatCurrency(Double.valueOf(challengeDto.getThirdPlaceReward()), Locale.US);
            formatPrize = StringUtils.isNotEmpty(formatPrize) ? formatPrize : "";
            result = result.replace(EmailService.VAR_CONTEST_THIRD_PRIZE, formatPrize);
        }

        Set<ChallengeRegistrantDto> winners = challengeRegistrantService.findWinnerRegistrantsByChallengeId(challengeDto.getChallengeId());
        Optional<ChallengeRegistrantDto> firstWinner = winners.stream().filter(
                winner -> winner.getReward() == RewardEnum.FIRST_PLACE).findFirst();
        Optional<ChallengeRegistrantDto> secondWinner = winners.stream().filter(
                winner -> winner.getReward() == RewardEnum.SECOND_PLACE).findFirst();
        Optional<ChallengeRegistrantDto> thirdWinner = winners.stream().filter(
                winner -> winner.getReward() == RewardEnum.THIRD_PLACE).findFirst();

        if (result.contains(EmailService.VAR_FIRST_WINNER_FIRST_NAME)
                || result.contains(EmailService.VAR_FIRST_WINNER_FULL_NAME)) {
            if (firstWinner.isPresent()) {
                String firstName = firstWinner.get().getRegistrantFirstName();
                String lastName = firstWinner.get().getRegistrantLastName();
                result = result.replace(EmailService.VAR_FIRST_WINNER_FIRST_NAME, firstName);
                result = result.replace(EmailService.VAR_FIRST_WINNER_FULL_NAME, firstName + " " + lastName);
            }
        }

        if (result.contains(EmailService.VAR_SECOND_WINNER_FIRST_NAME)
                || result.contains(EmailService.VAR_SECOND_WINNER_FULL_NAME)) {
            if (secondWinner.isPresent()) {
                String firstName = secondWinner.get().getRegistrantFirstName();
                String lastName = secondWinner.get().getRegistrantLastName();
                result = result.replace(EmailService.VAR_SECOND_WINNER_FIRST_NAME, firstName);
                result = result.replace(EmailService.VAR_SECOND_WINNER_FULL_NAME, firstName + " " + lastName);
            }
        }

        if (result.contains(EmailService.VAR_THIRD_WINNER_FIRST_NAME)
                || result.contains(EmailService.VAR_THIRD_WINNER_FULL_NAME)) {
            if (thirdWinner.isPresent()) {
                String firstName = thirdWinner.get().getRegistrantFirstName();
                String lastName = thirdWinner.get().getRegistrantLastName();
                result = result.replace(EmailService.VAR_THIRD_WINNER_FIRST_NAME, firstName);
                result = result.replace(EmailService.VAR_THIRD_WINNER_FULL_NAME, firstName + " " + lastName);
            }
        }

        return result;
    }

    public boolean sendEmailToRegistrant(String challengeOwner, Long registrantId, EmailContent emailContent) {
        boolean result = false;
        final Long ANNOUNCE_WINNER_EMAIL_TEMPLATE_EN = 4L;
        final Long ANNOUNCE_WINNER_EMAIL_TEMPLATE_VI = 104L;
        ChallengeRegistrantEntity registrant = challengeRegistrantRepository.findOne(registrantId);
        if (isOwnerOfChallenge(challengeOwner, registrant.getChallengeId())) {
            String csvEmails = registrant.getRegistrantEmail();

            if (emailContent.getTemplateId() == ANNOUNCE_WINNER_EMAIL_TEMPLATE_EN
                    || emailContent.getTemplateId() == ANNOUNCE_WINNER_EMAIL_TEMPLATE_VI) {
                csvEmails = challengeRegistrantService.findRegistrantsByChallengeId(registrant.getChallengeId()).stream()
                        .map(challengeRegistrant -> challengeRegistrant.getRegistrantEmail()).collect(Collectors.joining(","));
            }

            ChallengeDto challengeDto = findChallengeById(registrant.getChallengeId(), null);
            try {
                bindEmailTemplateVariables(emailContent, challengeDto, registrant);
                emailContent.setRecipients(InternetAddress.parse(csvEmails));
                result = emailService.sendEmail(emailContent);
            } catch (AddressException e) {
                LOGGER.debug("Can not parse email address", e);
            }
        }
        return result;
    }

    @Override
    public void updateSendEmailToContestantResultCode(ChallengeRegistrantEntity challengeRegistrantEntity, EmailSentResultEnum code) {
        if (challengeRegistrantEntity != null) {
            challengeRegistrantEntity.setLastEmailSentDateTime(currentDate(BASIC_DATE_TIME_PATTERN));
            challengeRegistrantEntity.setLastEmailSentResultCode(code.getValue());
            challengeRegistrantRepository.save(challengeRegistrantEntity);
        }
    }

    public void updateSendEmailToChallengeOwnerResultCode(ChallengeEntity challengeEntity, EmailSentResultEnum code) {
        if (challengeEntity != null) {
            challengeEntity.setLastEmailSentDateTime(currentDate(BASIC_DATE_TIME_PATTERN));
            challengeEntity.setLastEmailSentResultCode(code.getValue());
            challengeRepository.save(challengeEntity);
        }
    }

    public ChallengeDetailDto getChallengeDetail(Long challengeId, String loginEmail) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("challenge");
        TermQueryBuilder challengeIdQuery = termQuery("challengeId", challengeId);
        TermQueryBuilder expiredChallengeQuery = termQuery("expired", Boolean.TRUE);

        searchQueryBuilder.withQuery(boolQuery().must(challengeIdQuery).mustNot(expiredChallengeQuery));
        List<ChallengeEntity> challengeEntities = DataUtils.getAllEntities(challengeRepository, searchQueryBuilder);

        if (!challengeEntities.isEmpty()) {
            ChallengeEntity challengeEntity = challengeEntities.get(0);
            ChallengeDetailDto challengeDetailDto = dozerMapper.map(challengeEntity, ChallengeDetailDto.class);
            challengeDetailDto.setNumberOfRegistrants(challengeRegistrantService.getNumberOfRegistrants(challengeId));
            calculateChallengePhases(challengeDetailDto);
            boolean isAuthor = challengeEntity.getAuthorEmail().equals(loginEmail);
            challengeDetailDto.setIsAuthor(isAuthor);
            if (!isAuthor) {
                challengeDetailDto.setCriteria(null);
            }
            challengeDetailDto.setPhaseItems(challengeRegistrantService.getChallengeRegistrantFunnel(challengeId, loginEmail));

            try {
                Boolean isClosed = daysBetween(challengeDetailDto.getSubmissionDateTime(), currentDate()) > 0;
                challengeDetailDto.setIsClosed(isClosed);
            } catch (ParseException e) {
                LOGGER.error(e.getMessage(), e);
            }

            return challengeDetailDto;
        }
        return null;
    }

    public void calculateChallengePhases(ChallengeDetailDto challengeDetailDto) {
        String now = currentDate();

        String timeline[] = {
                challengeDetailDto.getSubmissionDateTime(),
                challengeDetailDto.getPrototypeSubmissionDateTime(),
                challengeDetailDto.getUxSubmissionDateTime(),
                challengeDetailDto.getIdeaSubmissionDateTime(),
                challengeDetailDto.getRegistrationDateTime()
        };

        int currentIndex = -1;
        int nextIndex = -1;
        for (int i = 0; i < timeline.length; ++i) {
            try {
                String milestone = timeline[i];
                if (daysBetween(now, milestone) >= 0) {
                    nextIndex = currentIndex;
                    currentIndex = i;
                }
            } catch (Exception e) {
                continue;
            }
        }

        if (currentIndex == -1) {//FINAL
            challengeDetailDto.setCurrentPhase(FINAL);
            challengeDetailDto.setNextPhase(FINAL);
        } else {
            challengeDetailDto.setCurrentPhase(CHALLENGE_TIMELINE[currentIndex]);
            challengeDetailDto.setNextPhase(CHALLENGE_TIMELINE[nextIndex > -1 ? nextIndex : currentIndex]);
        }
    }

}
