package com.techlooper.entity;

/**
 * Created by phuonghqh on 4/2/15.
 */
public class CompanyJob {

  private Long jobId;

  private String jobTitle;

  private String jobURL;

  private String expiredDate;

  private Long numOfApplications;

  private Long numOfViews;

  public Long getNumOfApplications() {
    return numOfApplications;
  }

  public void setNumOfApplications(Long numOfApplications) {
    this.numOfApplications = numOfApplications;
  }

  public Long getNumOfViews() {
    return numOfViews;
  }

  public void setNumOfViews(Long numOfViews) {
    this.numOfViews = numOfViews;
  }

  public Long getJobId() {
    return jobId;
  }

  public void setJobId(Long jobId) {
    this.jobId = jobId;
  }

  public String getJobTitle() {
    return jobTitle;
  }

  public void setJobTitle(String jobTitle) {
    this.jobTitle = jobTitle;
  }

  public String getJobURL() {
    return jobURL;
  }

  public void setJobURL(String jobURL) {
    this.jobURL = jobURL;
  }

  public String getExpiredDate() {
    return expiredDate;
  }

  public void setExpiredDate(String expiredDate) {
    this.expiredDate = expiredDate;
  }
}
