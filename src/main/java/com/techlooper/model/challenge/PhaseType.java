package com.techlooper.model.challenge;

import com.techlooper.model.ChallengePhaseEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by NguyenDangKhoa on 1/21/16.
 */
public abstract class PhaseType {

    private ChallengePhaseEnum phaseName;

    private String submissionDate;

    public static PhaseType newChallengePhase(ChallengePhaseEnum phaseName) {
        if (phaseName == null) {
            return new RegistrationPhase();
        }
        switch (phaseName) {
            case REGISTRATION:
                return new RegistrationPhase();
            default:
                throw new IllegalArgumentException("Incorrect Challenge Phase Name");
        }
    }

    boolean exist() {
        return StringUtils.isNotEmpty(getSubmissionDate());
    }

    public ChallengePhaseEnum getPhaseName() {
        return phaseName;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }
}
