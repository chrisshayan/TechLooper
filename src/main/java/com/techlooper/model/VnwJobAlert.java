package com.techlooper.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by phuonghqh on 5/21/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class VnwJobAlert {

  private String email;

  @JsonProperty("keywords")
  private String jobTitle;

  @JsonProperty("job_categories")
  private List<Long> jobCategories;

  @JsonProperty("job_locations")
  private List<Long> jobLocations;

  @JsonProperty("min_salary")
  private Long minSalary;

  private Long frequency;

  private Long lang;

  @JsonProperty("job_level")
  private Long jobLevel;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getJobTitle() {
    return jobTitle;
  }

  public void setJobTitle(String jobTitle) {
    this.jobTitle = jobTitle;
  }

  public List<Long> getJobCategories() {
    return jobCategories;
  }

  public void setJobCategories(List<Long> jobCategories) {
    this.jobCategories = jobCategories;
  }

  public List<Long> getJobLocations() {
    return jobLocations;
  }

  public void setJobLocations(List<Long> jobLocations) {
    this.jobLocations = jobLocations;
  }

  public Long getMinSalary() {
    return minSalary;
  }

  public void setMinSalary(Long minSalary) {
    this.minSalary = minSalary;
  }

  public Long getFrequency() {
    return frequency;
  }

  public void setFrequency(Long frequency) {
    this.frequency = frequency;
  }

  public Long getLang() {
    return lang;
  }

  public void setLang(Long lang) {
    this.lang = lang;
  }

  public Long getJobLevel() {
    return jobLevel;
  }

  public void setJobLevel(Long jobLevel) {
    this.jobLevel = jobLevel;
  }
}
