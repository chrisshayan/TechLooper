package com.techlooper.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 10/24/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class VNWJobSearchRequest {

  @JsonProperty(value = "job_title")
  private String jobTitle;

  @JsonProperty(value = "job_location")
  private String jobLocation;

  @JsonProperty(value = "job_category")
  private List<Long> jobCategories;

  @JsonProperty(value = "job_level")
  private String jobLevel;

  @JsonProperty(value = "page_number")
  private String pageNumber;

  @JsonProperty(value = "job_salary")
  private Integer jobSalary;

  public Integer getJobSalary() {
    return jobSalary;
  }

  public void setJobSalary(Integer jobSalary) {
    this.jobSalary = jobSalary;
  }

  public List<Long> getJobCategories() {
    return jobCategories;
  }

  public void setJobCategories(List<Long> jobCategories) {
    this.jobCategories = jobCategories;
  }

  public String getJobTitle() {
    return jobTitle;
  }

  public void setJobTitle(String jobTitle) {
    this.jobTitle = jobTitle;
  }

  public String getJobLocation() {
    return jobLocation;
  }

  public void setJobLocation(String jobLocation) {
    this.jobLocation = jobLocation;
  }

  public String getJobLevel() {
    return jobLevel;
  }

  public void setJobLevel(String jobLevel) {
    this.jobLevel = jobLevel;
  }

  public String getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(String pageNumber) {
    this.pageNumber = pageNumber;
  }
}
