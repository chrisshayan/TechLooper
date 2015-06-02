package com.techlooper.service;

import com.techlooper.entity.SalaryReview;
import com.techlooper.model.EmailRequest;
import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

/**
 * Created by NguyenDangKhoa on 5/18/15.
 */
public interface SalaryReviewService {

  List<SalaryReview> searchSalaryReview(SalaryReview salaryReview);

  void sendSalaryReviewReportEmail(EmailRequest emailRequest) throws IOException, TemplateException, MessagingException;
}
