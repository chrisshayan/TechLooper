package com.techlooper.entity;

/**
 * Created by phuonghqh on 4/2/15.
 */
public class CompanyJob {

  private Long jobId;

  private String jobTitle;

  private String jobURL;

  private String expiredDate;

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
