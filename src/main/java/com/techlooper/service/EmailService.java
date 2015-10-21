package com.techlooper.service;

import com.techlooper.dto.EmailTemplateDto;
import com.techlooper.model.EmailContent;

import java.util.List;

/**
 * Created by phuonghqh on 10/1/15.
 */
public interface EmailService {

    boolean sendEmail(EmailContent emailContent);

    List<EmailTemplateDto> getAvailableEmailTemplates();
}
