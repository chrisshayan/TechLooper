package com.techlooper.entity;

import com.techlooper.model.ChallengePhaseEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import static org.springframework.data.elasticsearch.annotations.FieldType.Boolean;
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
    private ChallengePhaseEnum submissionPhase;

    @Field(type = Boolean)
    private Boolean isRead;

    public java.lang.Long getRegistrantId() {
        return registrantId;
    }

    public void setRegistrantId(java.lang.Long registrantId) {
        this.registrantId = registrantId;
    }

    public java.lang.Long getChallengeSubmissionId() {
        return challengeSubmissionId;
    }

    public void setChallengeSubmissionId(java.lang.Long challengeSubmissionId) {
        this.challengeSubmissionId = challengeSubmissionId;
    }

    public java.lang.String getRegistrantName() {
        return registrantName;
    }

    public void setRegistrantName(java.lang.String registrantName) {
        this.registrantName = registrantName;
    }

    public java.lang.Long getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(java.lang.Long challengeId) {
        this.challengeId = challengeId;
    }

    public java.lang.String getSubmissionURL() {
        return submissionURL;
    }

    public void setSubmissionURL(java.lang.String submissionURL) {
        this.submissionURL = submissionURL;
    }

    public java.lang.String getSubmissionDateTime() {
        return submissionDateTime;
    }

    public void setSubmissionDateTime(java.lang.String submissionDateTime) {
        this.submissionDateTime = submissionDateTime;
    }

    public java.lang.String getSubmissionDescription() {
        return submissionDescription;
    }

    public void setSubmissionDescription(java.lang.String submissionDescription) {
        this.submissionDescription = submissionDescription;
    }

    public ChallengePhaseEnum getSubmissionPhase() {
        return submissionPhase;
    }

    public void setSubmissionPhase(ChallengePhaseEnum submissionPhase) {
        this.submissionPhase = submissionPhase;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public static class ChallengeSubmissionEntityBuilder {
        private ChallengeSubmissionEntity challengeSubmissionEntity;

        private ChallengeSubmissionEntityBuilder() {
            challengeSubmissionEntity = new ChallengeSubmissionEntity();
        }

        private ChallengeSubmissionEntityBuilder(ChallengeSubmissionEntity challengeSubmissionEntity) {
            this.challengeSubmissionEntity = challengeSubmissionEntity;
        }

        public ChallengeSubmissionEntityBuilder withChallengeSubmissionId(Long challengeSubmissionId) {
            challengeSubmissionEntity.challengeSubmissionId = challengeSubmissionId;
            return this;
        }

        public ChallengeSubmissionEntityBuilder withRegistrantId(Long registrantId) {
            challengeSubmissionEntity.registrantId = registrantId;
            return this;
        }

        public ChallengeSubmissionEntityBuilder withRegistrantName(String registrantName) {
            challengeSubmissionEntity.registrantName = registrantName;
            return this;
        }

        public ChallengeSubmissionEntityBuilder withChallengeId(Long challengeId) {
            challengeSubmissionEntity.challengeId = challengeId;
            return this;
        }

        public ChallengeSubmissionEntityBuilder withSubmissionURL(String submissionURL) {
            challengeSubmissionEntity.submissionURL = submissionURL;
            return this;
        }

        public ChallengeSubmissionEntityBuilder withSubmissionDateTime(String submissionDateTime) {
            challengeSubmissionEntity.submissionDateTime = submissionDateTime;
            return this;
        }

        public ChallengeSubmissionEntityBuilder withSubmissionDescription(String submissionDescription) {
            challengeSubmissionEntity.submissionDescription = submissionDescription;
            return this;
        }

        public ChallengeSubmissionEntityBuilder withSubmissionPhase(ChallengePhaseEnum submissionPhase) {
            challengeSubmissionEntity.submissionPhase = submissionPhase;
            return this;
        }

        public ChallengeSubmissionEntityBuilder withIsRead(Boolean isRead) {
            challengeSubmissionEntity.isRead = isRead;
            return this;
        }

        public static ChallengeSubmissionEntityBuilder challengeSubmissionEntity() {
            return new ChallengeSubmissionEntityBuilder();
        }

        public static ChallengeSubmissionEntityBuilder challengeSubmissionEntity(ChallengeSubmissionEntity entity) {
            return new ChallengeSubmissionEntityBuilder(entity);
        }

        public ChallengeSubmissionEntity build() {
            return challengeSubmissionEntity;
        }
    }
}
