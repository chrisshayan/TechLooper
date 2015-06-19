package com.techlooper.service;

import com.techlooper.entity.GetPromotedEntity;
import com.techlooper.entity.SalaryReviewEntity;
import com.techlooper.model.*;
import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

/**
 * Created by NguyenDangKhoa on 5/18/15.
 */
public interface SalaryReviewService {

    List<SalaryReviewEntity> searchSalaryReview(SalaryReviewEntity salaryReviewEntity);

    void sendSalaryReviewReportEmail(EmailRequest emailRequest) throws IOException, TemplateException, MessagingException;

    void createVnwJobAlert(VnwJobAlertRequest vnwJobAlertRequest);

    void sendTopDemandedSkillsEmail(long getPromotedId, GetPromotedEmailRequest emailRequest) throws MessagingException, IOException, TemplateException;

    long saveGetPromotedInformation(GetPromotedEmailRequest getPromotedEmailRequest);

    GetPromotedEntity getPromotedEntity(Long id);

    List<SimilarSalaryReview> getSimilarSalaryReview(SimilarSalaryReviewRequest request);

    long saveGetPromotedSurvey(GetPromotedSurveyRequest getPromotedSurveyRequest);
}
