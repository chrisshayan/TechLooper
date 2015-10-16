package com.techlooper.model;

import java.io.Serializable;

/**
 * Created by phuonghqh on 10/16/15.
 */
public class ChallengeCriteriaDto implements Serializable {

  private Long challengeId;

  private String name;

  private Long weight;

  public Long getChallengeId() {
    return challengeId;
  }

  public void setChallengeId(Long challengeId) {
    this.challengeId = challengeId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getWeight() {
    return weight;
  }

  public void setWeight(Long weight) {
    this.weight = weight;
  }
}
