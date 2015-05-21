package com.techlooper.service;

import com.techlooper.entity.SalaryReview;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 5/18/15.
 */
public interface SalaryReviewService {

    List<SalaryReview> searchSalaryReview(SalaryReview salaryReview);

}
