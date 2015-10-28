package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 10/26/15.
 */
public class ChallengeRegistrantFunnelItem {

    private ChallengePhaseEnum phase;

    private Long participant;

    private Long submission;

    public ChallengeRegistrantFunnelItem() {
    }

    public ChallengeRegistrantFunnelItem(ChallengePhaseEnum phase, Long participant, Long submission) {
        this.phase = phase;
        this.participant = participant;
        this.submission = submission;
    }

    public ChallengePhaseEnum getPhase() {
        return phase;
    }

    public void setPhase(ChallengePhaseEnum phase) {
        this.phase = phase;
    }

    public Long getParticipant() {
        return participant;
    }

    public void setParticipant(Long participant) {
        this.participant = participant;
    }

    public Long getSubmission() {
        return submission;
    }

    public void setSubmission(Long submission) {
        this.submission = submission;
    }
}
