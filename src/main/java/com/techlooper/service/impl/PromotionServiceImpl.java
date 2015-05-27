package com.techlooper.service.impl;

import com.techlooper.model.CitibankCreditCardPromotion;
import com.techlooper.service.PromotionService;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.StringWriter;

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

  public void placePromotion(CitibankCreditCardPromotion citibankCreditCardPromotion) throws IOException, TemplateException {

    StringWriter stringWriter = new StringWriter();
    citibankCreditCardPromotionTemplate.process(citibankCreditCardPromotion, stringWriter);
    System.out.println(stringWriter);
  }
}
