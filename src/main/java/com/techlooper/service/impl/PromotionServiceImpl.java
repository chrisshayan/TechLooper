package com.techlooper.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.techlooper.entity.SalaryReview;
import com.techlooper.model.CitibankCreditCardPromotion;
import com.techlooper.model.SalaryReviewSurvey;
import com.techlooper.repository.elasticsearch.SalaryReviewRepository;
import com.techlooper.service.PromotionService;
import com.techlooper.service.SalaryReviewService;
import com.techlooper.util.RestTemplateUtils;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;

/**
 * Created by phuonghqh on 5/27/15.
 */
@Service
public class PromotionServiceImpl implements PromotionService {

  @Resource
  private MailSender mailSender;

  @Resource
  private SimpleMailMessage citibankCreditCardPromotionMailMessage;

  @Resource
  private Template citibankCreditCardPromotionTemplate;

  @Resource
  private SalaryReviewRepository salaryReviewRepository;//USD_VND

  @Value("${api.exchangeRateUrl}")
  private String apiExchangeRateUrl;

  @Resource
  private RestTemplate restTemplate;

  @Value("${citibank.cc_promotion.minIncome}")
  private String minIncome;

  public void placePromotion(CitibankCreditCardPromotion citibankCreditCardPromotion) throws IOException, TemplateException {
    SalaryReview salaryReview = salaryReviewRepository.findOne(citibankCreditCardPromotion.getSalaryReviewId());
    String url = String.format(apiExchangeRateUrl, "USD_VND");
    BigDecimal rate = BigDecimal.valueOf(restTemplate.getForEntity(url, JsonNode.class).getBody().findPath("val").asDouble(0L));
    Money money = Money.parse("USD " + salaryReview.getNetSalary());
    Money minIncomeMoney = Money.parse(minIncome);

    StringWriter stringWriter = new StringWriter();
    citibankCreditCardPromotionTemplate.process(citibankCreditCardPromotion, stringWriter);
    System.out.println(stringWriter);
  }

  public static void main(String[] args) {
    JsonNode abc = new RestTemplate().getForEntity("http://www.freecurrencyconverterapi.com/api/v3/convert?q=USD_VND", JsonNode.class).getBody();
    System.out.println(abc.toString());
    System.out.println(abc.findPath("val").asDouble());
    System.out.println(abc.path("/results/USD_VND/val").asText());
  }
}
