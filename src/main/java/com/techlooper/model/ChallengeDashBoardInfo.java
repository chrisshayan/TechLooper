package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 1/19/16.
 */
public class ChallengeDashBoardInfo {

    private Long challengeId;

    private String challengeName;

    private String submissionDate;

    private ChallengePhaseEnum currentPhase;

    private String currentPhaseSubmissionDate;

    private Integer numberOfSubmissions;

    private Double score;

    private Integer rank;

    private Integer prize;

    private Boolean disqualified;

    public Long getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(Long challengeId) {
        this.challengeId = challengeId;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }

    public ChallengePhaseEnum getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(ChallengePhaseEnum currentPhase) {
        this.currentPhase = currentPhase;
    }

    public String getCurrentPhaseSubmissionDate() {
        return currentPhaseSubmissionDate;
    }

    public void setCurrentPhaseSubmissionDate(String currentPhaseSubmissionDate) {
        this.currentPhaseSubmissionDate = currentPhaseSubmissionDate;
    }

    public Integer getNumberOfSubmissions() {
        return numberOfSubmissions;
    }

    public void setNumberOfSubmissions(Integer numberOfSubmissions) {
        this.numberOfSubmissions = numberOfSubmissions;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getPrize() {
        return prize;
    }

    public void setPrize(Integer prize) {
        this.prize = prize;
    }

    public Boolean getDisqualified() {
        return disqualified;
    }

    public void setDisqualified(Boolean disqualified) {
        this.disqualified = disqualified;
    }
}
