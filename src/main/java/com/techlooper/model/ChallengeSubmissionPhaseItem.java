package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 10/26/15.
 */
public class ChallengeSubmissionPhaseItem {

    private ChallengePhaseEnum phase;

    private Long submission;

    public ChallengeSubmissionPhaseItem() {
    }

    public ChallengeSubmissionPhaseItem(ChallengePhaseEnum phase, Long submission) {
        this.phase = phase;
        this.submission = submission;
    }

    public ChallengePhaseEnum getPhase() {
        return phase;
    }

    public void setPhase(ChallengePhaseEnum phase) {
        this.phase = phase;
    }

    public Long getSubmission() {
        return submission;
    }

    public void setSubmission(Long submission) {
        this.submission = submission;
    }
}
