package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 10/26/15.
 */
public class ChallengeRegistrantPhaseItem {

    private ChallengePhaseEnum phase;

    private Long registration;

    public ChallengeRegistrantPhaseItem() {
    }

    public ChallengeRegistrantPhaseItem(ChallengePhaseEnum phase, Long registration) {
        this.phase = phase;
        this.registration = registration;
    }

    public ChallengePhaseEnum getPhase() {
        return phase;
    }

    public void setPhase(ChallengePhaseEnum phase) {
        this.phase = phase;
    }

    public Long getRegistration() {
        return registration;
    }

    public void setRegistration(Long registration) {
        this.registration = registration;
    }

}
