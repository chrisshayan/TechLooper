package com.techlooper.model;

import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * Created by phuonghqh on 5/21/15.
 */
public class VnwJobAlertRequest {

    private String email;

    private String jobTitle;

    private List<Long> jobCategories;

    private Long locationId;

    private Long netSalary;

    private Long frequency;

    private Long lang;

    private Long jobLevel;

    private Long salaryReviewId;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public List<Long> getJobCategories() {
        return jobCategories;
    }

    public void setJobCategories(List<Long> jobCategories) {
        this.jobCategories = jobCategories;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(Long netSalary) {
        this.netSalary = netSalary;
    }

    public Long getFrequency() {
        return frequency;
    }

    public void setFrequency(Long frequency) {
        this.frequency = frequency;
    }

    public Long getLang() {
        return lang;
    }

    public void setLang(Long lang) {
        this.lang = lang;
    }

    public Long getJobLevel() {
        return jobLevel;
    }

    public void setJobLevel(Long jobLevel) {
        this.jobLevel = jobLevel;
    }

    public Long getSalaryReviewId() {
        return salaryReviewId;
    }

    public void setSalaryReviewId(Long salaryReviewId) {
        this.salaryReviewId = salaryReviewId;
    }
}
