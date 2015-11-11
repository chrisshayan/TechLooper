package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 10/26/15.
 */
public class ChallengeRegistrantFunnelItem {

    private ChallengePhaseEnum phase;

    private Long participant;

    private Long submission;

    private Long unreadSubmission;

    public ChallengeRegistrantFunnelItem() {
    }

    public ChallengeRegistrantFunnelItem(ChallengePhaseEnum phase, Long participant, Long submission, Long unreadSubmission) {
        this.phase = phase;
        this.participant = participant;
        this.submission = submission;
        this.unreadSubmission = unreadSubmission;
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

    public Long getUnreadSubmission() {
        return unreadSubmission;
    }

    public void setUnreadSubmission(Long unreadSubmission) {
        this.unreadSubmission = unreadSubmission;
    }
}
