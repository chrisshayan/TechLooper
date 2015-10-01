package com.techlooper.service.impl;

import com.techlooper.model.EmailContent;
import com.techlooper.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

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

  public boolean sendEmailWithBcc(EmailContent emailContent) {
    try {
      fromTechlooperMailMessage.setSubject(MimeUtility.encodeText(emailContent.getSubject(), "UTF-8", null));
      fromTechlooperMailMessage.setText(emailContent.getContent(), "UTF-8", "html");
      //InternetAddress.parse(StringUtils.join(emails, ','))
      fromTechlooperMailMessage.setRecipients(Message.RecipientType.BCC, emailContent.getBcc());
      fromTechlooperMailMessage.saveChanges();
      mailSender.send(fromTechlooperMailMessage);
    }
    catch (Exception e) {
      LOGGER.error("Can not send email", e);
      return false;
    }
    return true;
  }
}
