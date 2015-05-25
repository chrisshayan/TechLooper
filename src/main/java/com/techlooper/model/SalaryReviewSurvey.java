package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 5/13/15.
 */
public class SalaryReviewSurvey {

    private Long salaryReviewId;

    private Boolean isUnderstandable;

    private Boolean isAccurate;

    private String feedback;

    public Long getSalaryReviewId() {
        return salaryReviewId;
    }

    public void setSalaryReviewId(Long salaryReviewId) {
        this.salaryReviewId = salaryReviewId;
    }

    public Boolean getIsUnderstandable() {
        return isUnderstandable;
    }

    public void setIsUnderstandable(Boolean isUnderstandable) {
        this.isUnderstandable = isUnderstandable;
    }

    public Boolean getIsAccurate() {
        return isAccurate;
    }

    public void setIsAccurate(Boolean isAccurate) {
        this.isAccurate = isAccurate;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
