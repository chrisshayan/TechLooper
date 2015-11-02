package com.techlooper.dto;

import com.techlooper.model.RewardEnum;

import java.io.Serializable;

/**
 * Created by phuonghqh on 11/2/15.
 */
public class WinnerDto implements Serializable {

  private Long registrantId;

  private RewardEnum reward;

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
}
