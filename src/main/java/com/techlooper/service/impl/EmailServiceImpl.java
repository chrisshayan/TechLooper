package com.techlooper.service.impl;

import com.techlooper.dto.EmailTemplateDto;
import com.techlooper.entity.EmailTemplateEntity;
import com.techlooper.model.EmailContent;
import com.techlooper.model.Language;
import com.techlooper.repository.elasticsearch.EmailTemplateRepository;
import com.techlooper.service.EmailService;
import com.techlooper.util.DataUtils;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by phuonghqh on 10/1/15.
 */
@Service
public class EmailServiceImpl implements EmailService {

    private final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Resource
    private JavaMailSender mailSender;

    @Resource
    private MimeMessage fromTechlooperMailMessage;

    @Resource
    private Template challengeEmployerMailTemplateEn;

    @Resource
    private Template challengeEmployerMailTemplateVi;

    @Value("${web.baseUrl}")
    private String webBaseUrl;

    @Resource
    private EmailTemplateRepository emailTemplateRepository;

    @Resource
    private Mapper dozerMapper;

    public boolean sendEmail(EmailContent emailContent) {
        try {
            String subject = StringUtils.isNotEmpty(emailContent.getSubject()) ? emailContent.getSubject() : "[No Subject]";
            fromTechlooperMailMessage.setSubject(MimeUtility.encodeText(subject, "UTF-8", null));

            Template template = emailContent.getLanguage() == Language.vi ? challengeEmployerMailTemplateVi :
                    challengeEmployerMailTemplateEn;

            StringWriter stringWriter = new StringWriter();
            Map<String, Object> templateModel = new HashMap<>();
            templateModel.put("webBaseUrl", webBaseUrl);
            templateModel.put("emailContent", emailContent.getContent());
            template.process(templateModel, stringWriter);

            fromTechlooperMailMessage.setText(stringWriter.toString(), "UTF-8", "html");
            fromTechlooperMailMessage.setRecipients(Message.RecipientType.TO, emailContent.getRecipients());
            fromTechlooperMailMessage.setReplyTo(emailContent.getReplyTo());
            fromTechlooperMailMessage.saveChanges();
            mailSender.send(fromTechlooperMailMessage);
        } catch (Exception e) {
            LOGGER.error("Can not send email", e);
            return false;
        }
        return true;
    }

    @Override
    public List<EmailTemplateDto> getAvailableEmailTemplates() {
        List<EmailTemplateDto> emailTemplateDtoList = new ArrayList<>();
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("emailTemplate");
        searchQueryBuilder.withQuery(QueryBuilders.termQuery("isEnable", Boolean.TRUE));
        List<EmailTemplateEntity> templateEntities = DataUtils.getAllEntities(emailTemplateRepository, searchQueryBuilder);

        for (EmailTemplateEntity emailTemplateEntity : templateEntities) {
            emailTemplateDtoList.add(dozerMapper.map(emailTemplateEntity, EmailTemplateDto.class));
        }
        return emailTemplateDtoList;
    }

    @Override
    public EmailTemplateDto getTemplateById(Long templateId) {
        EmailTemplateEntity emailTemplateEntity = emailTemplateRepository.findOne(templateId);
        if (emailTemplateEntity != null && emailTemplateEntity.getIsEnable()) {
            return dozerMapper.map(emailTemplateEntity, EmailTemplateDto.class);
        }
        return null;
    }
}
