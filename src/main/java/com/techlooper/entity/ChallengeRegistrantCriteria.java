package com.techlooper.entity;

import java.io.Serializable;

/**
 * Created by phuonghqh on 10/19/15.
 */
public class ChallengeRegistrantCriteria implements Serializable {

  private String name;

  private Long score;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getScore() {
    return score;
  }

  public void setScore(Long score) {
    this.score = score;
  }
}
