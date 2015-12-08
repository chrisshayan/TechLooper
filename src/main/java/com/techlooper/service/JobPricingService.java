package com.techlooper.service;

import com.techlooper.entity.GetPromotedEntity;
import com.techlooper.entity.PriceJobEntity;
import com.techlooper.model.GetPromotedEmailRequest;
import com.techlooper.model.GetPromotedSurveyRequest;
import com.techlooper.model.PriceJobSurvey;

public interface JobPricingService {

    void sendTopDemandedSkillsEmail(long getPromotedId, GetPromotedEmailRequest emailRequest);

    long saveGetPromotedInformation(GetPromotedEmailRequest getPromotedEmailRequest);

    GetPromotedEntity getPromotedEntity(Long id);

    long saveGetPromotedSurvey(GetPromotedSurveyRequest getPromotedSurveyRequest);

    void priceJob(PriceJobEntity priceJobEntity);

    boolean savePriceJobSurvey(PriceJobSurvey priceJobSurvey);

}
