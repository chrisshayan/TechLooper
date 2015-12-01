package com.techlooper.service.impl;

import com.techlooper.entity.SalaryReviewEntity;
import com.techlooper.model.CitibankCreditCardPromotion;
import com.techlooper.model.EmailRequestModel;
import com.techlooper.model.EmailTemplateNameEnum;
import com.techlooper.model.Language;
import com.techlooper.repository.elasticsearch.SalaryReviewRepository;
import com.techlooper.service.EmailService;
import com.techlooper.service.PromotionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Service
public class PromotionServiceImpl implements PromotionService {

    @Resource
    private MimeMessage citibankCreditCardPromotionMailMessage;

    @Resource
    private SalaryReviewRepository salaryReviewRepository;

    @Resource
    private Map<Long, String> locationMap;

    @Resource
    private EmailService emailService;

    @Override
    public void placePromotion(CitibankCreditCardPromotion citibankCreditCardPromotion) {
        SalaryReviewEntity salaryReviewEntity = salaryReviewRepository.findOne(citibankCreditCardPromotion.getSalaryReviewId());

        citibankCreditCardPromotion.setNetIncome(String.valueOf(salaryReviewEntity.getNetSalary()));

        String location = locationMap.get(salaryReviewEntity.getLocationId());
        citibankCreditCardPromotion.setLocation(location);

        EmailRequestModel emailRequestModel = new EmailRequestModel.Builder()
                .withTemplateName(EmailTemplateNameEnum.CITIBANK_CREDIT_CARD_PROMOTION.name())
                .withLanguage(Language.en)
                .withTemplateModel(buildTemplateModel(citibankCreditCardPromotion))
                .withMailMessage(citibankCreditCardPromotionMailMessage).build();
        emailService.sendMail(emailRequestModel);
    }

    private Map<String, Object> buildTemplateModel(CitibankCreditCardPromotion model) {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("fullName", model.getFullName());
        templateModel.put("mobileNumber", model.getMobileNumber());
        templateModel.put("email", model.getEmail());
        templateModel.put("location", model.getLocation());
        templateModel.put("netIncome", model.getNetIncome());
        templateModel.put("paymentMethod", model.getPaymentMethod());
        return templateModel;
    }
}
