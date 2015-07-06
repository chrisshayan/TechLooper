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
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.FilterBuilders;
import org.springframework.beans.factory.annotation.Value;
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

/**
 * Created by NguyenDangKhoa on 6/29/15.
 */
@Service
public class ChallengeServiceImpl implements ChallengeService {

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

    @Resource
    private JavaMailSender mailSender;

    @Resource
    private ChallengeRepository challengeRepository;

    @Resource
    private ChallengeRegistrantRepository challengeRegistrantRepository;

    @Resource
    private Mapper dozerMapper;

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
        Address[] recipientAddresses = getRecipientAddresses(challengeEntity);
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
        Template template = challengeEntity.getLang() == Language.vi ?
                confirmUserJoinChallengeMailTemplateVi : confirmUserJoinChallengeMailTemplateEn;
        String mailSubject = challengeEntity.getLang() == Language.vi ?
                confirmUserJoinChallengeMailSubjectVn : confirmUserJoinChallengeMailSubjectEn;
        mailSubject = String.format(mailSubject, challengeEntity.getChallengeName());
        sendContestApplicationEmail(template, mailSubject, challengeEntity, challengeRegistrantEntity);
    }

    @Override
    public void sendApplicationEmailToEmployer(ChallengeEntity challengeEntity, ChallengeRegistrantEntity challengeRegistrantEntity) throws MessagingException, IOException, TemplateException {
        Template template = challengeEntity.getLang() == Language.vi ?
                alertEmployerChallengeMailTemplateVi : alertEmployerChallengeMailTemplateEn;
        String mailSubject = challengeEntity.getLang() == Language.vi ?
                alertEmployerChallengeMailSubjectVn : alertEmployerChallengeMailSubjectEn;
        mailSubject = String.format(mailSubject, challengeEntity.getChallengeName());
        sendContestApplicationEmail(template, mailSubject, challengeEntity, challengeRegistrantEntity);
    }

    @Override
    public long joinChallenge(ChallengeRegistrantDto joinChallenge) {
        return 15L;
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

    private void sendContestApplicationEmail(Template template, String mailSubject, ChallengeEntity challengeEntity,
                                             ChallengeRegistrantEntity challengeRegistrantEntity) throws MessagingException, IOException, TemplateException {
        postChallengeMailMessage.setRecipients(Message.RecipientType.TO, challengeRegistrantEntity.getRegistrantEmail());
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

        template.process(templateModel, stringWriter);
        postChallengeMailMessage.setSubject(MimeUtility.encodeText(mailSubject, "UTF-8", null));
        postChallengeMailMessage.setText(stringWriter.toString(), "UTF-8", "html");

        stringWriter.flush();
        mailSender.send(postChallengeMailMessage);
    }

    private void sendPostChallengeEmail(ChallengeEntity challengeEntity, String mailSubject,
                                        Address[] recipientAddresses, Template template) throws MessagingException, IOException, TemplateException {
        postChallengeMailMessage.setRecipients(Message.RecipientType.TO, recipientAddresses);
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

        template.process(templateModel, stringWriter);
        mailSubject = String.format(mailSubject, challengeEntity.getAuthorEmail(), challengeEntity.getChallengeName());
        postChallengeMailMessage.setSubject(MimeUtility.encodeText(mailSubject, "UTF-8", null));
        postChallengeMailMessage.setText(stringWriter.toString(), "UTF-8", "html");

        stringWriter.flush();
        mailSender.send(postChallengeMailMessage);
    }

    private Address[] getRecipientAddresses(ChallengeEntity challengeEntity) throws AddressException {
        Set<String> emails = new HashSet<>(challengeEntity.getReceivedEmails());
        emails.add(challengeEntity.getAuthorEmail());
        return InternetAddress.parse(StringUtils.join(emails, ','));
    }

    private List<ChallengeDetailDto> sortChallengesByDescendingStartDate(List<ChallengeDetailDto> challenges) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return challenges.stream().sorted((challenge1, challenge2) -> {
            try {
                return (int) sdf.parse(challenge1.getStartDateTime()).getTime() -
                        (int) (sdf.parse(challenge2.getStartDateTime()).getTime());
            } catch (ParseException e) {
                return 0;
            }
        }).collect(Collectors.toList());
    }

}
