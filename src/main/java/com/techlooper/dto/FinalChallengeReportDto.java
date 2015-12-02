package com.techlooper.dto;

import com.techlooper.entity.ChallengeRegistrantDto;
import com.techlooper.model.ChallengeDto;
import com.techlooper.model.ChallengePhaseEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by phuonghqh on 12/1/15.
 */
public class FinalChallengeReportDto extends ChallengeDto {

  private List<PhaseEntry> phaseEntries;

  private Set<ChallengeRegistrantDto> winnersInfo;

  private int totalPlaceReward;

  private String baseUrl;

  private String phaseOptions = "";

//  private static final ChallengePhaseEnum[] ALL_OPTS = new ChallengePhaseEnum[] {ChallengePhaseEnum.IDEA, ChallengePhaseEnum.UIUX, ChallengePhaseEnum.PROTOTYPE};

  public String getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public Set<ChallengeRegistrantDto> getWinnersInfo() {
    return winnersInfo;
  }

  public void setWinnersInfo(Set<ChallengeRegistrantDto> winnersInfo) {
    this.winnersInfo = winnersInfo;
  }

  public List<PhaseEntry> getPhaseEntries() {
    return phaseEntries;
  }

  public void setPhaseEntries(List<PhaseEntry> phaseEntries) {
    this.phaseEntries = phaseEntries;
  }

  private void calculateTotalPlaceReward() {
    totalPlaceReward = 0;
    if (getFirstPlaceReward() != null) totalPlaceReward += getFirstPlaceReward();
    if (getSecondPlaceReward() != null) totalPlaceReward += getSecondPlaceReward();
    if (getThirdPlaceReward() != null) totalPlaceReward += getThirdPlaceReward();
  }

  public void calculateRemainingFields() {
    calculateTotalPlaceReward();
    calculatePhaseOptions();
  }

  private void calculatePhaseOptions() {
    List<String> opts = new ArrayList<>();
    phaseEntries.stream().forEach(pe -> {
      switch (pe.getPhase()) {
        case IDEA:
        case UIUX:
        case PROTOTYPE:
          opts.add(pe.getPhase().name().toLowerCase());
          break;
      }
    });
    phaseOptions = opts.stream().collect(Collectors.joining("-"));
  }

  public int getTotalPlaceReward() {
    return totalPlaceReward;
  }

  public void setTotalPlaceReward(int totalPlaceReward) {
    this.totalPlaceReward = totalPlaceReward;
  }

  public String getPhaseOptions() {
    return phaseOptions;
  }

  public void setPhaseOptions(String phaseOptions) {
    this.phaseOptions = phaseOptions;
  }
}
