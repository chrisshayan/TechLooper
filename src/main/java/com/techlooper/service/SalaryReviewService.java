package com.techlooper.service;

import com.techlooper.entity.SalaryReviewEntity;
import com.techlooper.model.*;

import java.util.List;

public interface SalaryReviewService {

    List<SalaryReviewEntity> findSalaryReview(SalaryReviewDto salaryReviewDto);

    void sendSalaryReviewReportEmail(SalaryReviewEmailRequest emailRequest);

    void createVnwJobAlert(VnwJobAlertRequest vnwJobAlertRequest);

    List<SimilarSalaryReview> getSimilarSalaryReview(SimilarSalaryReviewRequest request);

    SalaryReviewResultDto reviewSalary(SalaryReviewDto salaryReviewDto);

    void saveSalaryReviewResult(SalaryReviewResultDto salaryReviewResult);

    void deleteSalaryReview(SalaryReviewEntity salaryReviewEntity);

    boolean saveSalaryReviewSurvey(SalaryReviewSurvey salaryReviewSurvey);

    List<TopPaidJob> findTopPaidJob(SalaryReviewDto salaryReviewDto);

}
