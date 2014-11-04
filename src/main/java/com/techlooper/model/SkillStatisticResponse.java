package com.techlooper.model;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 11/4/14.
 */
public class SkillStatisticResponse {

    private TechnicalTermEnum jobTerm;
    private Long count;
    private String period;
    private List<SkillStatisticItem> jobSkills;

    public SkillStatisticResponse(TechnicalTermEnum jobTerm, Long count, String period, List<SkillStatisticItem> jobSkills) {
        this.jobTerm = jobTerm;
        this.count = count;
        this.period = period;
        this.jobSkills = jobSkills;
    }

    public TechnicalTermEnum getJobTerm() {
        return jobTerm;
    }

    public void setJobTerm(TechnicalTermEnum jobTerm) {
        this.jobTerm = jobTerm;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public List<SkillStatisticItem> getJobSkills() {
        return jobSkills;
    }

    public void setJobSkills(List<SkillStatisticItem> jobSkills) {
        this.jobSkills = jobSkills;
    }
}