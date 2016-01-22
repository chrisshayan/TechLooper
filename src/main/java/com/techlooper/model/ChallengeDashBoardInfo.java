package com.techlooper.model;

import com.techlooper.entity.ChallengeRegistrantCriteria;
import com.techlooper.entity.ChallengeSubmissionEntity;
import com.techlooper.model.challenge.PhaseType;

import java.util.List;
import java.util.Set;

/**
 * Created by NguyenDangKhoa on 1/19/16.
 */
public class ChallengeDashBoardInfo {

    private Long challengeId;

    private String challengeName;

    private String submissionDate;

    private PhaseType currentPhase;

    private String currentPhaseSubmissionDate;

    private Integer numberOfSubmissions;

    private Double score;

    private Integer rank;

    private Integer prize;

    private Boolean disqualified;

    private ChallengeTypeEnum challengeType;

    private Set<ChallengeRegistrantCriteria> criteria;

    private List<ChallengeSubmissionEntity> submissions;

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

    public PhaseType getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(PhaseType currentPhase) {
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

    public Set<ChallengeRegistrantCriteria> getCriteria() {
        return criteria;
    }

    public void setCriteria(Set<ChallengeRegistrantCriteria> criteria) {
        this.criteria = criteria;
    }

    public List<ChallengeSubmissionEntity> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<ChallengeSubmissionEntity> submissions) {
        this.submissions = submissions;
    }

    public ChallengeTypeEnum getChallengeType() {
        return challengeType;
    }

    public void setChallengeType(ChallengeTypeEnum challengeType) {
        this.challengeType = challengeType;
    }

    public static class Builder {

        private ChallengeDashBoardInfo challengeDashBoardInfo = new ChallengeDashBoardInfo();

        public Builder withChallengeId(Long challengeId) {
            challengeDashBoardInfo.setChallengeId(challengeId);
            return this;
        }

        public Builder withChallengeName(String challengeName) {
            challengeDashBoardInfo.setChallengeName(challengeName);
            return this;
        }

        public Builder withSubmissionDate(String submissionDate) {
            challengeDashBoardInfo.setSubmissionDate(submissionDate);
            return this;
        }

        public Builder withCurrentPhase(PhaseType currentPhase) {
            challengeDashBoardInfo.setCurrentPhase(currentPhase);
            return this;
        }

        public Builder withCurrentPhaseSubmissionDate(String currentPhaseSubmissionDate) {
            challengeDashBoardInfo.setCurrentPhaseSubmissionDate(currentPhaseSubmissionDate);
            return this;
        }

        public Builder withNumberOfSubmissions(Integer numberOfSubmissions) {
            challengeDashBoardInfo.setNumberOfSubmissions(numberOfSubmissions);
            return this;
        }

        public Builder withScore(Double score) {
            challengeDashBoardInfo.setScore(score);
            return this;
        }

        public Builder withRank(Integer rank) {
            challengeDashBoardInfo.setRank(rank);
            return this;
        }

        public Builder withPrize(Integer prize) {
            challengeDashBoardInfo.setPrize(prize);
            return this;
        }

        public Builder withDisqualified(Boolean disqualified) {
            challengeDashBoardInfo.setDisqualified(disqualified);
            return this;
        }

        public Builder withCriteria(Set<ChallengeRegistrantCriteria> criteria) {
            challengeDashBoardInfo.setCriteria(criteria);
            return this;
        }

        public Builder withSubmissions(List<ChallengeSubmissionEntity> submissions) {
            challengeDashBoardInfo.setSubmissions(submissions);
            return this;
        }

        public Builder withChallengeType(ChallengeTypeEnum challengeType) {
            challengeDashBoardInfo.setChallengeType(challengeType);
            return this;
        }

        public ChallengeDashBoardInfo build() {
            return challengeDashBoardInfo;
        }
    }
}
