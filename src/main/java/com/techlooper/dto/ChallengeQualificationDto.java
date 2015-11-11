package com.techlooper.dto;

import com.techlooper.model.ChallengePhaseEnum;
import com.techlooper.model.QualificationCriteriaEnum;

/**
 * Created by NguyenDangKhoa on 11/10/15.
 */
public class ChallengeQualificationDto {

    private Long challengeId;

    private ChallengePhaseEnum currentPhase;

    private ChallengePhaseEnum nextPhase;

    private QualificationCriteriaEnum qualificationCriteria;

    public Long getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(Long challengeId) {
        this.challengeId = challengeId;
    }

    public ChallengePhaseEnum getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(ChallengePhaseEnum currentPhase) {
        this.currentPhase = currentPhase;
    }

    public ChallengePhaseEnum getNextPhase() {
        return nextPhase;
    }

    public void setNextPhase(ChallengePhaseEnum nextPhase) {
        this.nextPhase = nextPhase;
    }

    public QualificationCriteriaEnum getQualificationCriteria() {
        return qualificationCriteria;
    }

    public void setQualificationCriteria(QualificationCriteriaEnum qualificationCriteria) {
        this.qualificationCriteria = qualificationCriteria;
    }
}
