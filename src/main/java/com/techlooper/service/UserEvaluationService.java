package com.techlooper.service;

import com.techlooper.entity.SalaryReview;
import com.techlooper.model.SalaryReviewSurvey;

/**
 * Created by NguyenDangKhoa on 3/19/15.
 */
public interface UserEvaluationService {

    void evaluateJobOffer(SalaryReview salaryReview);

    void deleteSalaryReview(SalaryReview salaryReview);

    boolean saveSalaryReviewSurvey(SalaryReviewSurvey salaryReviewSurvey);
}
