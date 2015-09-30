package com.techlooper.service.impl;

import com.techlooper.entity.ChallengeEntity;
import com.techlooper.entity.ChallengeRegistrantDto;
import com.techlooper.entity.ChallengeRegistrantEntity;
import com.techlooper.entity.ChallengeSubmissionEntity;
import com.techlooper.model.*;
import com.techlooper.repository.elasticsearch.ChallengeRegistrantRepository;
import com.techlooper.repository.elasticsearch.ChallengeRepository;
import com.techlooper.repository.elasticsearch.ChallengeSubmissionRepository;
import com.techlooper.service.ChallengeService;
import com.techlooper.util.DateTimeUtils;
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
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.FilterBuilders.*;
import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.elasticsearch.search.aggregations.AggregationBuilders.sum;
import static org.elasticsearch.search.sort.SortBuilders.fieldSort;

/**
 * Created by NguyenDangKhoa on 6/29/15.
 */
@Service
public class ChallengeServiceImpl implements ChallengeService {

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

    public ChallengeEntity savePostChallenge(ChallengeDto challengeDto) throws Exception {
        ChallengeEntity challengeEntity = dozerMapper.map(challengeDto, ChallengeEntity.class);
        if (challengeDto.getChallengeId() == null) {
            challengeEntity.setChallengeId(new Date().getTime());
        }
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
            numberOfDays = DateTimeUtils.daysBetween(DateTimeUtils.currentDate(), challengeEntity.getRegistrationDateTime()) + 1;
        } else if (challengePhase == ChallengePhaseEnum.IN_PROGRESS) {
            numberOfDays = DateTimeUtils.daysBetween(DateTimeUtils.currentDate(), challengeEntity.getSubmissionDateTime()) + 1;
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

    public ChallengeDetailDto getChallengeDetail(Long challengeId) {
        ChallengeEntity challengeEntity = challengeRepository.findOne(challengeId);
        if (challengeEntity != null && !Boolean.TRUE.equals(challengeEntity.getExpired())) {
            ChallengeDetailDto challengeDetailDto = dozerMapper.map(challengeEntity, ChallengeDetailDto.class);
            challengeDetailDto.setNumberOfRegistrants(getNumberOfRegistrants(challengeId));
            return challengeDetailDto;
        }
        return null;
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

    public long joinChallenge(ChallengeRegistrantDto challengeRegistrantDto) throws MessagingException, IOException, TemplateException {
        Long challengeId = challengeRegistrantDto.getChallengeId();
        boolean isExist = checkIfChallengeRegistrantExist(challengeId, challengeRegistrantDto.getRegistrantEmail());

        if (!isExist) {
            ChallengeRegistrantEntity challengeRegistrantEntity = dozerMapper.map(challengeRegistrantDto, ChallengeRegistrantEntity.class);
            ChallengeEntity challengeEntity = challengeRepository.findOne(challengeId);
            sendApplicationEmailToContestant(challengeEntity, challengeRegistrantEntity);
            sendApplicationEmailToEmployer(challengeEntity, challengeRegistrantEntity);
            challengeRegistrantEntity.setMailSent(Boolean.TRUE);
            challengeRegistrantEntity.setRegistrantId(new Date().getTime());
            challengeRegistrantRepository.save(challengeRegistrantEntity);
        }

        return getNumberOfRegistrants(challengeId);
    }

    public List<ChallengeDetailDto> listChallenges() {
//    List<ChallengeDetailDto> challenges = new ArrayList<>();
//    Iterator<ChallengeEntity> challengeIter = challengeRepository.findAll().iterator();
//    while (challengeIter.hasNext()) {
//      ChallengeEntity challengeEntity = challengeIter.next();
//      ChallengeDetailDto challengeDetailDto = dozerMapper.map(challengeEntity, ChallengeDetailDto.class);
//      challengeDetailDto.setNumberOfRegistrants(getNumberOfRegistrants(challengeEntity.getChallengeId()));
//      challenges.add(challengeDetailDto);
//    }
//    return sortChallengesByDescendingStartDate(challenges);
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
        mailSubject = String.format(mailSubject, challengeEntity.getAuthorEmail(), challengeEntity.getChallengeName());
        postChallengeMailMessage.setSubject(MimeUtility.encodeText(mailSubject, "UTF-8", null));
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
        }).collect(Collectors.toList());
    }

    public boolean checkIfChallengeRegistrantExist(Long challengeId, String email) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withQuery(boolQuery()
                .must(matchPhraseQuery("registrantEmail", email))
                .must(termQuery("challengeId", challengeId))
                .must(termQuery("mailSent", true)));

