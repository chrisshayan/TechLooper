package com.techlooper.service.impl;

import com.techlooper.entity.ChallengeEntity;
import com.techlooper.entity.ChallengeRegistrantDto;
import com.techlooper.entity.ChallengeRegistrantEntity;
import com.techlooper.model.ChallengeDetailDto;
import com.techlooper.model.ChallengeDto;
import com.techlooper.model.Language;
import com.techlooper.repository.elasticsearch.ChallengeRegistrantRepository;
import com.techlooper.repository.elasticsearch.ChallengeRepository;
import com.techlooper.service.ChallengeService;
import com.techlooper.service.EmployerService;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.sum.SumBuilder;
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

import static org.elasticsearch.search.aggregations.AggregationBuilders.sum;

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
  private Template postChallengeMailTemplateVi;

  @Value("${mail.postChallenge.subject.vn}")
  private String postChallengeMailSubjectVn;

  @Value("${mail.postChallenge.subject.en}")
  private String postChallengeMailSubjectEn;

  @Value("${mail.postChallenge.techloopies.mailSubject}")
  private String postChallengeTechloopiesMailSubject;

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
  private Mapper dozerMapper;

  @Value("${elasticsearch.userimport.index.name}")
  private String techlooperIndex;

  @Resource
  private EmployerService employerService;

  @Override
  public ChallengeEntity savePostChallenge(ChallengeDto challengeDto) throws Exception {
    ChallengeEntity challengeEntity = dozerMapper.map(challengeDto, ChallengeEntity.class);
    challengeEntity.setChallengeId(new Date().getTime());
    challengeEntity.setStartDateTime(challengeDto.getStartDate());
    challengeEntity.setRegistrationDateTime(challengeDto.getRegistrationDate());
    challengeEntity.setSubmissionDateTime(challengeDto.getSubmissionDate());
    return challengeRepository.save(challengeEntity);
  }

  @Override
  public void sendPostChallengeEmailToEmployer(ChallengeEntity challengeEntity)
    throws MessagingException, IOException, TemplateException {
    String mailSubject = challengeEntity.getLang() == Language.vi ? postChallengeMailSubjectVn : postChallengeMailSubjectEn;
    Address[] recipientAddresses = getRecipientAddresses(challengeEntity, true);
    Template template = challengeEntity.getLang() == Language.vi ? postChallengeMailTemplateVi : postChallengeMailTemplateEn;
    sendPostChallengeEmail(challengeEntity, mailSubject, recipientAddresses, template);
  }

  @Override
  public void sendPostChallengeEmailToTechloopies(ChallengeEntity challengeEntity)
    throws MessagingException, IOException, TemplateException {
    String mailSubject = postChallengeTechloopiesMailSubject;
    Address[] recipientAddresses = InternetAddress.parse(postChallengeTechloopiesMailList);
    sendPostChallengeEmail(challengeEntity, mailSubject, recipientAddresses, postChallengeMailTemplateEn);
  }

  @Override
  public ChallengeDetailDto getChallengeDetail(Long challengeId) {
    ChallengeEntity challengeEntity = challengeRepository.findOne(challengeId);
    if (challengeEntity != null) {
      ChallengeDetailDto challengeDetailDto = dozerMapper.map(challengeEntity, ChallengeDetailDto.class);
      challengeDetailDto.setNumberOfRegistrants(getNumberOfRegistrants(challengeId));
      return challengeDetailDto;
    }
    return null;
  }

  @Override
  public Long getNumberOfRegistrants(Long challengeId) {
    NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withSearchType(SearchType.COUNT);
    searchQueryBuilder.withFilter(FilterBuilders.termFilter("challengeId", challengeId));
    return challengeRegistrantRepository.search(searchQueryBuilder.build()).getTotalElements();
  }

  @Override
  public void sendApplicationEmailToContestant(ChallengeEntity challengeEntity, ChallengeRegistrantEntity challengeRegistrantEntity) throws MessagingException, IOException, TemplateException {
    Template template = challengeRegistrantEntity.getLang() == Language.vi ?
      confirmUserJoinChallengeMailTemplateVi : confirmUserJoinChallengeMailTemplateEn;
    String mailSubject = challengeRegistrantEntity.getLang() == Language.vi ?
      confirmUserJoinChallengeMailSubjectVn : confirmUserJoinChallengeMailSubjectEn;
    mailSubject = String.format(mailSubject, challengeEntity.getChallengeName());
    Address[] emailAddress = InternetAddress.parse(challengeRegistrantEntity.getRegistrantEmail());
    sendContestApplicationEmail(template, mailSubject, emailAddress, challengeEntity, challengeRegistrantEntity, false);
  }

  @Override
  public void sendApplicationEmailToEmployer(ChallengeEntity challengeEntity, ChallengeRegistrantEntity challengeRegistrantEntity) throws MessagingException, IOException, TemplateException {
    Template template = challengeRegistrantEntity.getLang() == Language.vi ?
      alertEmployerChallengeMailTemplateVi : alertEmployerChallengeMailTemplateEn;
    String mailSubject = challengeRegistrantEntity.getLang() == Language.vi ?
      alertEmployerChallengeMailSubjectVn : alertEmployerChallengeMailSubjectEn;
    mailSubject = String.format(mailSubject, challengeEntity.getChallengeName());
    Address[] emailAddress = getRecipientAddresses(challengeEntity, false);
    sendContestApplicationEmail(template, mailSubject, emailAddress, challengeEntity, challengeRegistrantEntity, true);
  }

  @Override
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

  @Override
  public List<ChallengeDetailDto> listChallenges() {
    List<ChallengeDetailDto> challenges = new ArrayList<>();
    Iterator<ChallengeEntity> challengeIter = challengeRepository.findAll().iterator();
    while (challengeIter.hasNext()) {
      ChallengeEntity challengeEntity = challengeIter.next();
      ChallengeDetailDto challengeDetailDto = dozerMapper.map(challengeEntity, ChallengeDetailDto.class);
      challengeDetailDto.setNumberOfRegistrants(getNumberOfRegistrants(challengeEntity.getChallengeId()));
      challenges.add(challengeDetailDto);
    }
    return sortChallengesByDescendingStartDate(challenges);
  }

  private void sendContestApplicationEmail(Template template, String mailSubject, Address[] recipientAddresses,
                                           ChallengeEntity challengeEntity, ChallengeRegistrantEntity challengeRegistrantEntity, boolean hasReplyTo)
    throws MessagingException, IOException, TemplateException {
    postChallengeMailMessage.setRecipients(Message.RecipientType.TO, recipientAddresses);

    if (hasReplyTo) {
      postChallengeMailMessage.setReplyTo(InternetAddress.parse(challengeRegistrantEntity.getRegistrantEmail()));
    }
    else {
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
        }
        else if (challenge1.getStartDateTime() == null) {
          return 1;
        }
        long challenge2StartDate = sdf.parse(challenge2.getStartDateTime()).getTime();
        long challenge1StartDate = sdf.parse(challenge1.getStartDateTime()).getTime();
        if (challenge2StartDate - challenge1StartDate > 0) {
          return 1;
        }
        else if (challenge2StartDate - challenge1StartDate < 0) {
          return -1;
        }
        else {
          return 0;
        }
      }
      catch (ParseException e) {
        return 0;
      }
    }).collect(Collectors.toList());
  }

  public boolean checkIfChallengeRegistrantExist(Long challengeId, String email) {
    NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
    searchQueryBuilder.withQuery(QueryBuilders.boolQuery()
      .must(QueryBuilders.matchPhraseQuery("registrantEmail", email))
      .must(QueryBuilders.termQuery("challengeId", challengeId))
      .must(QueryBuilders.termQuery("mailSent", true)));

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
    searchQueryBuilder.withQuery(QueryBuilders.matchAllQuery());

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
    QueryStringQueryBuilder query = QueryBuilders.queryStringQuery(owner).defaultField("authorEmail");
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
    QueryStringQueryBuilder query = QueryBuilders.queryStringQuery(ownerEmail).defaultField("authorEmail");
    Iterable<ChallengeEntity> challenges = challengeRepository.search(query);
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

//    public Collection<ChallengeRegistrantDto> findRegistrantsByChallengeId(Long challengeId) {
//        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder().withIndices(techlooperIndex).withTypes("challengeRegistrant");
//        queryBuilder.withFilter(FilterBuilders.queryFilter(QueryBuilders.termQuery("challengeId", challengeId)));
//
//        int pageIndex = 0;
//        Set<ChallengeRegistrantDto> registrants = new HashSet<>();
//        while (true) {
//            queryBuilder.withPageable(new PageRequest(pageIndex++, 100));
//            FacetedPage<ChallengeRegistrantEntity> page = challengeRegistrantRepository.search(queryBuilder.build());
//            if (!page.hasContent()) {
//                break;
//            }
//
//            page.spliterator().forEachRemaining(registrant -> registrants.add(dozerMapper.map(registrant, ChallengeRegistrantDto.class)));
//        }
//
//        return registrants;
//    }

  public Long countRegistrantsByChallengeId(Long challengeId) {
    NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder().withIndices(techlooperIndex).withTypes("challengeRegistrant");
    queryBuilder.withFilter(FilterBuilders.queryFilter(QueryBuilders.termQuery("challengeId", challengeId)))
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

}
