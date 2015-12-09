package com.techlooper.service.impl;

import com.techlooper.dto.EmailTemplateDto;
import com.techlooper.entity.ChallengeRegistrantDto;
import com.techlooper.entity.EmailTemplateEntity;
import com.techlooper.model.EmailRequestModel;
import com.techlooper.model.Language;
import com.techlooper.model.RewardEnum;
import com.techlooper.repository.elasticsearch.EmailTemplateRepository;
import com.techlooper.service.ChallengeEmailService;
import com.techlooper.service.ChallengeRegistrantService;
import com.techlooper.service.EmailService;
import com.techlooper.util.DataUtils;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

import static java.util.stream.Collectors.joining;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

/**
 * Created by NguyenDangKhoa on 11/20/15.
 */
@Service
public class EmailServiceImpl implements EmailService {

    private final static Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    public static final String MAIL_SENDER_NAME = "TechLooper";

    @Resource
    private JavaMailSender mailSender;

    @Resource
    private Map<String, Template> mailTemplates;

    @Resource
    private EmailTemplateRepository emailTemplateRepository;

    @Resource
    private ChallengeRegistrantService challengeRegistrantService;

    @Resource
    private Mapper dozerMapper;

    @Value("${mail.techlooper.services}")
    private String serviceMailAddress;

    @Override
    public void sendMail(EmailRequestModel emailRequestModel) {
        MimeMessage mailMessage = emailRequestModel.getMailMessage();
        String templateName = emailRequestModel.getTemplateName();
        Language language = emailRequestModel.getLanguage() != null ? emailRequestModel.getLanguage() : Language.en;
        Map<String, Object> templateModel = emailRequestModel.getTemplateModel();

        EmailTemplateDto emailTemplateConfig = getTemplateByName(templateName, language);
        String mailSubject = emailTemplateConfig.getSubject();
        String localizedTemplateName = emailTemplateConfig.getTemplateName() + "_" + language.getValue();
        Template template = mailTemplates.get(localizedTemplateName);
        StringWriter stringWriter = new StringWriter();

        try {
            if (CollectionUtils.isNotEmpty(emailTemplateConfig.getReplyToAddresses())) {
                String replyToAddresses = emailTemplateConfig.getReplyToAddresses().stream().collect(joining(", "));
                mailMessage.setReplyTo(InternetAddress.parse(replyToAddresses));
            }

            if (CollectionUtils.isNotEmpty(emailTemplateConfig.getRecipientAddresses())) {
                String recipientAddresses = emailTemplateConfig.getRecipientAddresses().stream().collect(joining(", "));
                mailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientAddresses));
            } else {
                Address[] recipientAddresses = InternetAddress.parse(emailRequestModel.getRecipientAddresses());
                mailMessage.setRecipients(Message.RecipientType.TO, recipientAddresses);
            }

            if (StringUtils.isNotEmpty(emailTemplateConfig.getFromAddress())) {
                mailMessage.setFrom(new InternetAddress(emailTemplateConfig.getFromAddress(), MAIL_SENDER_NAME, "UTF-8"));
            } else {
                mailMessage.setFrom(new InternetAddress(serviceMailAddress, MAIL_SENDER_NAME, "UTF-8"));
            }

            if (CollectionUtils.isNotEmpty(emailRequestModel.getSubjectVariableValues())) {
                mailMessage.setSubject(String.format(mailSubject,
                        emailRequestModel.getSubjectVariableValues().toArray(new String[]{})));
            } else if (StringUtils.isNotEmpty(emailRequestModel.getSubject())) {
                mailMessage.setSubject(emailRequestModel.getSubject());
            } else {
                mailMessage.setSubject(mailSubject);
            }

