package com.techlooper.model;

import java.io.Serializable;

/**
 * Created by phuonghqh on 11/2/15.
 */
public class ChallengeWinner implements Serializable {

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

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ChallengeWinner that = (ChallengeWinner) o;

    return !(registrantId != null ? !registrantId.equals(that.registrantId) : that.registrantId != null);

  }

  public int hashCode() {
    return registrantId != null ? registrantId.hashCode() : 0;
  }
}
