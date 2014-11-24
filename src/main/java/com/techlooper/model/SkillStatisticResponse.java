package com.techlooper.model;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 11/4/14.
 */
public class SkillStatisticResponse {

    private String jobTerm;
    private Long count;
    private Long totalTechnicalJobs;
    private List<SkillStatistic> skills;

    public static class Builder {

        private SkillStatisticResponse instance = new SkillStatisticResponse();

        public Builder withTotalTechnicalJobs(Long totalTechnicalJobs) {
            instance.totalTechnicalJobs = totalTechnicalJobs;
            return this;
        }

        public Builder withSkills(List<SkillStatistic> skills) {
            instance.skills = skills;
            return this;
        }

        public Builder withJobTerm(String jobTerm) {
            instance.jobTerm = jobTerm;
            return this;
        }

        public Builder withCount(Long count) {
            instance.count = count;
            return this;
        }

        public SkillStatisticResponse build() {
            return instance;
        }
    }

    public String getJobTerm() {
        return jobTerm;
    }

    public void setJobTerm(String jobTerm) {
        this.jobTerm = jobTerm;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<SkillStatistic> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillStatistic> skills) {
        this.skills = skills;
    }

    public Long getTotalTechnicalJobs() {
        return totalTechnicalJobs;
    }

    public void setTotalTechnicalJobs(Long totalTechnicalJobs) {
        this.totalTechnicalJobs = totalTechnicalJobs;
    }
}