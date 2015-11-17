package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 11/17/15.
 */
public class ChallengeFilterCondition {

    private boolean notExpired = true;

    private ChallengePhaseEnum phase;

    private String authorEmail;

    public boolean isNotExpired() {
        return notExpired;
    }

    public void setNotExpired(boolean notExpired) {
        this.notExpired = notExpired;
    }

    public ChallengePhaseEnum getPhase() {
        return phase;
    }

    public void setPhase(ChallengePhaseEnum phase) {
        this.phase = phase;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }
}
