package com.techlooper.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.techlooper.entity.SalaryReview;
import com.techlooper.model.EmailRequest;
import com.techlooper.model.Language;
import com.techlooper.model.SalaryRange;
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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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

  @Value("${web.baseUrl}")
  private String webBaseUrl;

  @Resource
  private JsonNode vietnamworksConfiguration;

  @Value("${mail.salaryReview.subject.vn}")
  private String salaryReviewSubjectVn;

  @Value("${mail.salaryReview.subject.en}")
  private String salaryReviewSubjectEn;

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

  public void sendSalaryReviewReportEmail(EmailRequest emailRequest) throws IOException, TemplateException, MessagingException {
    salaryReviewMailMessage.setRecipients(Message.RecipientType.TO, emailRequest.getEmail());
    StringWriter stringWriter = new StringWriter();
    Template template = emailRequest.getLang() == Language.vi ? salaryReviewReportTemplateVi : salaryReviewReportTemplateEn;

    Map<String, Object> templateModel = new HashMap<>();

    SalaryReview salaryReview = salaryReviewRepository.findOne(emailRequest.getSalaryReviewId());
    templateModel.put("id", Base64.getEncoder().encodeToString(salaryReview.getCreatedDateTime().toString().getBytes()));
    templateModel.put("salaryReview", salaryReview);
    templateModel.put("webBaseUrl", webBaseUrl);

    String configLang = "lang_" + emailRequest.getLang().getValue();
    templateModel.put("jobLevel", vietnamworksConfiguration.findPath(salaryReview.getJobLevelIds().toString()).get(configLang).asText());
    templateModel.put("jobSkills", salaryReview.getSkills().stream().collect(Collectors.joining(" | ")));

    JsonNode categories = vietnamworksConfiguration.findPath("categories");
    List<String> categoryIds = categories.findValuesAsText("category_id");
    List<String> list = new ArrayList<>();
    salaryReview.getJobCategories().stream()
      .map(aLong -> aLong.toString())
      .forEach(jobCategory -> list.add(categories.get(categoryIds.indexOf(jobCategory)).get(configLang).asText()));
    templateModel.put("jobCategories", list.stream().collect(Collectors.joining(" | ")));

    JsonNode locations = vietnamworksConfiguration.findPath("locations");
    List<String> locationIds = vietnamworksConfiguration.findValuesAsText("location_id");
    templateModel.put("location", locations.get(locationIds.indexOf(salaryReview.getLocationId().toString())).get(configLang).asText());

    templateModel.put("date", new SimpleDateFormat("dd/MM/yyyy").format(new Date(salaryReview.getCreatedDateTime())));

    List<SalaryRange> lessSalaryRanges = new ArrayList<>();
    List<SalaryRange> moreSalaryRanges = new ArrayList<>();
    List<SalaryRange> salaryRanges = salaryReview.getSalaryReport().getSalaryRanges();
    salaryRanges.forEach(salaryRange -> {
      if (salaryRange.getPercent() > salaryReview.getSalaryReport().getPercentRank()) {
        moreSalaryRanges.add(salaryRange);
      }
      else if (salaryRange.getPercent() < salaryReview.getSalaryReport().getPercentRank()) {
        lessSalaryRanges.add(salaryRange);
      }
    });

    lessSalaryRanges.sort((sala, salb) -> salb.getPercent().compareTo(sala.getPercent()));
    moreSalaryRanges.sort((sala, salb) -> salb.getPercent().compareTo(sala.getPercent()));

    templateModel.put("lessSalaryRanges", lessSalaryRanges);
    templateModel.put("moreSalaryRanges", moreSalaryRanges);

    template.process(templateModel, stringWriter);

    salaryReviewMailMessage.setSubject(
      String.format(emailRequest.getLang().getValue().equalsIgnoreCase("vn") ? salaryReviewSubjectVn : salaryReviewSubjectEn, salaryReview.getJobTitle()));
    salaryReviewMailMessage.setText(stringWriter.toString(), "utf-8", "html");

    stringWriter.flush();

    mailSender.send(salaryReviewMailMessage);
  }

//  public static void main(String[] args) {
//    List<Integer> integers = Arrays.asList(1, 2, 4, 3, 4, 6, 5);
//    integers.sort((o1, o2) -> o1.compareTo(o2));
//    System.out.println(integers);
//
//    integers = Arrays.asList(1, 2, 4, 3, 4, 6, 5);
//    integers.sort((o1, o2) -> o2.compareTo(o1));
//    System.out.println(integers);
//  }
}
