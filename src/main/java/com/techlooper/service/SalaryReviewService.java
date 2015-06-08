package com.techlooper.service;

import com.techlooper.entity.SalaryReviewEntity;
import com.techlooper.model.EmailRequest;
import com.techlooper.model.VnwJobAlertRequest;
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
}
