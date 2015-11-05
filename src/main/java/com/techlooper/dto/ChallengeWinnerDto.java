package com.techlooper.dto;

import com.techlooper.model.RewardEnum;

import java.io.Serializable;

/**
 * Created by phuonghqh on 11/5/15.
 */
public class ChallengeWinnerDto implements Serializable {
  private Long registrantId;

  private RewardEnum reward;

  private Boolean removable;

  public Long getRegistrantId() {
    return registrantId;
  }

  public void setRegistrantId(Long registrantId) {
    this.registrantId = registrantId;
  }

  public RewardEnum getReward() {
    return reward;
  }

  public void setReward(RewardEnum reward) {
    this.reward = reward;
  }

  public Boolean getRemovable() {
    return removable;
  }

  public void setRemovable(Boolean removable) {
    this.removable = removable;
  }
}
