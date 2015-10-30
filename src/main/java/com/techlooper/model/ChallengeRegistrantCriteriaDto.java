package com.techlooper.model;

import com.techlooper.entity.ChallengeRegistrantCriteria;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by phuonghqh on 10/20/15.
 */
public class ChallengeRegistrantCriteriaDto implements Serializable {

  private Long registrantId;

  private Set<ChallengeRegistrantCriteria> criteria;

  public Long getRegistrantId() {
    return registrantId;
  }

  public void setRegistrantId(Long registrantId) {
    this.registrantId = registrantId;
  }

  public Set<ChallengeRegistrantCriteria> getCriteria() {
    if (criteria == null) criteria = new HashSet<>();
    return criteria;
  }

  public void setCriteria(Set<ChallengeRegistrantCriteria> criteria) {
    this.criteria = criteria;
  }

  public static class ChallengeRegistrantCriteriaDtoBuilder {
    private ChallengeRegistrantCriteriaDto challengeRegistrantCriteriaDto;

    private ChallengeRegistrantCriteriaDtoBuilder() {
      challengeRegistrantCriteriaDto = new ChallengeRegistrantCriteriaDto();
    }

    public ChallengeRegistrantCriteriaDtoBuilder withRegistrantId(Long registrantId) {
      challengeRegistrantCriteriaDto.registrantId = registrantId;
      return this;
    }

    public ChallengeRegistrantCriteriaDtoBuilder withCriteria(Set<ChallengeRegistrantCriteria> criteria) {
      challengeRegistrantCriteriaDto.criteria = criteria;
      return this;
    }

    public static ChallengeRegistrantCriteriaDtoBuilder challengeRegistrantCriteriaDto() {
      return new ChallengeRegistrantCriteriaDtoBuilder();
    }

    public ChallengeRegistrantCriteriaDto build() {
      return challengeRegistrantCriteriaDto;
    }
  }
}
