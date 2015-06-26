package com.techlooper.service;

import com.techlooper.model.CitibankCreditCardPromotion;
import freemarker.template.TemplateException;

import java.io.IOException;

/**
 * Created by phuonghqh on 5/27/15.
 */
public interface PromotionService {

  void placePromotion(CitibankCreditCardPromotion citibankCreditCardPromotion) throws IOException, TemplateException;
}
