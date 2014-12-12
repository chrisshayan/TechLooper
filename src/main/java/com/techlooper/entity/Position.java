package com.techlooper.entity;

/**
 * Created by phuonghqh on 12/12/14.
 */
public class Position {
  private Company company;
  private LinkedInDate startDate;
  private LinkedInDate endDate;
  private String summary;
  private String title;

  public Company getCompany() {
    return company;
  }

  public void setCompany(Company company) {
    this.company = company;
  }


  public LinkedInDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LinkedInDate startDate) {
    this.startDate = startDate;
  }

  public LinkedInDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LinkedInDate endDate) {
    this.endDate = endDate;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
