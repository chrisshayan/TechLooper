package com.techlooper.service;

import com.techlooper.dto.EmailTemplateDto;
import com.techlooper.model.EmailContent;

import java.util.List;

/**
 * Created by phuonghqh on 10/1/15.
 */
public interface EmailService {

    final String VAR_CONTEST_NAME = "{contest_name}";

    final String VAR_CONTEST_FIRST_PRIZE = "{contest_1st_prize}";

    final String VAR_CONTESTANT_FIRST_NAME = "{contestant_first_name}";

    final String VAR_MAIL_SIGNATURE = "{mail_signature}";

    boolean sendEmail(EmailContent emailContent);

    List<EmailTemplateDto> getAvailableEmailTemplates();

    EmailTemplateDto getTemplateById(Long templateId);
}
