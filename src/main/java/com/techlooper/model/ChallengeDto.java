package com.techlooper.model;

import java.util.List;
import java.util.Set;

/**
 * Created by NguyenDangKhoa on 6/29/15.
 */
public class ChallengeDto {

    private Long challengeId;

    private String challengeName;

    private String businessRequirement;

    private String generalNote;

    private List<String> technologies;

    private String documents;

    private String deliverables;

    private List<String> receivedEmails;

    private String reviewStyle;

    private String startDate;

    private String registrationDate;

    private String ideaSubmissionDate;

    private String uxSubmissionDate;

    private String prototypeSubmissionDate;

    private String submissionDate;

    private Integer firstPlaceReward;

    private Integer secondPlaceReward;

    private Integer thirdPlaceReward;

    private String qualityIdea;

    private String authorEmail;

    private Language lang;

    private String challengeOverview;

    private Set<ChallengeWinner> winners;

    private ChallengeTypeEnum challengeType;

    private List<String> companyDomains;

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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getIdeaSubmissionDate() {
        return ideaSubmissionDate;
    }

    public void setIdeaSubmissionDate(String ideaSubmissionDate) {
        this.ideaSubmissionDate = ideaSubmissionDate;
    }

    public String getUxSubmissionDate() {
        return uxSubmissionDate;
    }

    public void setUxSubmissionDate(String uxSubmissionDate) {
        this.uxSubmissionDate = uxSubmissionDate;
    }

    public String getPrototypeSubmissionDate() {
        return prototypeSubmissionDate;
    }

    public void setPrototypeSubmissionDate(String prototypeSubmissionDate) {
        this.prototypeSubmissionDate = prototypeSubmissionDate;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
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

    public Set<ChallengeWinner> getWinners() {
        return winners;
    }

    public void setWinners(Set<ChallengeWinner> winners) {
        this.winners = winners;
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
}
