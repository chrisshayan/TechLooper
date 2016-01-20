package com.techlooper.service;

import com.techlooper.model.*;

import java.util.List;

public interface SalaryReviewService {

    void sendSalaryReviewReportEmail(SalaryReviewEmailRequest emailRequest);

    List<SimilarSalaryReview> getSimilarSalaryReview(SimilarSalaryReviewRequest request);

    SalaryReviewResultDto reviewSalary(SalaryReviewDto salaryReviewDto);

    void saveSalaryReviewResult(SalaryReviewResultDto salaryReviewResult);

    boolean saveSalaryReviewSurvey(SalaryReviewSurvey salaryReviewSurvey);

    List<TopPaidJob> findTopPaidJob(SalaryReviewDto salaryReviewDto);

    String chooseTheMostRelevantTitle(List<String> normalizedJobTitleCandidates);

}
