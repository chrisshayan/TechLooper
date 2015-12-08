package com.techlooper.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by phuonghqh on 5/29/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SalaryReviewEmailRequest {

    private Language lang;

    private String email;

    private Long salaryReviewId;

    public Language getLang() {
        return lang;
    }

    public void setLang(Language lang) {
        this.lang = lang;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getSalaryReviewId() {
        return salaryReviewId;
    }

    public void setSalaryReviewId(Long salaryReviewId) {
        this.salaryReviewId = salaryReviewId;
    }
}
