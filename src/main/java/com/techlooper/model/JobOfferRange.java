package com.techlooper.model;

/**
 * Created by phuonghqh on 5/5/15.
 */
public class JobOfferRange {

  private JobLevelEnum jobLevel;

  private SalaryRange salaryRange;

  public JobLevelEnum getJobLevel() {
    return jobLevel;
  }

  public void setJobLevel(JobLevelEnum jobLevel) {
    this.jobLevel = jobLevel;
  }

  public SalaryRange getSalaryRange() {
    return salaryRange;
  }

  public void setSalaryRange(SalaryRange salaryRange) {
    this.salaryRange = salaryRange;
  }
}