            template.process(templateModel, stringWriter);
            mailMessage.setText(stringWriter.toString(), "UTF-8", "html");
            stringWriter.flush();
            mailMessage.saveChanges();
            mailSender.send(mailMessage);
        } catch (MessagingException | IOException | TemplateException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    @Override
    public EmailTemplateDto getTemplateById(Long templateId, Long challengeId) {
        EmailTemplateEntity emailTemplateEntity = emailTemplateRepository.findOne(templateId);
        if (emailTemplateEntity != null && emailTemplateEntity.getIsEnable()) {
            if (emailTemplateEntity.getTemplateId() == ChallengeEmailService.ANNOUNCE_WINNER_EMAIL_TEMPLATE_EN
                    || emailTemplateEntity.getTemplateId() == ChallengeEmailService.ANNOUNCE_WINNER_EMAIL_TEMPLATE_VI) {
                processWinnerAnnouncementEmailContent(challengeId, emailTemplateEntity);
            }
            return dozerMapper.map(emailTemplateEntity, EmailTemplateDto.class);
        }
        return null;
    }

    @Override
    public List<EmailTemplateDto> getAvailableEmailTemplates(Language language) {
        List<EmailTemplateDto> emailTemplateDtoList = new ArrayList<>();
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("emailTemplate");
        searchQueryBuilder.withQuery(boolQuery()
                .must(termQuery("isEnable", Boolean.TRUE))
                .must(termQuery("language", language)));
        List<EmailTemplateEntity> templateEntities = DataUtils.getAllEntities(emailTemplateRepository, searchQueryBuilder);

        for (EmailTemplateEntity emailTemplateEntity : templateEntities) {
            emailTemplateDtoList.add(dozerMapper.map(emailTemplateEntity, EmailTemplateDto.class));
        }
        return emailTemplateDtoList;
    }

    @Override
    public EmailTemplateDto getTemplateByName(String templateName, Language language) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("emailTemplate");
        searchQueryBuilder.withQuery(boolQuery().must(termQuery("templateName", templateName))
                .must(termQuery("language", language)));
        List<EmailTemplateEntity> templates = DataUtils.getAllEntities(emailTemplateRepository, searchQueryBuilder);
        return templates.isEmpty() ? null : dozerMapper.map(templates.get(0), EmailTemplateDto.class);
    }

    @Override
    public String getCitiBankPromotionTitle(String lang) {
        EmailTemplateEntity emailTemplateEntity = emailTemplateRepository.findOne(5L);
        return Language.en.getValue().equalsIgnoreCase(lang) ? emailTemplateEntity.getTitleEN() : emailTemplateEntity.getTitleVI();
    }

    private void processWinnerAnnouncementEmailContent(Long challengeId, EmailTemplateEntity emailTemplateEntity) {
        Set<ChallengeRegistrantDto> winners = challengeRegistrantService.
                findWinnerRegistrantsByChallengeId(challengeId);
        Optional<ChallengeRegistrantDto> firstWinner = winners.stream().filter(
                winner -> winner.getReward() == RewardEnum.FIRST_PLACE).findFirst();
        Optional<ChallengeRegistrantDto> secondWinner = winners.stream().filter(
                winner -> winner.getReward() == RewardEnum.SECOND_PLACE).findFirst();
        Optional<ChallengeRegistrantDto> thirdWinner = winners.stream().filter(
                winner -> winner.getReward() == RewardEnum.THIRD_PLACE).findFirst();

        if (!firstWinner.isPresent()) {
            emailTemplateEntity.setBody(emailTemplateEntity.getBody().replaceFirst("<li class='1st'>(.+?)</li>", ""));
        }

        if (!secondWinner.isPresent()) {
            emailTemplateEntity.setBody(emailTemplateEntity.getBody().replaceFirst("<li class='2nd'>(.+?)</li>", ""));
        }

        if (!thirdWinner.isPresent()) {
            emailTemplateEntity.setBody(emailTemplateEntity.getBody().replaceFirst("<li class='3rd'>(.+?)</li>", ""));
        }
    }

}
