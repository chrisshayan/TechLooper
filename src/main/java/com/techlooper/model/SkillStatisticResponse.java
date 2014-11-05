package com.techlooper.model;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by NguyenDangKhoa on 11/4/14.
 */
public class SkillStatisticResponse {

    private String jobTerm;
    private Long count;
    private String period;
    private List<SkillStatisticItem> jobSkills;

    private static SkillStatisticResponse defaultObject;

    public SkillStatisticResponse(String jobTerm, Long count, String period, List<SkillStatisticItem> jobSkills) {
        this.jobTerm = jobTerm;
        this.count = count;
        this.period = period;
        this.jobSkills = jobSkills;
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

    public static SkillStatisticResponse getDefaultObject() {
        return Optional.ofNullable(defaultObject).orElseGet(() -> {
            defaultObject = new SkillStatisticResponse(null, 0L, PeriodEnum.EMPTY.toString(), Collections.emptyList());
            return defaultObject;
        });
    }
}