package com.techlooper.service.impl;

import com.techlooper.model.EmailContent;
import com.techlooper.model.Language;
import com.techlooper.service.EmailService;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.StringWriter;
import java.util.HashMap;
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

    public boolean sendEmail(EmailContent emailContent) {
        try {
            String subject = StringUtils.isNotEmpty(emailContent.getSubject()) ? emailContent.getSubject() : "[No Subject]";
            fromTechlooperMailMessage.setSubject(MimeUtility.encodeText(subject, "UTF-8", null));

            Template template = emailContent.getLanguage() == Language.vi ? challengeEmployerMailTemplateVi :
                    challengeEmployerMailTemplateEn;

            StringWriter stringWriter = new StringWriter();
            Map<String, Object> templateModel = new HashMap<>();
            templateModel.put("emailContent", emailContent.getContent());
            template.process(templateModel, stringWriter);

            fromTechlooperMailMessage.setText(stringWriter.toString(), "UTF-8", "html");
            fromTechlooperMailMessage.setRecipients(Message.RecipientType.TO, emailContent.getRecipients());
            fromTechlooperMailMessage.saveChanges();
            mailSender.send(fromTechlooperMailMessage);
        } catch (Exception e) {
            LOGGER.error("Can not send email", e);
            return false;
        }
        return true;
    }
}
