package com.techlooper.dto;

import com.techlooper.entity.ChallengeRegistrantEntity;
import com.techlooper.model.ChallengeDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by phuonghqh on 12/1/15.
 */
public class FinalChallengeReportDto extends ChallengeDto {

    private List<PhaseEntry> phaseEntries;

    private List<ChallengeRegistrantEntity> winnersInfo;

    private int totalPlaceReward;

    private String baseUrl;

    private String phaseOptions = "";

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public List<ChallengeRegistrantEntity> getWinnersInfo() {
        return winnersInfo;
    }

    public void setWinnersInfo(List<ChallengeRegistrantEntity> winnersInfo) {
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
                default:
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