        long total = challengeRegistrantRepository.search(searchQueryBuilder.build()).getTotalElements();
        return (total > 0);
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
        MatchQueryBuilder authorEmailQuery = matchQuery("authorEmail", ownerEmail).minimumShouldMatch("100%");
        TermQueryBuilder notExpiredQuery = termQuery("expired", Boolean.TRUE);
        Iterable<ChallengeEntity> challenges = challengeRepository.search(boolQuery().must(authorEmailQuery).mustNot(notExpiredQuery));
        ArrayList<ChallengeDetailDto> dtos = new ArrayList<>();
        challenges.forEach(challengeEntity -> {
            ChallengeDetailDto challengeDetailDto = dozerMapper.map(challengeEntity, ChallengeDetailDto.class);
            challengeDetailDto.setNumberOfRegistrants(getNumberOfRegistrants(challengeEntity.getChallengeId()));
            dtos.add(challengeDetailDto);
        });
        dozerMapper.map(challenges, dtos);
        return dtos;
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

    public ChallengeDto findChallengeById(Long id) {
        return dozerMapper.map(challengeRepository.findOne(id), ChallengeDto.class);
    }

    public Set<ChallengeRegistrantDto> findRegistrantsByOwner(String ownerEmail, Long challengeId) {
        BoolQueryBuilder boolQueryBuilder = boolQuery();

        if (StringUtils.isNotEmpty(ownerEmail)) {
            boolQueryBuilder.must(matchQuery("authorEmail", ownerEmail).minimumShouldMatch("100%"));
        }

        TermQueryBuilder challengeQuery = termQuery("challengeId", challengeId);
        if (challengeId != null) {
            boolQueryBuilder.must(challengeQuery);
        }

        boolQueryBuilder.mustNot(termQuery("expired", Boolean.TRUE));
        Iterator<ChallengeEntity> challengeIterator = challengeRepository.search(boolQueryBuilder).iterator();
        Set<ChallengeRegistrantDto> registrantDtos = new HashSet<>();

        if (challengeIterator.hasNext()) {
            Iterator<ChallengeRegistrantEntity> registrants = challengeRegistrantRepository.search(challengeQuery).iterator();
            registrants.forEachRemaining(registrant -> registrantDtos.add(dozerMapper.map(registrant, ChallengeRegistrantDto.class)));
        }

        return registrantDtos;
    }

    public ChallengeRegistrantDto saveRegistrant(String ownerEmail, ChallengeRegistrantDto challengeRegistrantDto) {
        ChallengeEntity challenge = challengeRepository.findOne(challengeRegistrantDto.getChallengeId());
        if (ownerEmail.equalsIgnoreCase(challenge.getAuthorEmail())) {
            ChallengeRegistrantEntity registrant = challengeRegistrantRepository.findOne(challengeRegistrantDto.getRegistrantId());
            challengeRegistrantDto.setRegistrantEmail(registrant.getRegistrantEmail());
            dozerMapper.map(challengeRegistrantDto, registrant);
            registrant = challengeRegistrantRepository.save(registrant);
            challengeRegistrantDto = dozerMapper.map(registrant, ChallengeRegistrantDto.class);
//      challengeRegistrantDto.setRegistrantEmail(null);
        }
        return challengeRegistrantDto;
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
        searchQueryBuilder.withPageable(new PageRequest(0, 100));

        List<ChallengeRegistrantEntity> result = new ArrayList<>();
        Iterator<ChallengeRegistrantEntity> iterator = challengeRegistrantRepository.search(searchQueryBuilder.build()).iterator();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }

        return result;
    }

    @Override
    public List<ChallengeSubmissionEntity> findChallengeSubmissionWithinPeriod(
            Long challengeId, Long currentDateTime, TimePeriodEnum period) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("challengeSubmission");

        BoolQueryBuilder boolQueryBuilder = boolQuery();
        boolQueryBuilder.must(termQuery("challengeId", challengeId));

        Long pastTime = currentDateTime - period.getMiliseconds() > 0 ? currentDateTime - period.getMiliseconds() : 0;
        boolQueryBuilder.must(rangeQuery("challengeSubmissionId").from(pastTime));
        searchQueryBuilder.withQuery(boolQueryBuilder);
        searchQueryBuilder.withSort(fieldSort("challengeSubmissionId").order(SortOrder.DESC));
        searchQueryBuilder.withPageable(new PageRequest(0, 100));

        List<ChallengeSubmissionEntity> result = new ArrayList<>();
        Iterator<ChallengeSubmissionEntity> iterator = challengeSubmissionRepository.search(searchQueryBuilder.build()).iterator();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }

        return result;
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
        templateModel.put("challengeEntity", challengeEntity);

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

}
