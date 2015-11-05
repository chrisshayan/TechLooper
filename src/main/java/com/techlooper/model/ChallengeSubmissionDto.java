package com.techlooper.model;

import java.io.Serializable;

/**
 * Created by NguyenDangKhoa on 10/6/15.
 */
public class ChallengeSubmissionDto implements Serializable {

    private Long challengeSubmissionId;

    private String registrantEmail;

    private String registrantFirstName;

    private String registrantLastName;

    private Long challengeId;

    private Long registrantId;

    private String submissionURL;

    private String submissionDescription;

    private String submissionDateTime;

    private ChallengePhaseEnum submissionPhase;

    private Language lang;

    public ChallengePhaseEnum getSubmissionPhase() {
        return submissionPhase;
    }

    public void setSubmissionPhase(ChallengePhaseEnum submissionPhase) {
        this.submissionPhase = submissionPhase;
    }

    public Long getChallengeSubmissionId() {
        return challengeSubmissionId;
    }

    public void setChallengeSubmissionId(Long challengeSubmissionId) {
        this.challengeSubmissionId = challengeSubmissionId;
    }

    public Long getRegistrantId() {
        return registrantId;
    }

    public void setRegistrantId(Long registrantId) {
        this.registrantId = registrantId;
    }

    public String getRegistrantFirstName() {
        return registrantFirstName;
    }

    public void setRegistrantFirstName(String registrantFirstName) {
        this.registrantFirstName = registrantFirstName;
    }

    public String getRegistrantLastName() {
        return registrantLastName;
    }

    public void setRegistrantLastName(String registrantLastName) {
        this.registrantLastName = registrantLastName;
    }

    public String getRegistrantEmail() {
        return registrantEmail;
    }

    public void setRegistrantEmail(String registrantEmail) {
        this.registrantEmail = registrantEmail;
    }

    public Long getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(Long challengeId) {
        this.challengeId = challengeId;
    }

    public String getSubmissionDateTime() {
        return submissionDateTime;
    }

    public void setSubmissionDateTime(String submissionDateTime) {
        this.submissionDateTime = submissionDateTime;
    }

    public String getSubmissionURL() {
        return submissionURL;
    }

    public void setSubmissionURL(String submissionURL) {
        this.submissionURL = submissionURL;
    }

    public String getSubmissionDescription() {
        return submissionDescription;
    }

    public void setSubmissionDescription(String submissionDescription) {
        this.submissionDescription = submissionDescription;
    }

    public Language getLang() {
        return lang;
    }

    public void setLang(Language lang) {
        this.lang = lang;
    }
}
