package com.techlooper.service.impl;

import com.techlooper.entity.ChallengeEntity;
import com.techlooper.model.ChallengeDto;
import com.techlooper.model.Language;
import com.techlooper.repository.elasticsearch.ChallengeRepository;
import com.techlooper.service.ChallengeService;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Value;
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
import java.text.SimpleDateFormat;
import java.util.*;

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
    private JavaMailSender mailSender;

    @Resource
    private ChallengeRepository challengeRepository;

    @Resource
    private Mapper dozerMapper;

    @Override
    public ChallengeEntity savePostChallenge(ChallengeDto challengeDto) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        ChallengeEntity challengeEntity = dozerMapper.map(challengeDto, ChallengeEntity.class);
        challengeEntity.setChallengeId(new Date().getTime());
        challengeEntity.setStartDateTime(formatter.parse(challengeDto.getStartDate()));
        challengeEntity.setRegistrationDateTime(formatter.parse(challengeDto.getRegistrationDate()));
        challengeEntity.setSubmissionDateTime(formatter.parse(challengeDto.getSubmissionDate()));
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
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        templateModel.put("startDate", formatter.format(challengeEntity.getStartDateTime()));
        templateModel.put("registrationDate", formatter.format(challengeEntity.getRegistrationDateTime()));
        templateModel.put("submissionDate", formatter.format(challengeEntity.getSubmissionDateTime()));
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

}
