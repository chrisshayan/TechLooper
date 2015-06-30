package com.techlooper.model;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 5/11/15.
 */
public class TopPaidJob {

    private String jobId;

    private String jobTitle;

    private String companyDesc;

    private Double addedPercent;

    private List<String> skills;

    public TopPaidJob() {
    }

    public TopPaidJob(String jobId, String jobTitle, String companyDesc, Double addedPercent, List<String> skills) {
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.companyDesc = companyDesc;
        this.addedPercent = addedPercent;
        this.skills = skills;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanyDesc() {
        return companyDesc;
    }

    public void setCompanyDesc(String companyDesc) {
        this.companyDesc = companyDesc;
    }

    public Double getAddedPercent() {
        return addedPercent;
    }

    public void setAddedPercent(Double addedPercent) {
        this.addedPercent = addedPercent;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }
}
