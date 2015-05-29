package com.techlooper.service.impl;

import com.techlooper.entity.SalaryReview;
import com.techlooper.model.EmailRequest;
import com.techlooper.model.Language;
import com.techlooper.repository.elasticsearch.SalaryReviewRepository;
import com.techlooper.service.JobQueryBuilder;
import com.techlooper.service.SalaryReviewService;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.elasticsearch.index.query.FilterBuilders.*;
import static org.elasticsearch.index.query.QueryBuilders.filteredQuery;

/**
 * Created by NguyenDangKhoa on 5/18/15.
 */
@Service
public class SalaryReviewServiceImpl implements SalaryReviewService {

  public static final long MIN_SALARY_ACCEPTABLE = 250L;

  public static final long MAX_SALARY_ACCEPTABLE = 5000L;

  @Resource
  private SalaryReviewRepository salaryReviewRepository;

  @Resource
  private JobQueryBuilder jobQueryBuilder;

  @Value("${elasticsearch.index.name}")
  private String elasticSearchIndexName;

  @Resource
  private MimeMessage salaryReviewMailMessage;

  @Resource
  private JavaMailSender mailSender;

  @Resource
  private Template salaryReviewReportTemplateEn;

  @Resource
  private Template salaryReviewReportTemplateVi;

  @Override
  public List<SalaryReview> searchSalaryReview(SalaryReview salaryReview) {
    Calendar now = Calendar.getInstance();
    now.add(Calendar.MONTH, -6);
    QueryBuilder queryBuilder = filteredQuery(jobQueryBuilder.jobTitleQueryBuilder(salaryReview.getJobTitle()),
      boolFilter().must(termsFilter("jobCategories", salaryReview.getJobCategories()))
        .must(rangeFilter("netSalary").from(MIN_SALARY_ACCEPTABLE).to(MAX_SALARY_ACCEPTABLE))
        .must(rangeFilter("createdDateTime").from(now.getTimeInMillis())));

    List<SalaryReview> salaryReviews = new ArrayList<>();
    FacetedPage<SalaryReview> salaryReviewFirstPage = salaryReviewRepository.search(queryBuilder, new PageRequest(0, 100));
    salaryReviews.addAll(salaryReviewFirstPage.getContent());

    int totalPage = salaryReviewFirstPage.getTotalPages();
    int pageIndex = 1;
    while (pageIndex < totalPage) {
      salaryReviews.addAll(salaryReviewRepository.search(queryBuilder, new PageRequest(pageIndex, 100)).getContent());
      pageIndex++;
    }
    return salaryReviews;
  }

  public void sendReportEmail(EmailRequest emailRequest) throws IOException, TemplateException, MessagingException {
    SalaryReview salaryReview = salaryReviewRepository.findOne(emailRequest.getSalaryReviewId());
    salaryReviewMailMessage.setRecipients(Message.RecipientType.TO, emailRequest.getEmail());

    StringWriter stringWriter = new StringWriter();
    Template template = emailRequest.getLang() == Language.vi ? salaryReviewReportTemplateVi : salaryReviewReportTemplateEn;
    template.process(salaryReview, stringWriter);
    salaryReviewMailMessage.setText(stringWriter.toString(), "utf-8", "html");
    stringWriter.flush();

    mailSender.send(salaryReviewMailMessage);
  }
}
