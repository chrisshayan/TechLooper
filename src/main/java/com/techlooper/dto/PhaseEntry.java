package com.techlooper.dto;

import com.techlooper.model.ChallengePhaseEnum;

/**
 * Created by phuonghqh on 12/2/15.
 */
public class PhaseEntry {
  private String date;

  private ChallengePhaseEnum phase;

  private Long countMembers;

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public ChallengePhaseEnum getPhase() {
    return phase;
  }

  public void setPhase(ChallengePhaseEnum phase) {
    this.phase = phase;
  }

  public Long getCountMembers() {
    return countMembers;
  }

  public void setCountMembers(Long countMembers) {
    this.countMembers = countMembers;
  }

  public static class PhaseEntryBuilder {
    private PhaseEntry phaseEntry;

    private PhaseEntryBuilder() {
      phaseEntry = new PhaseEntry();
    }

    public PhaseEntryBuilder withDate(String date) {
      phaseEntry.date = date;
      return this;
    }

    public PhaseEntryBuilder withPhase(ChallengePhaseEnum phase) {
      phaseEntry.phase = phase;
      return this;
    }

    public PhaseEntryBuilder withCountMembers(Long countMembers) {
      phaseEntry.countMembers = countMembers;
      return this;
    }

    public static PhaseEntryBuilder phaseEntry() {
      return new PhaseEntryBuilder();
    }

    public PhaseEntry build() {
      return phaseEntry;
    }
  }
}
