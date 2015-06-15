package com.techlooper.model;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 6/15/15.
 */
public class GetPromotedRequest {

    private String jobTitle;

    private Integer jobLevelId;

    private List<Long> jobCategories;

    private int limitSkills;

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Integer getJobLevelId() {
        return jobLevelId;
    }

    public void setJobLevelId(Integer jobLevelId) {
        this.jobLevelId = jobLevelId;
    }

    public List<Long> getJobCategories() {
        return jobCategories;
    }

    public void setJobCategories(List<Long> jobCategories) {
        this.jobCategories = jobCategories;
    }

    public int getLimitSkills() {
        return limitSkills;
    }

    public void setLimitSkills(int limitSkills) {
        this.limitSkills = limitSkills;
    }
}
