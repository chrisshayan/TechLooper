package com.techlooper.entity;

import com.techlooper.model.ChallengePhaseEnum;
import com.techlooper.model.Language;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.data.elasticsearch.annotations.FieldType.Boolean;
import static org.springframework.data.elasticsearch.annotations.FieldType.Double;
import static org.springframework.data.elasticsearch.annotations.FieldType.Integer;
import static org.springframework.data.elasticsearch.annotations.FieldType.Long;
import static org.springframework.data.elasticsearch.annotations.FieldType.String;

/**
 * Created by NguyenDangKhoa on 7/3/15.
 */
@Document(indexName = "techlooper", type = "challengeRegistrant")
public class ChallengeRegistrantEntity {

    @Id
    private Long registrantId;

    @Field(type = String, index = FieldIndex.not_analyzed)
    private String registrantEmail;

    @Field(type = Long)
    private Long challengeId;

    @Field(type = String)
    private String registrantFirstName;

    @Field(type = String)
    private String registrantLastName;

    @Field(type = Boolean)
    private Boolean mailSent;

    @Field(type = String, index = FieldIndex.not_analyzed)
    private Language lang;

    @Field(type = Double)
    private Double score;

    @Field(type = Boolean)
    private Boolean disqualified;

    @Field(type = String, index = FieldIndex.not_analyzed)
    private String disqualifiedReason;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "dd/MM/yyyy HH:mm")
    private String lastEmailSentDateTime;

    @Field(type = Integer)
    private int lastEmailSentResultCode;

    @Field(type = String, index = FieldIndex.not_analyzed)
    private ChallengePhaseEnum activePhase;

    @Field(type = FieldType.Nested)
    private Set<ChallengeRegistrantCriteria> criteria;

    @Field(type = FieldType.Integer)
    private Integer passCode;

    public Set<ChallengeRegistrantCriteria> getCriteria() {
        if (criteria == null) criteria = new HashSet<>();
        return criteria;
    }

    public void setCriteria(Set<ChallengeRegistrantCriteria> criteria) {
        this.criteria = criteria;
    }

    public java.lang.String getDisqualifiedReason() {
        return disqualifiedReason;
    }

    public void setDisqualifiedReason(java.lang.String disqualifiedReason) {
        this.disqualifiedReason = disqualifiedReason;
    }

    public java.lang.Boolean getDisqualified() {
        return disqualified;
    }

    public void setDisqualified(java.lang.Boolean disqualified) {
        this.disqualified = disqualified;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Long getRegistrantId() {
        return registrantId;
    }

    public void setRegistrantId(Long registrantId) {
        this.registrantId = registrantId;
    }

    public Long getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(Long challengeId) {
        this.challengeId = challengeId;
    }

    public String getRegistrantEmail() {
        return registrantEmail;
    }

    public void setRegistrantEmail(String registrantEmail) {
        this.registrantEmail = registrantEmail;
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

    public Boolean getMailSent() {
        return mailSent;
    }

    public void setMailSent(Boolean mailSent) {
        this.mailSent = mailSent;
    }

    public Language getLang() {
        return lang;
    }

    public void setLang(Language lang) {
        this.lang = lang;
    }

    public String getLastEmailSentDateTime() {
        return lastEmailSentDateTime;
    }

    public void setLastEmailSentDateTime(String lastEmailSentDateTime) {
        this.lastEmailSentDateTime = lastEmailSentDateTime;
    }

    public int getLastEmailSentResultCode() {
        return lastEmailSentResultCode;
    }

    public void setLastEmailSentResultCode(int lastEmailSentResultCode) {
        this.lastEmailSentResultCode = lastEmailSentResultCode;
    }

    public ChallengePhaseEnum getActivePhase() {
        return activePhase;
    }

    public void setActivePhase(ChallengePhaseEnum activePhase) {
        this.activePhase = activePhase;
    }

    public Integer getPassCode() {
        return passCode;
    }

    public void setPassCode(Integer passCode) {
        this.passCode = passCode;
    }
}
