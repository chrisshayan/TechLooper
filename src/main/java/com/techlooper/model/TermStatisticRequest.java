package com.techlooper.model;

import java.util.List;

/**
 * Created by phuonghqh on 4/17/15.
 */
public class TermStatisticRequest {

  private List<String> skills;

  private Integer jobLevelId;

  public List<String> getSkills() {
    return skills;
  }

  public void setSkills(List<String> skills) {
    this.skills = skills;
  }
}
