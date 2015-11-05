package com.techlooper.service;

import com.techlooper.dto.EmailTemplateDto;
import com.techlooper.model.EmailContent;
import com.techlooper.model.Language;

import java.util.List;

/**
 * Created by phuonghqh on 10/1/15.
 */
public interface EmailService {

    final String VAR_CONTEST_NAME = "{contest_name}";

    final String VAR_CONTEST_FIRST_PRIZE = "{contest_1st_prize}";

    final String VAR_CONTESTANT_FIRST_NAME = "{contestant_first_name}";

    final String VAR_MAIL_SIGNATURE = "{mail_signature}";

    final String VAR_CONTEST_SECOND_PRIZE = "{contest_2nd_prize}";

    final String VAR_CONTEST_THIRD_PRIZE = "{contest_3rd_prize}";

    final String VAR_FIRST_WINNER_FIRST_NAME = "{1st_winner_firstname}";

    final String VAR_FIRST_WINNER_FULL_NAME = "{1st_winner_fullname}";

    final String VAR_SECOND_WINNER_FIRST_NAME = "{2nd_winner_firstname}";

    final String VAR_SECOND_WINNER_FULL_NAME = "{2nd_winner_fullname}";

    final String VAR_THIRD_WINNER_FIRST_NAME = "{3rd_winner_firstname}";

    final String VAR_THIRD_WINNER_FULL_NAME = "{3rd_winner_fullname}";

    boolean sendEmail(EmailContent emailContent);

    List<EmailTemplateDto> getAvailableEmailTemplates(Language language);

    EmailTemplateDto getTemplateById(Long templateId);
}
