package com.techlooper.model;

import com.techlooper.entity.ChallengeCriteria;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by phuonghqh on 10/16/15.
 */
public class ChallengeCriteriaDto implements Serializable {

  private Long challengeId;

  private Set<ChallengeCriteria> challengeCriterias;

  public Long getChallengeId() {
    return challengeId;
  }

  public void setChallengeId(Long challengeId) {
    this.challengeId = challengeId;
  }

  public Set<ChallengeCriteria> getChallengeCriterias() {
    return challengeCriterias;
  }

  public void setChallengeCriterias(Set<ChallengeCriteria> challengeCriterias) {
    this.challengeCriterias = challengeCriterias;
  }

  public static class ChallengeCriteriaDtoBuilder {
    private ChallengeCriteriaDto challengeCriteriaDto;

    private ChallengeCriteriaDtoBuilder() {
      challengeCriteriaDto = new ChallengeCriteriaDto();
    }

    public ChallengeCriteriaDtoBuilder withChallengeId(Long challengeId) {
      challengeCriteriaDto.challengeId = challengeId;
      return this;
    }

    public ChallengeCriteriaDtoBuilder withChallengeCriterias(Set<ChallengeCriteria> challengeCriterias) {
      challengeCriteriaDto.challengeCriterias = challengeCriterias;
      return this;
    }

    public static ChallengeCriteriaDtoBuilder challengeCriteriaDto() {
      return new ChallengeCriteriaDtoBuilder();
    }

    public ChallengeCriteriaDto build() {
      return challengeCriteriaDto;
    }
  }
}
