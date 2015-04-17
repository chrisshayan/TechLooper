package com.techlooper.model;

import java.util.List;

/**
 * Created by phuonghqh on 4/17/15.
 */
public class TermStatisticRequest {

  private String term;

  private List<String> skills;

  private Integer jobLevelId;

  public String getTerm() {
    return term;
  }

  public void setTerm(String term) {
    this.term = term;
  }

  public Integer getJobLevelId() {
    return jobLevelId;
  }

  public void setJobLevelId(Integer jobLevelId) {
    this.jobLevelId = jobLevelId;
  }

  public List<String> getSkills() {
    return skills;
  }

  public void setSkills(List<String> skills) {
    this.skills = skills;
  }
}
