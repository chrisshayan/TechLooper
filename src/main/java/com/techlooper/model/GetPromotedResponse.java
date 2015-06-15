package com.techlooper.model;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 6/15/15.
 */
public class GetPromotedResponse {

    private Long totalJob;

    private Double salaryMin;

    private Double salaryMax;

    List<TopDemandedSkillResponse> topDemandedSkills;

    public Long getTotalJob() {
        return totalJob;
    }

    public void setTotalJob(Long totalJob) {
        this.totalJob = totalJob;
    }

    public Double getSalaryMin() {
        return salaryMin;
    }

    public void setSalaryMin(Double salaryMin) {
        this.salaryMin = salaryMin;
    }

    public Double getSalaryMax() {
        return salaryMax;
    }

    public void setSalaryMax(Double salaryMax) {
        this.salaryMax = salaryMax;
    }

    public List<TopDemandedSkillResponse> getTopDemandedSkills() {
        return topDemandedSkills;
    }

    public void setTopDemandedSkills(List<TopDemandedSkillResponse> topDemandedSkills) {
        this.topDemandedSkills = topDemandedSkills;
    }
}
