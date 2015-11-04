package com.techlooper.service.impl;

import com.techlooper.dto.EmailSettingDto;
import com.techlooper.dto.EmailTemplateDto;
import com.techlooper.entity.*;
import com.techlooper.model.*;
import com.techlooper.repository.elasticsearch.ChallengeRegistrantRepository;
import com.techlooper.repository.elasticsearch.ChallengeRepository;
import com.techlooper.repository.elasticsearch.ChallengeSubmissionRepository;
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
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.FacetedPage;
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
import java.util.function.Predicate;

import static com.techlooper.util.DateTimeUtils.*;
import static java.util.stream.Collectors.*;
import static org.elasticsearch.index.query.FilterBuilders.*;
import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.elasticsearch.search.aggregations.AggregationBuilders.sum;
import static org.elasticsearch.search.sort.SortBuilders.fieldSort;

/**
 * Created by NguyenDangKhoa on 6/29/15.
 */
@Service
public class ChallengeServiceImpl implements ChallengeService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ChallengeServiceImpl.class);

    private final static ChallengePhaseEnum CHALLENGE_TIMELINE[] = {
            ChallengePhaseEnum.FINAL,
            ChallengePhaseEnum.PROTOTYPE,
            ChallengePhaseEnum.UIUX,
            ChallengePhaseEnum.IDEA,
            ChallengePhaseEnum.REGISTRATION
    };

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

    @Resource
    private Template alertEmployerChallengeMailTemplateEn;

    @Resource
    private Template alertEmployerChallengeMailTemplateVi;

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
    private ChallengeSubmissionRepository challengeSubmissionRepository;

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

    @Resource
    private Template notifyChallengeTimelineMailTemplateVi;

    @Resource
    private Template notifyChallengeTimelineMailTemplateEn;

    @Resource
    private Template dailyChallengeSummaryMailTemplateVi;

    @Resource
    private Template dailyChallengeSummaryMailTemplateEn;

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

        challengeEntity.setCriteria(DataUtils.defaultChallengeCriterias());
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
              ChallengeRegistrantEntity challengeRegistrantEntity, ChallengePhaseEnum challengePhase) throws Exception {
        String mailSubject = getNotifyRegistrantChallengeTimelineSubject(challengeRegistrantEntity, challengePhase);
        Address[] recipientAddresses = InternetAddress.parse(challengeRegistrantEntity.getRegistrantEmail());
        Template template = challengeRegistrantEntity.getLang() == Language.vi ?
                notifyChallengeTimelineMailTemplateVi : notifyChallengeTimelineMailTemplateEn;
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
        if (challengePhase == ChallengePhaseEnum.REGISTRATION) {
            numberOfDays = daysBetween(currentDate(), challengeEntity.getRegistrationDateTime()) + 1;
        } else if (challengePhase == ChallengePhaseEnum.IN_PROGRESS) {
            numberOfDays = daysBetween(currentDate(), challengeEntity.getSubmissionDateTime()) + 1;
        } else if (challengePhase == ChallengePhaseEnum.IDEA) {
            numberOfDays = daysBetween(currentDate(), challengeEntity.getIdeaSubmissionDateTime()) + 1;
        } else if (challengePhase == ChallengePhaseEnum.UIUX) {
            numberOfDays = daysBetween(currentDate(), challengeEntity.getUxSubmissionDateTime()) + 1;
        } else if (challengePhase == ChallengePhaseEnum.PROTOTYPE) {
            numberOfDays = daysBetween(currentDate(), challengeEntity.getPrototypeSubmissionDateTime()) + 1;
        } else if (challengePhase == ChallengePhaseEnum.IN_PROGRESS) {
            numberOfDays = daysBetween(currentDate(), challengeEntity.getSubmissionDateTime()) + 1;
        } else if (challengePhase == ChallengePhaseEnum.FINAL) {
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

    private String getNotifyRegistrantChallengeTimelineSubject(
            ChallengeRegistrantEntity challengeRegistrantEntity, ChallengePhaseEnum challengePhase) {
        if (challengeRegistrantEntity.getLang() == Language.vi) {
            if (challengePhase == ChallengePhaseEnum.REGISTRATION) {
                return notifyChallengeTimelineRegistrationMailSubjectVn;
            } else {
                return notifyChallengeTimelineInProgressMailSubjectVn;
            }
        } else {
            if (challengePhase == ChallengePhaseEnum.REGISTRATION) {
                return notifyChallengeTimelineRegistrationMailSubjectEn;
            } else {
                return notifyChallengeTimelineInProgressMailSubjectEn;
            }
        }
    }

    public Long getNumberOfRegistrants(Long challengeId) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withSearchType(SearchType.COUNT);
        searchQueryBuilder.withFilter(FilterBuilders.termFilter("challengeId", challengeId));
        return challengeRegistrantRepository.search(searchQueryBuilder.build()).getTotalElements();
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

    public void sendApplicationEmailToEmployer(ChallengeEntity challengeEntity, ChallengeRegistrantEntity challengeRegistrantEntity) throws MessagingException, IOException, TemplateException {
        Template template = challengeRegistrantEntity.getLang() == Language.vi ?
                alertEmployerChallengeMailTemplateVi : alertEmployerChallengeMailTemplateEn;
        String mailSubject = challengeRegistrantEntity.getLang() == Language.vi ?
                alertEmployerChallengeMailSubjectVn : alertEmployerChallengeMailSubjectEn;
        mailSubject = String.format(mailSubject, challengeEntity.getChallengeName());
        Address[] emailAddress = getRecipientAddresses(challengeEntity, false);
        sendContestApplicationEmail(template, mailSubject, emailAddress, challengeEntity, challengeRegistrantEntity, true);
    }

    public long joinChallenge(ChallengeRegistrantDto challengeRegistrantDto) {
        ChallengeRegistrantEntity entity = joinChallengeEntity(challengeRegistrantDto);
        if (entity != null) {
            return getNumberOfRegistrants(challengeRegistrantDto.getChallengeId());
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
            challengeDetailDto.setNumberOfRegistrants(getNumberOfRegistrants(challengeEntity.getChallengeId()));
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

    @Override
    public ChallengeRegistrantEntity findRegistrantByChallengeIdAndEmail(Long challengeId, String email) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("challengeRegistrant");
        searchQueryBuilder.withQuery(boolQuery()
                .must(termQuery("registrantEmail", email))
                .must(termQuery("challengeId", challengeId)));

        List<ChallengeRegistrantEntity> registrantEntities = DataUtils.getAllEntities(challengeRegistrantRepository, searchQueryBuilder);
        if (!registrantEntities.isEmpty()) {
            return registrantEntities.get(0);
        }
        return null;
    }

    public boolean checkIfChallengeRegistrantExist(Long challengeId, String email) {
        return findRegistrantByChallengeIdAndEmail(challengeId, email) != null;
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
    public Long getTotalNumberOfRegistrants() {
        return challengeRegistrantRepository.count();
    }

    @Override
    public ChallengeDetailDto getTheLatestChallenge() {
//    NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
//    searchQueryBuilder.withQuery(QueryBuilders.matchAllQuery());
//    searchQueryBuilder.withSort(SortBuilders.fieldSort("challengeId").order(SortOrder.DESC));
//    searchQueryBuilder.withPageable(new PageRequest(0, 1));
//
//    List<ChallengeEntity> challengeEntities = challengeRepository.search(searchQueryBuilder.build()).getContent();
//    if (!challengeEntities.isEmpty()) {
//      ChallengeEntity challengeEntity = challengeEntities.get(0);
//      return dozerMapper.map(challengeEntity, ChallengeDetailDto.class);
//    }
        return listChallenges().get(0);
    }

    public Collection<ChallengeDetailDto> findByOwnerAndCondition(String owner,
                                                                  Predicate<? super ChallengeEntity> condition) {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder().withIndices(techlooperIndex).withTypes("challenge");
        QueryStringQueryBuilder query = queryStringQuery(owner).defaultField("authorEmail");
        queryBuilder.withFilter(FilterBuilders.queryFilter(query));

        int pageIndex = 0;
        Set<ChallengeDetailDto> challenges = new HashSet<>();
        while (true) {
            queryBuilder.withPageable(new PageRequest(pageIndex++, 100));
            FacetedPage<ChallengeEntity> page = challengeRepository.search(queryBuilder.build());
            if (!page.hasContent()) {
                break;
            }

            page.spliterator().forEachRemaining(challenge -> {
                if (condition.test(challenge)) {
                    ChallengeDetailDto challengeDetailDto = dozerMapper.map(challenge, ChallengeDetailDto.class);
                    challengeDetailDto.setNumberOfRegistrants(countRegistrantsByChallengeId(challenge.getChallengeId()));
                    challenges.add(challengeDetailDto);
                }
            });
        }
        return challenges;
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
            challengeDetailDto.setNumberOfRegistrants(getNumberOfRegistrants(challengeDetailDto.getChallengeId()));
            result.add(challengeDetailDto);
        }
        return result;
    }

    public Collection<ChallengeDetailDto> findInProgressChallenges(String owner) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy");
        return findByOwnerAndCondition(owner, challengeEntity -> {
            DateTime startDate = dateTimeFormatter.parseDateTime(challengeEntity.getStartDateTime());
            DateTime submissionDate = dateTimeFormatter.parseDateTime(challengeEntity.getSubmissionDateTime());
            DateTime now = DateTime.now();
            boolean inRange = now.isAfter(startDate) && now.isBefore(submissionDate);
            boolean atBoundary = now.isEqual(startDate) || now.isEqual(submissionDate);
            return inRange || atBoundary;
        });
    }

    public Long countRegistrantsByChallengeId(Long challengeId) {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder().withIndices(techlooperIndex).withTypes("challengeRegistrant");
        queryBuilder.withFilter(FilterBuilders.queryFilter(termQuery("challengeId", challengeId)))
                .withSearchType(SearchType.COUNT);
        return challengeRegistrantRepository.search(queryBuilder.build()).getTotalElements();
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

    public Set<ChallengeRegistrantDto> findRegistrantsByOwner(RegistrantFilterCondition condition) throws ParseException {
        Set<ChallengeRegistrantDto> result = new HashSet<>();
        Long challengeId = condition.getChallengeId();
        ChallengeEntity challengeEntity = challengeRepository.findOne(challengeId);

        if (challengeEntity != null && challengeEntity.getAuthorEmail().equals(condition.getAuthorEmail())) {
            List<ChallengeRegistrantEntity> registrants = filterChallengeRegistrantByDate(condition);
            for (ChallengeRegistrantEntity registrant : registrants) {
                ChallengeRegistrantDto registrantDto = dozerMapper.map(registrant, ChallengeRegistrantDto.class);
                registrantDto.setSubmissions(findChallengeSubmissionByRegistrant(challengeId, registrant.getRegistrantId()));
                result.add(registrantDto);
            }
        }

        return result;
    }

    public ChallengeRegistrantDto saveRegistrant(String ownerEmail, ChallengeRegistrantDto challengeRegistrantDto) {
        ChallengeRegistrantDto resultChallengeRegistrantDto = challengeRegistrantDto;
        ChallengeEntity challenge = challengeRepository.findOne(challengeRegistrantDto.getChallengeId());
        if (ownerEmail.equalsIgnoreCase(challenge.getAuthorEmail())) {
            ChallengeRegistrantEntity registrant = challengeRegistrantRepository.findOne(challengeRegistrantDto.getRegistrantId());
            challengeRegistrantDto.setRegistrantEmail(registrant.getRegistrantEmail());
            dozerMapper.map(challengeRegistrantDto, registrant);
            registrant = challengeRegistrantRepository.save(registrant);
            resultChallengeRegistrantDto = dozerMapper.map(registrant, ChallengeRegistrantDto.class);
        }
        return resultChallengeRegistrantDto;
    }

    @Override
    public List<ChallengeRegistrantEntity> findChallengeRegistrantWithinPeriod(
            Long challengeId, Long currentDateTime, TimePeriodEnum period) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("challengeRegistrant");

        BoolQueryBuilder boolQueryBuilder = boolQuery();
        boolQueryBuilder.must(termQuery("challengeId", challengeId));

        Long pastTime = currentDateTime - period.getMiliseconds() > 0 ? currentDateTime - period.getMiliseconds() : 0;
        boolQueryBuilder.must(rangeQuery("registrantId").from(pastTime));
        searchQueryBuilder.withQuery(boolQueryBuilder);
        searchQueryBuilder.withSort(fieldSort("registrantId").order(SortOrder.DESC));
        return DataUtils.getAllEntities(challengeRegistrantRepository, searchQueryBuilder);
    }

    @Override
    public List<ChallengeRegistrantEntity> filterChallengeRegistrantByDate(RegistrantFilterCondition condition) throws ParseException {
        List<ChallengeRegistrantEntity> result = new ArrayList<>();

        if (ChallengeRegistrantFilterTypeEnum.BY_SUBMISSION.getValue().equals(condition.getFilterType())) {
            Set<Long> registrantIds = findRegistrantByChallengeSubmissionDate(
                    condition.getChallengeId(), condition.getFromDate(), condition.getToDate());
            for (Long registrantId : registrantIds) {
                ChallengeRegistrantEntity registrantEntity = challengeRegistrantRepository.findOne(registrantId);
                if (registrantEntity != null) {
                    result.add(registrantEntity);
                }
            }

            if (StringUtils.isNotEmpty(condition.getPhase()) &&
                    !ChallengePhaseEnum.ALL_PHASES.getValue().equals(condition.getPhase())) {
                result = result.stream().filter(registrantEntity ->
                        condition.getPhase().equals(registrantEntity.getActivePhase().getValue())).collect(toList());
            }
        } else {
            NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("challengeRegistrant");
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            final String registrationPhase = ChallengePhaseEnum.REGISTRATION.getValue();

            if (condition.getChallengeId() != null) {
                boolQueryBuilder.must(termQuery("challengeId", condition.getChallengeId()));
            }

            if (StringUtils.isNotEmpty(condition.getPhase())
                    && !registrationPhase.equals(condition.getPhase())
                    && !ChallengePhaseEnum.ALL_PHASES.getValue().equals(condition.getPhase())) {
                boolQueryBuilder.must(matchPhraseQuery("activePhase", condition.getPhase()));
            }

            if (StringUtils.isNotEmpty(condition.getFilterType()) &&
                    (StringUtils.isNotEmpty(condition.getFromDate()) || StringUtils.isNotEmpty(condition.getToDate()))) {
                RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(condition.getFilterType());

                if (StringUtils.isNotEmpty(condition.getFromDate())) {
                    Long from = string2Date(condition.getFromDate(), BASIC_DATE_PATTERN).getTime();
                    rangeQueryBuilder.from(from);
                }
                if (StringUtils.isNotEmpty(condition.getToDate())) {
                    Long to = string2Date(condition.getToDate(), BASIC_DATE_PATTERN).getTime() +
                            TimePeriodEnum.TWENTY_FOUR_HOURS.getMiliseconds();
                    rangeQueryBuilder.to(to);
                }

                boolQueryBuilder.must(rangeQueryBuilder);
            }

            searchQueryBuilder.withQuery(boolQueryBuilder);
            result.addAll(DataUtils.getAllEntities(challengeRegistrantRepository, searchQueryBuilder));

            if (registrationPhase.equals(condition.getPhase())) {
                result = result.stream().filter(registrantEntity -> registrantEntity.getActivePhase() == null
                        || registrationPhase.equals(registrantEntity.getActivePhase().getValue())).collect(toList());
            }
        }

        return result;
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
        List<ChallengeRegistrantEntity> latestRegistrants = findChallengeRegistrantWithinPeriod(
                challengeEntity.getChallengeId(), currentDateTime, TimePeriodEnum.TWENTY_FOUR_HOURS);
        templateModel.put("numberOfRegistrants", latestRegistrants.size());
        templateModel.put("latestRegistrants", latestRegistrants);

        List<ChallengeSubmissionEntity> latestSubmissions = findChallengeSubmissionWithinPeriod(
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
            List<ChallengeRegistrantEntity> registrants = findChallengeRegistrantWithinPeriod(challengeId, now, TimePeriodEnum.TWENTY_FOUR_HOURS);
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
        Long templateId = emailContent.getTemplateId();
        EmailTemplateDto emailTemplateDto = emailService.getTemplateById(templateId);
        String subject = emailTemplateDto.getSubject();
        String body = emailTemplateDto.getBody();
        EmailSettingDto emailSettingDto = employerService.findEmployerEmailSetting(challengeDto.getAuthorEmail());
        // Process email subject
        subject = processEmailVariables(challengeDto, registrant, emailSettingDto, subject, emailTemplateDto.getSubjectVariables());
        // Process email body
        body = processEmailVariables(challengeDto, registrant, emailSettingDto, body, emailTemplateDto.getBodyVariables());

        emailContent.setSubject(subject);
        emailContent.setContent(body);
        try {
            emailContent.setReplyTo(InternetAddress.parse(emailSettingDto.getReplyEmail()));
        } catch (AddressException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    private String processEmailVariables(ChallengeDto challengeDto, ChallengeRegistrantEntity registrant,
                                         EmailSettingDto emailSettingDto, String replacementCandidate, List<String> variables) {
        String result = replacementCandidate;

        for (String variable : variables) {
            if (EmailService.VAR_CONTEST_NAME.equals(variable)) {
                result = result.replace(EmailService.VAR_CONTEST_NAME, challengeDto.getChallengeName());
            }
            if (EmailService.VAR_CONTEST_FIRST_PRIZE.equals(variable)) {
                String formatPrize = currencyService.formatCurrency(Double.valueOf(challengeDto.getFirstPlaceReward()), Locale.US);
                result = result.replace(EmailService.VAR_CONTEST_FIRST_PRIZE, formatPrize);
            }
            if (EmailService.VAR_CONTESTANT_FIRST_NAME.equals(variable)) {
                result = result.replace(EmailService.VAR_CONTESTANT_FIRST_NAME, registrant.getRegistrantFirstName());
            }
            if (EmailService.VAR_MAIL_SIGNATURE.equals(variable)) {
                result = result.replace(EmailService.VAR_MAIL_SIGNATURE, emailSettingDto.getEmailSignature());
            }
            if (EmailService.VAR_CONTEST_SECOND_PRIZE.equals(variable) && challengeDto.getSecondPlaceReward() != null) {
                String formatPrize = currencyService.formatCurrency(Double.valueOf(challengeDto.getSecondPlaceReward()), Locale.US);
                formatPrize = StringUtils.isNotEmpty(formatPrize) ? formatPrize : "";
                result = result.replace(EmailService.VAR_CONTEST_SECOND_PRIZE, formatPrize);
            }
            if (EmailService.VAR_CONTEST_THIRD_PRIZE.equals(variable) && challengeDto.getThirdPlaceReward() != null) {
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

            if (EmailService.VAR_FIRST_WINNER_FIRST_NAME.equals(variable)
                    || EmailService.VAR_FIRST_WINNER_FULL_NAME.equals(variable)) {
                if (firstWinner.isPresent()) {
                    String firstName = firstWinner.get().getRegistrantFirstName();
                    String lastName = firstWinner.get().getRegistrantLastName();
                    result = result.replace(EmailService.VAR_FIRST_WINNER_FIRST_NAME, firstName);
                    result = result.replace(EmailService.VAR_FIRST_WINNER_FULL_NAME, firstName + " " + lastName);
                }
            }

            if (EmailService.VAR_SECOND_WINNER_FIRST_NAME.equals(variable)
                    || EmailService.VAR_SECOND_WINNER_FULL_NAME.equals(variable)) {
                if (secondWinner.isPresent()) {
                    String firstName = secondWinner.get().getRegistrantFirstName();
                    String lastName = secondWinner.get().getRegistrantLastName();
                    result = result.replace(EmailService.VAR_SECOND_WINNER_FIRST_NAME, firstName);
                    result = result.replace(EmailService.VAR_SECOND_WINNER_FULL_NAME, firstName + " " + lastName);
                }
            }

            if (EmailService.VAR_THIRD_WINNER_FIRST_NAME.equals(variable)
                    || EmailService.VAR_THIRD_WINNER_FULL_NAME.equals(variable)) {
                if (thirdWinner.isPresent()) {
                    String firstName = thirdWinner.get().getRegistrantFirstName();
                    String lastName = thirdWinner.get().getRegistrantLastName();
                    result = result.replace(EmailService.VAR_THIRD_WINNER_FIRST_NAME, firstName);
                    result = result.replace(EmailService.VAR_THIRD_WINNER_FULL_NAME, firstName + " " + lastName);
                }
            }
        }

        return result;
    }

    public boolean sendEmailToRegistrant(String challengeOwner, Long registrantId, EmailContent emailContent) {
        boolean result = false;
        ChallengeRegistrantEntity registrant = challengeRegistrantRepository.findOne(registrantId);
        if (isOwnerOfChallenge(challengeOwner, registrant.getChallengeId())) {
//            ChallengeRegistrantEntity registrant = challengeRegistrantRepository.findOne(registrantId);
            String csvEmails = registrant.getRegistrantEmail();
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
    public List<ChallengeSubmissionDto> findChallengeSubmissionByRegistrant(Long challengeId, Long registrantId) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("challengeSubmission");
        BoolFilterBuilder boolFilterBuilder = new BoolFilterBuilder();
        boolFilterBuilder.must(termFilter("challengeId", challengeId));
        boolFilterBuilder.must(termFilter("registrantId", registrantId));

        searchQueryBuilder.withQuery(filteredQuery(matchAllQuery(), boolFilterBuilder));
        List<ChallengeSubmissionEntity> submissions = DataUtils.getAllEntities(challengeSubmissionRepository, searchQueryBuilder);
        return submissions.stream().map(submission -> dozerMapper.map(submission, ChallengeSubmissionDto.class)).collect(toList());
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

    public Set<Long> findRegistrantByChallengeSubmissionDate(Long challengeId, String fromDate, String toDate) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("challengeSubmission");
        BoolQueryBuilder boolQueryBuilder = boolQuery();
        boolQueryBuilder.must(termQuery("challengeId", challengeId));

        RangeQueryBuilder submissionDateQuery = QueryBuilders.rangeQuery("submissionDateTime");
        if (StringUtils.isNotEmpty(fromDate) || StringUtils.isNotEmpty(toDate)) {
            if (StringUtils.isNotEmpty(fromDate)) {
                submissionDateQuery.from(fromDate);
            }

            if (StringUtils.isNotEmpty(toDate)) {
                submissionDateQuery.to(toDate);
            }

            boolQueryBuilder.must(submissionDateQuery);
        }

        searchQueryBuilder.withQuery(boolQueryBuilder);
        List<ChallengeSubmissionEntity> submissions = DataUtils.getAllEntities(challengeSubmissionRepository, searchQueryBuilder);
        return submissions.stream().map(submission -> submission.getRegistrantId()).collect(toSet());
    }

    public ChallengeDetailDto getChallengeDetail(Long challengeId, String loginEmail) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("challenge");
        TermQueryBuilder challengeIdQuery = termQuery("challengeId", challengeId);
        //MatchQueryBuilder authorEmailQuery = matchQuery("authorEmail", loginEmail).minimumShouldMatch("100%");
        TermQueryBuilder expiredChallengeQuery = termQuery("expired", Boolean.TRUE);

        searchQueryBuilder.withQuery(boolQuery().must(challengeIdQuery).mustNot(expiredChallengeQuery));
        List<ChallengeEntity> challengeEntities = DataUtils.getAllEntities(challengeRepository, searchQueryBuilder);

        if (!challengeEntities.isEmpty()) {
            ChallengeEntity challengeEntity = challengeEntities.get(0);
            ChallengeDetailDto challengeDetailDto = dozerMapper.map(challengeEntity, ChallengeDetailDto.class);
            challengeDetailDto.setNumberOfRegistrants(getNumberOfRegistrants(challengeId));
            calculateChallengePhases(challengeDetailDto);
            boolean isAuthor = challengeEntity.getAuthorEmail().equals(loginEmail);
            challengeDetailDto.setIsAuthor(isAuthor);
            if (!isAuthor) {
                challengeDetailDto.setCriteria(null);
            }
            challengeDetailDto.setPhaseItems(this.getChallengeRegistrantFunnel(challengeId, loginEmail));
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
            challengeDetailDto.setCurrentPhase(ChallengePhaseEnum.FINAL);
            challengeDetailDto.setNextPhase(ChallengePhaseEnum.FINAL);
        } else {
            challengeDetailDto.setCurrentPhase(CHALLENGE_TIMELINE[currentIndex]);
            challengeDetailDto.setNextPhase(CHALLENGE_TIMELINE[nextIndex > -1 ? nextIndex : currentIndex]);
        }
    }

    public ChallengeRegistrantDto acceptRegistrant(String ownerEmail, Long registrantId) {
        ChallengeRegistrantEntity registrant = challengeRegistrantRepository.findOne(registrantId);
        if (registrant == null) {
            return null;
        }

        ChallengeEntity challenge = challengeRepository.findOne(registrant.getChallengeId());
        if (!ownerEmail.equalsIgnoreCase(challenge.getAuthorEmail())) {
            return null;
        }

        ChallengeDetailDto challengeDetailDto = dozerMapper.map(challenge, ChallengeDetailDto.class);
        calculateChallengePhases(challengeDetailDto);
        ChallengePhaseEnum activePhase = challengeDetailDto.getNextPhase();
        if (activePhase != registrant.getActivePhase()) {
            registrant.setActivePhase(activePhase);
            registrant = challengeRegistrantRepository.save(registrant);
        }

        return dozerMapper.map(registrant, ChallengeRegistrantDto.class);
    }

    @Override
    public List<ChallengeRegistrantFunnelItem> getChallengeRegistrantFunnel(Long challengeId, String ownerEmail) {
        List<ChallengeRegistrantFunnelItem> funnel = new ArrayList<>();
        ChallengeDto challengeDto = findChallengeById(challengeId, ownerEmail);
        Map<ChallengePhaseEnum, ChallengeRegistrantPhaseItem> numberOfRegistrantsByPhase =
                challengeRegistrantService.countNumberOfRegistrantsByPhase(challengeId);
        Map<ChallengePhaseEnum, ChallengeSubmissionPhaseItem> numberOfSubmissionsByPhase =
                challengeSubmissionService.countNumberOfSubmissionsByPhase(challengeId);

        for (Map.Entry<ChallengePhaseEnum, ChallengeRegistrantPhaseItem> entry : numberOfRegistrantsByPhase.entrySet()) {
            ChallengePhaseEnum phase = entry.getKey();
            Long participant = entry.getValue().getRegistration();
            Long submission = 0L;
            if (numberOfSubmissionsByPhase.get(phase) != null) {
                submission = numberOfSubmissionsByPhase.get(phase).getSubmission();
            }

            if (isValidPhase(challengeDto, phase)) {
                funnel.add(new ChallengeRegistrantFunnelItem(phase, participant, submission));
            }
        }

        Long numberOfWinners = challengeRegistrantService.countNumberOfWinners(challengeId);
        funnel.add(new ChallengeRegistrantFunnelItem(ChallengePhaseEnum.WINNER, numberOfWinners, numberOfWinners));

        Comparator<ChallengeRegistrantFunnelItem> sortByPhaseComparator = (item1, item2) ->
                item1.getPhase().getOrder() - item2.getPhase().getOrder();
        return funnel.stream().sorted(sortByPhaseComparator).collect(toList());
    }

    private boolean isValidPhase(ChallengeDto challengeDto, ChallengePhaseEnum phase) {
        switch (phase) {
            case REGISTRATION:
                return StringUtils.isNotEmpty(challengeDto.getRegistrationDate());
            case IDEA:
                return StringUtils.isNotEmpty(challengeDto.getIdeaSubmissionDate());
            case UIUX:
                return StringUtils.isNotEmpty(challengeDto.getUxSubmissionDate());
            case PROTOTYPE:
                return StringUtils.isNotEmpty(challengeDto.getPrototypeSubmissionDate());
            case FINAL:
                return StringUtils.isNotEmpty(challengeDto.getSubmissionDate());
            default:
                return false;
        }
    }
}
