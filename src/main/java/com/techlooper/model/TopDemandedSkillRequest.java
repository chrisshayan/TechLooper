package com.techlooper.model;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 6/12/15.
 */
public class TopDemandedSkillRequest {

    private String jobTitle;

    private List<Long> jobCategories;

    private Integer jobLevelId;

    private int limit;

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

    public Integer getJobLevelId() {
        return jobLevelId;
    }

    public void setJobLevelId(Integer jobLevelId) {
        this.jobLevelId = jobLevelId;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
