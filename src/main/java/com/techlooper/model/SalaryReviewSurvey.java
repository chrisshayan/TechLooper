package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 5/13/15.
 */
public class SalaryReviewSurvey {

    private Long salaryReviewId;

    private boolean isUnderstandable;

    private boolean isAccurate;

    private String feedback;

    public Long getSalaryReviewId() {
        return salaryReviewId;
    }

    public void setSalaryReviewId(Long salaryReviewId) {
        this.salaryReviewId = salaryReviewId;
    }

    public boolean isUnderstandable() {
        return isUnderstandable;
    }

    public void setUnderstandable(boolean isUnderstandable) {
        this.isUnderstandable = isUnderstandable;
    }

    public boolean isAccurate() {
        return isAccurate;
    }

    public void setAccurate(boolean isAccurate) {
        this.isAccurate = isAccurate;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
