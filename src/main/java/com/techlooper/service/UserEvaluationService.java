package com.techlooper.service;

import com.techlooper.entity.PriceJobEntity;
import com.techlooper.entity.SalaryReviewEntity;
import com.techlooper.model.PriceJobSurvey;
import com.techlooper.model.SalaryReviewSurvey;

/**
 * Created by NguyenDangKhoa on 3/19/15.
 */
public interface UserEvaluationService {

    void reviewSalary(SalaryReviewEntity salaryReviewEntity);

    void deleteSalaryReview(SalaryReviewEntity salaryReviewEntity);

    boolean saveSalaryReviewSurvey(SalaryReviewSurvey salaryReviewSurvey);

    void priceJob(PriceJobEntity priceJobEntity);

    boolean savePriceJobSurvey(PriceJobSurvey priceJobSurvey);
}
