package com.techlooper.service.impl;

import com.techlooper.entity.SalaryReviewEntity;
import com.techlooper.model.CitibankCreditCardPromotion;
import com.techlooper.repository.elasticsearch.SalaryReviewRepository;
import com.techlooper.service.CurrencyService;
import com.techlooper.service.PromotionService;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.StringWriter;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

/**
 * Created by phuonghqh on 5/27/15.
 */
@Service
public class PromotionServiceImpl implements PromotionService {

  @Resource
  private JavaMailSender mailSender;

  @Resource
  private SimpleMailMessage citibankCreditCardPromotionMailMessage;

  @Resource
  private Template citibankCreditCardPromotionTemplate;

  @Resource
  private SalaryReviewRepository salaryReviewRepository;//USD_VND

  @Resource
  private Map<Long, String> locationMap;

  @Resource
  private CurrencyService currencyService;

  public void placePromotion(CitibankCreditCardPromotion citibankCreditCardPromotion) throws IOException, TemplateException {
    SalaryReviewEntity salaryReviewEntity = salaryReviewRepository.findOne(citibankCreditCardPromotion.getSalaryReviewId());

    Long netIncome = currencyService.usdToVndRate() * salaryReviewEntity.getNetSalary();
    citibankCreditCardPromotion.setNetIncome("VND " + NumberFormat.getNumberInstance(Locale.US).format(netIncome));

    String location = locationMap.get(salaryReviewEntity.getLocationId());
    citibankCreditCardPromotion.setLocation(location);
    StringWriter stringWriter = new StringWriter();
    citibankCreditCardPromotionTemplate.process(citibankCreditCardPromotion, stringWriter);
    stringWriter.flush();
    citibankCreditCardPromotionMailMessage.setText(stringWriter.toString());
    mailSender.send(citibankCreditCardPromotionMailMessage);
  }
}
