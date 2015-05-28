package com.techlooper.service.impl;

import com.techlooper.entity.SalaryReview;
import com.techlooper.model.CitibankCreditCardPromotion;
import com.techlooper.model.PaymentMethod;
import com.techlooper.repository.elasticsearch.SalaryReviewRepository;
import com.techlooper.service.CurrencyService;
import com.techlooper.service.PromotionService;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

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

  @Value("${citibank.cc_promotion.minIncome.vnd}")
  private Long minIncome;

  @Value("${citibank.accept.city.ids}")
  private String acceptCityIds;

  @Resource
  private Map<Long, String> locationMap;

  @Resource
  private CurrencyService currencyService;

  public boolean placePromotion(CitibankCreditCardPromotion citibankCreditCardPromotion) throws IOException, TemplateException {
    SalaryReview salaryReview = salaryReviewRepository.findOne(citibankCreditCardPromotion.getSalaryReviewId());
    Long netIncome = currencyService.usdToVndRate() * salaryReview.getNetSalary();
    if (citibankCreditCardPromotion.getAgree() != Boolean.TRUE ||
      citibankCreditCardPromotion.getPaymentMethod() != PaymentMethod.BANK_TRANSFER ||
      netIncome < minIncome ||
      !acceptCityIds.contains(salaryReview.getLocationId().toString())) {
      return false;
    }

    citibankCreditCardPromotion.setNetIncome("VND " + netIncome);

    String location = locationMap.get(salaryReview.getLocationId());
    citibankCreditCardPromotion.setLocation(location);
    StringWriter stringWriter = new StringWriter();
    citibankCreditCardPromotionTemplate.process(citibankCreditCardPromotion, stringWriter);
    citibankCreditCardPromotionMailMessage.setText(stringWriter.toString());
    mailSender.send(citibankCreditCardPromotionMailMessage);
    return true;
  }
}
