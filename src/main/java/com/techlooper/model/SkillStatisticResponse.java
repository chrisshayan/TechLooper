package com.techlooper.model;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by NguyenDangKhoa on 11/4/14.
 */
public class SkillStatisticResponse {

    private static SkillStatisticResponse defaultObject;

    private String jobTerm;
    private Long count;
    private Long totalTechnicalJobs;
    private List<SkillStatisticItem> jobSkills;

    public SkillStatisticResponse() {
    }

    public SkillStatisticResponse(String jobTerm, Long count, Long totalTechnicalJobs, List<SkillStatisticItem> jobSkills) {
        this.jobTerm = jobTerm;
        this.count = count;
        this.totalTechnicalJobs = totalTechnicalJobs;
        this.jobSkills = jobSkills;
    }

    public static SkillStatisticResponse getDefaultObject() {
        return Optional.ofNullable(defaultObject).orElseGet(() -> {
            defaultObject = new SkillStatisticResponse(null, 0L, 0L, Collections.emptyList());
            return defaultObject;
        });
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

    public List<SkillStatisticItem> getJobSkills() {
        return jobSkills;
    }

    public void setJobSkills(List<SkillStatisticItem> jobSkills) {
        this.jobSkills = jobSkills;
    }

    public Long getTotalTechnicalJobs() {
        return totalTechnicalJobs;
    }

    public void setTotalTechnicalJobs(Long totalTechnicalJobs) {
        this.totalTechnicalJobs = totalTechnicalJobs;
    }

    public static class Builder {

        private SkillStatisticResponse instance = new SkillStatisticResponse();

        public Builder withTotalTechnicalJobs(Long totalTechnicalJobs) {
            instance.totalTechnicalJobs = totalTechnicalJobs;
            return this;
        }

        public Builder withJobSkills(List<SkillStatisticItem> jobSkills) {
            instance.jobSkills = jobSkills;
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
}