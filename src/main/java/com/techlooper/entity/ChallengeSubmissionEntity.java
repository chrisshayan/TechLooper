package com.techlooper.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import static org.springframework.data.elasticsearch.annotations.FieldType.Long;
import static org.springframework.data.elasticsearch.annotations.FieldType.String;

@Document(indexName = "techlooper", type = "challengeSubmission")
public class ChallengeSubmissionEntity {

    @Id
    private Long challengeSubmissionId;

    @Field(type = Long)
    private Long registrantId;

    @Field(type = String)
    private String registrantName;

    @Field(type = Long)
    private Long challengeId;

    @Field(type = String)
    private String submissionURL;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "dd/MM/yyyy")
    private String submissionDateTime;

    @Field(type = String)
    private String submissionDescription;

    @Field(type = String, index = FieldIndex.not_analyzed)
    private String submissionStatus;

    public ChallengeSubmissionEntity() {
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

    public String getSubmissionURL() {
        return submissionURL;
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
