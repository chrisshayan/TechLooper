package com.techlooper.service;

import com.techlooper.dto.EmailTemplateDto;
import com.techlooper.model.EmailRequestModel;
import com.techlooper.model.Language;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 11/21/15.
 */
public interface EmailService {

    void sendMail(EmailRequestModel emailRequestModel);

    EmailTemplateDto getTemplateById(Long templateId, Long challengeId);

    List<EmailTemplateDto> getAvailableEmailTemplates(Language language);

    EmailTemplateDto getTemplateByName(String templateName, Language language);

}
