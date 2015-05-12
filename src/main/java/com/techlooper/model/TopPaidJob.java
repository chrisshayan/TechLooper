package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 5/11/15.
 */
public class TopPaidJob {

    private String jobId;

    private String jobTitle;

    private String companyDesc;

    private Double addedPercent;

    public TopPaidJob() {
    }

    public TopPaidJob(String jobId, String jobTitle, String companyDesc, Double addedPercent) {
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.companyDesc = companyDesc;
        this.addedPercent = addedPercent;
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
}
