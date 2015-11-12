package com.techlooper.dto;

import com.techlooper.model.ChallengePhaseEnum;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 11/10/15.
 */
public class ChallengeQualificationDto {

    private Long challengeId;

    private ChallengePhaseEnum nextPhase;

    private List<Long> registrantIds;

    public Long getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(Long challengeId) {
        this.challengeId = challengeId;
    }

    public List<Long> getRegistrantIds() {
        return registrantIds;
    }

    public void setRegistrantIds(List<Long> registrantIds) {
        this.registrantIds = registrantIds;
    }

    public ChallengePhaseEnum getNextPhase() {
        return nextPhase;
    }

    public void setNextPhase(ChallengePhaseEnum nextPhase) {
        this.nextPhase = nextPhase;
    }
}
