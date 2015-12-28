package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 11/17/15.
 */
public class ChallengeFilterCondition {

    private boolean notExpired = true;

    private ChallengePhaseEnum phase;

    private String authorEmail;

    private ChallengeTypeEnum challengeType;

    private String challengeSearchText;

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

    public ChallengeTypeEnum getChallengeType() {
        return challengeType;
    }

    public void setChallengeType(ChallengeTypeEnum challengeType) {
        this.challengeType = challengeType;
    }

    public String getChallengeSearchText() {
        return challengeSearchText;
    }

    public void setChallengeSearchText(String challengeSearchText) {
        this.challengeSearchText = challengeSearchText;
    }
}
