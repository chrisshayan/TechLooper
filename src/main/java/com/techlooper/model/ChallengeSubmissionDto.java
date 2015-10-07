package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 10/6/15.
 */
public class ChallengeSubmissionDto {

    private Long challengeSubmissionId;

    private Long registrantId;

    private String registrantName;

    private Long challengeId;

    private String submissionURL;

    private String submissionDateTime;

    private String submissionDescription;

    private String submissionStatus;

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

    public String getRegistrantName() {
        return registrantName;
    }

    public void setRegistrantName(String registrantName) {
        this.registrantName = registrantName;
    }

    public Long getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(Long challengeId) {
        this.challengeId = challengeId;
    }

    public String getSubmissionURL() {
        return submissionURL;
    }

    public void setSubmissionURL(String submissionURL) {
        this.submissionURL = submissionURL;
    }

    public String getSubmissionDateTime() {
        return submissionDateTime;
    }

    public void setSubmissionDateTime(String submissionDateTime) {
        this.submissionDateTime = submissionDateTime;
    }

    public String getSubmissionDescription() {
        return submissionDescription;
    }

    public void setSubmissionDescription(String submissionDescription) {
        this.submissionDescription = submissionDescription;
    }

    public String getSubmissionStatus() {
        return submissionStatus;
    }

    public void setSubmissionStatus(String submissionStatus) {
        this.submissionStatus = submissionStatus;
    }
}
