package com.techlooper.model;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 6/15/15.
 */
public class GetPromotedRequest {

    private String jobTitle;

    private List<Integer> jobLevelIds;

    private List<Long> jobCategoryIds;

    private int limitSkills;

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public List<Integer> getJobLevelIds() {
        return jobLevelIds;
    }

    public void setJobLevelIds(List<Integer> jobLevelIds) {
        this.jobLevelIds = jobLevelIds;
    }

    public List<Long> getJobCategoryIds() {
        return jobCategoryIds;
    }

    public void setJobCategoryIds(List<Long> jobCategoryIds) {
        this.jobCategoryIds = jobCategoryIds;
    }

    public int getLimitSkills() {
        return limitSkills;
    }

    public void setLimitSkills(int limitSkills) {
        this.limitSkills = limitSkills;
    }
}
