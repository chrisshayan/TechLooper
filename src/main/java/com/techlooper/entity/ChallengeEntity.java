package com.techlooper.entity;

import com.techlooper.model.Language;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

import static org.springframework.data.elasticsearch.annotations.FieldType.String;

/**
 * Created by NguyenDangKhoa on 6/29/15.
 */
@Document(indexName = "techlooper", type = "challenge")
public class ChallengeEntity {

    @Id
    private Long challengeId;

    @Field(type = String)
    private String challengeName;

    @Field(type = String)
    private String challengeOverview;

    @Field(type = String)
    private String businessRequirement;

    @Field(type = String)
    private String generalNote;

    @Field(type = String)
    private List<String> technologies;

    @Field(type = String)
    private String documents;

    @Field(type = String)
    private String deliverables;

    @Field(type = String)
    private List<String> receivedEmails;

    @Field(type = String)
    private String reviewStyle;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "dd/MM/yyyy")
    private String startDateTime;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "dd/MM/yyyy")
    private String registrationDateTime;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "dd/MM/yyyy")
    private String submissionDateTime;

    @Field(type = FieldType.Integer)
    private Integer firstPlaceReward;

    @Field(type = FieldType.Integer)
    private Integer secondPlaceReward;

    @Field(type = FieldType.Integer)
    private Integer thirdPlaceReward;

    @Field(type = String)
    private String qualityIdea;

    @Field(type = String)
    private String authorEmail;

    @Field(type = String)
    private Language lang;

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

    public String getBusinessRequirement() {
        return businessRequirement;
    }

    public void setBusinessRequirement(String businessRequirement) {
        this.businessRequirement = businessRequirement;
    }

    public String getGeneralNote() {
        return generalNote;
    }

    public void setGeneralNote(String generalNote) {
        this.generalNote = generalNote;
    }

    public List<String> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(List<String> technologies) {
        this.technologies = technologies;
    }

    public String getDocuments() {
        return documents;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }

    public String getDeliverables() {
        return deliverables;
    }

    public void setDeliverables(String deliverables) {
        this.deliverables = deliverables;
    }

    public List<String> getReceivedEmails() {
        return receivedEmails;
    }

    public void setReceivedEmails(List<String> receivedEmails) {
        this.receivedEmails = receivedEmails;
    }

    public String getReviewStyle() {
        return reviewStyle;
    }

    public void setReviewStyle(String reviewStyle) {
        this.reviewStyle = reviewStyle;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getRegistrationDateTime() {
        return registrationDateTime;
    }

    public void setRegistrationDateTime(String registrationDateTime) {
        this.registrationDateTime = registrationDateTime;
    }

    public String getSubmissionDateTime() {
        return submissionDateTime;
    }

    public void setSubmissionDateTime(String submissionDateTime) {
        this.submissionDateTime = submissionDateTime;
    }

    public Integer getFirstPlaceReward() {
        return firstPlaceReward;
    }

    public void setFirstPlaceReward(Integer firstPlaceReward) {
        this.firstPlaceReward = firstPlaceReward;
    }

    public Integer getSecondPlaceReward() {
        return secondPlaceReward;
    }

    public void setSecondPlaceReward(Integer secondPlaceReward) {
        this.secondPlaceReward = secondPlaceReward;
    }

    public Integer getThirdPlaceReward() {
        return thirdPlaceReward;
    }

    public void setThirdPlaceReward(Integer thirdPlaceReward) {
        this.thirdPlaceReward = thirdPlaceReward;
    }

    public String getQualityIdea() {
        return qualityIdea;
    }

    public void setQualityIdea(String qualityIdea) {
        this.qualityIdea = qualityIdea;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public Language getLang() {
        return lang;
    }

    public void setLang(Language lang) {
        this.lang = lang;
    }

    public String getChallengeOverview() {
        return challengeOverview;
    }

    public void setChallengeOverview(String challengeOverview) {
        this.challengeOverview = challengeOverview;
    }
}
