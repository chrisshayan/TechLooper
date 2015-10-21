package com.techlooper.model;

import com.techlooper.entity.ChallengeCriteria;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by phuonghqh on 10/16/15.
 */
public class ChallengeCriteriaDto implements Serializable {

  private Long challengeId;

  private Set<ChallengeCriteria> challengeCriteria;

  private Set<ChallengeRegistrantCriteriaDto> registrantCriteria;

  public Set<ChallengeRegistrantCriteriaDto> getRegistrantCriteria() {
    if (registrantCriteria == null) registrantCriteria = new HashSet<>();
    return registrantCriteria;
  }

  public void setRegistrantCriteria(Set<ChallengeRegistrantCriteriaDto> registrantCriteria) {
    this.registrantCriteria = registrantCriteria;
  }

  public Long getChallengeId() {
    return challengeId;
  }

  public void setChallengeId(Long challengeId) {
    this.challengeId = challengeId;
  }

  public Set<ChallengeCriteria> getChallengeCriteria() {
    return challengeCriteria;
  }

  public void setChallengeCriteria(Set<ChallengeCriteria> challengeCriteria) {
    this.challengeCriteria = challengeCriteria;
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

    public ChallengeCriteriaDtoBuilder withChallengeCriteria(Set<ChallengeCriteria> challengeCriteria) {
      challengeCriteriaDto.challengeCriteria = challengeCriteria;
      return this;
    }

    public ChallengeCriteriaDtoBuilder withRegistrantCriteria(Set<ChallengeRegistrantCriteriaDto> registrantCriteria) {
      challengeCriteriaDto.registrantCriteria = registrantCriteria;
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
