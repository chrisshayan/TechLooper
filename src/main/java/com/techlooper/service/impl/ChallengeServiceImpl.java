package com.techlooper.service.impl;

import com.techlooper.entity.ChallengeEntity;
import com.techlooper.model.Language;
import com.techlooper.service.ChallengeService;
import freemarker.template.SimpleDate;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by NguyenDangKhoa on 6/29/15.
 */
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

    @Value("${web.baseUrl}")
    private String webBaseUrl;

    @Resource
    private JavaMailSender mailSender;

    @Override
    public void sendPostChallengeEmailToEmployer(ChallengeEntity challengeEntity, String mailSubject)
            throws MessagingException, IOException, TemplateException {
        mailSubject = challengeEntity.getLang() == Language.vi ? postChallengeMailSubjectVn : postChallengeMailSubjectEn;
        sendPostChallengeEmail(challengeEntity, mailSubject);
    }

    @Override
    public void sendPostChallengeEmailToTechloopies(ChallengeEntity challengeEntity, String mailSubject)
            throws MessagingException, IOException, TemplateException {
        mailSubject = "A new challenge has been created";
        sendPostChallengeEmail(challengeEntity, mailSubject);
    }

    private void sendPostChallengeEmail(ChallengeEntity challengeEntity, String mailSubject) throws MessagingException, IOException, TemplateException {
        Address[] recipientAddresses = getRecipientAddresses(challengeEntity);
        postChallengeMailMessage.setRecipients(Message.RecipientType.TO, recipientAddresses);
        StringWriter stringWriter = new StringWriter();
        Template template = challengeEntity.getLang() == Language.vi ? postChallengeMailTemplateVi : postChallengeMailTemplateEn;

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("webBaseUrl", webBaseUrl);
        templateModel.put("challengeName", challengeEntity.getChallengeName());
        templateModel.put("businessRequirement", challengeEntity.getBusinessRequirement());
        templateModel.put("generalNote", challengeEntity.getGeneralNote());
        templateModel.put("technologies", StringUtils.join(challengeEntity.getTechnologies(), ", "));
        templateModel.put("document", challengeEntity.getDocument());
        templateModel.put("deliverables", challengeEntity.getDeliverables());
        templateModel.put("emails", StringUtils.join(challengeEntity.getEmails(), "<br/>"));
        templateModel.put("reviewStyle", challengeEntity.getReviewStyle());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        templateModel.put("dateChallengeStart", formatter.format(challengeEntity.getDateChallengeStart()));
        templateModel.put("dateChallengeRegister", formatter.format(challengeEntity.getDateChallengeRegister()));
        templateModel.put("dateChallengeSubmit", formatter.format(challengeEntity.getDateChallengeSubmit()));
        templateModel.put("quality", challengeEntity.getQuality());
        templateModel.put("firstReward", challengeEntity.getFirstReward());
        templateModel.put("secondReward", challengeEntity.getSecondReward());
        templateModel.put("thirdReward", challengeEntity.getThirdReward());
        templateModel.put("challengeId", challengeEntity.getChallengeId());
        templateModel.put("authorEmail", challengeEntity.getAuthorEmail());

        template.process(templateModel, stringWriter);
        mailSubject = String.format(mailSubject, challengeEntity.getAuthorEmail(), challengeEntity.getChallengeName());
        postChallengeMailMessage.setSubject(MimeUtility.encodeText(mailSubject, "UTF-8", null));
        postChallengeMailMessage.setText(stringWriter.toString(), "UTF-8", "html");

        stringWriter.flush();
        mailSender.send(postChallengeMailMessage);
    }

    private Address[] getRecipientAddresses(ChallengeEntity challengeEntity) throws AddressException {
        Set<String> emails = new HashSet<>(challengeEntity.getEmails());
        emails.add(challengeEntity.getAuthorEmail());
        return InternetAddress.parse(StringUtils.join(emails, ','));
    }

}
