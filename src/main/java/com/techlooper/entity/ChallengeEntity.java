package com.techlooper.entity;

import com.techlooper.model.ChallengePhaseEnum;
import com.techlooper.model.ChallengeTypeEnum;
import com.techlooper.model.ChallengeWinner;
import com.techlooper.model.Language;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.data.elasticsearch.annotations.FieldType.Boolean;
import static org.springframework.data.elasticsearch.annotations.FieldType.*;
import static org.springframework.data.elasticsearch.annotations.FieldType.Integer;
import static org.springframework.data.elasticsearch.annotations.FieldType.String;

/**
 * Created by NguyenDangKhoa on 6/29/15.
 */
@Document(indexName = "techlooper", type = "challenge")
public class ChallengeEntity {

    @Id
    private Long challengeId;

    @Field(type = Nested)
    private Set<ChallengeCriteria> criteria;

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

    @Field(type = Date, format = DateFormat.custom, pattern = "dd/MM/yyyy")
    private String startDateTime;

    @Field(type = Date, format = DateFormat.custom, pattern = "dd/MM/yyyy")
    private String registrationDateTime;

    @Field(type = Date, format = DateFormat.custom, pattern = "dd/MM/yyyy")
    private String ideaSubmissionDateTime;

    @Field(type = Date, format = DateFormat.custom, pattern = "dd/MM/yyyy")
    private String uxSubmissionDateTime;

    @Field(type = Date, format = DateFormat.custom, pattern = "dd/MM/yyyy")
    private String prototypeSubmissionDateTime;

    @Field(type = Date, format = DateFormat.custom, pattern = "dd/MM/yyyy")
    private String submissionDateTime;

    @Field(type = Integer)
    private Integer firstPlaceReward;

    @Field(type = Integer)
    private Integer secondPlaceReward;

    @Field(type = Integer)
    private Integer thirdPlaceReward;

    @Field(type = String)
    private String qualityIdea;

    @Field(type = String)
    private String authorEmail;

    @Field(type = String)
    private Language lang;

    @Field(type = Boolean)
    private Boolean expired;

    @Field(type = Date, format = DateFormat.custom, pattern = "dd/MM/yyyy HH:mm")
    private String lastEmailSentDateTime;

    @Field(type = Integer)
    private int lastEmailSentResultCode;

    @Field(type = Nested)
    private Set<ChallengeWinner> winners;

    @Field(type = String, index = FieldIndex.not_analyzed)
    private ChallengeTypeEnum challengeType;

    @Field(type = String)
    private List<String> companyDomains;

    public Set<ChallengeWinner> getWinners() {
        if (winners == null) winners = new HashSet<>();
        return winners;
    }

    public void setWinners(Set<ChallengeWinner> winners) {
        this.winners = winners;
    }

    public Set<ChallengeCriteria> getCriteria() {
        if (criteria == null) criteria = new HashSet<>();
        return criteria;
    }

    public void setCriteria(Set<ChallengeCriteria> criteria) {
        this.criteria = criteria;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

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

    public String getIdeaSubmissionDateTime() {
        return ideaSubmissionDateTime;
    }

    public void setIdeaSubmissionDateTime(String ideaSubmissionDateTime) {
        this.ideaSubmissionDateTime = ideaSubmissionDateTime;
    }

    public String getUxSubmissionDateTime() {
        return uxSubmissionDateTime;
    }

    public void setUxSubmissionDateTime(String uxSubmissionDateTime) {
        this.uxSubmissionDateTime = uxSubmissionDateTime;
    }

    public String getPrototypeSubmissionDateTime() {
        return prototypeSubmissionDateTime;
    }

    public void setPrototypeSubmissionDateTime(String prototypeSubmissionDateTime) {
        this.prototypeSubmissionDateTime = prototypeSubmissionDateTime;
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

    public ChallengeTypeEnum getChallengeType() {
        return challengeType;
    }

    public void setChallengeType(ChallengeTypeEnum challengeType) {
        this.challengeType = challengeType;
    }

    public List<String> getCompanyDomains() {
        return companyDomains;
    }

    public void setCompanyDomains(List<String> companyDomains) {
        this.companyDomains = companyDomains;
    }

    public boolean hasPhase(ChallengePhaseEnum phase) {
        switch (phase) {
            case IDEA:
                return StringUtils.hasText(ideaSubmissionDateTime);

            case UIUX:
                return StringUtils.hasText(uxSubmissionDateTime);

            case PROTOTYPE:
                return StringUtils.hasText(prototypeSubmissionDateTime);

            default:
                return true;
        }
    }

    public String phaseStartDate(ChallengePhaseEnum phase) {
        switch (phase) {
            case REGISTRATION:
                return startDateTime;

            case IDEA:
                return registrationDateTime;

            case UIUX:
                return hasPhase(ChallengePhaseEnum.IDEA) ? ideaSubmissionDateTime : phaseStartDate(ChallengePhaseEnum.IDEA);

            case PROTOTYPE:
                return hasPhase(ChallengePhaseEnum.UIUX) ? uxSubmissionDateTime : phaseStartDate(ChallengePhaseEnum.UIUX);

            case FINAL:
                return hasPhase(ChallengePhaseEnum.PROTOTYPE) ? prototypeSubmissionDateTime : phaseStartDate(ChallengePhaseEnum.PROTOTYPE);

            default:
                return submissionDateTime;
        }
    }
}
