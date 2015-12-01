package com.techlooper.model;

import com.techlooper.entity.ChallengeCriteria;

import java.util.List;
import java.util.Set;

/**
 * Created by NguyenDangKhoa on 7/3/15.
 */
public class ChallengeDetailDto {

    private Long challengeId;

    private String challengeName;

    private String challengeOverview;

    private String businessRequirement;

    private String generalNote;

    private List<String> technologies;

    private String documents;

    private String deliverables;

    private String reviewStyle;

    private String startDateTime;

    private String registrationDateTime;

    private String ideaSubmissionDateTime;

    private String uxSubmissionDateTime;

    private String prototypeSubmissionDateTime;

    private String submissionDateTime;

    private Integer firstPlaceReward;

    private Integer secondPlaceReward;

    private Integer thirdPlaceReward;

    private String qualityIdea;

    private Long numberOfRegistrants;

    private List<String> receivedEmails;

    private Boolean expired;

    private ChallengePhaseEnum currentPhase;

    private ChallengePhaseEnum nextPhase;

    private Boolean isAuthor;

    private Boolean isClosed;

    private Set<ChallengeCriteria> criteria;

    private Set<ChallengeWinner> winners;

    private List<ChallengeRegistrantFunnelItem> phaseItems;

    public List<ChallengeRegistrantFunnelItem> getPhaseItems() {
        return phaseItems;
    }

    public void setPhaseItems(List<ChallengeRegistrantFunnelItem> phaseItems) {
        this.phaseItems = phaseItems;
    }

    public Set<ChallengeWinner> getWinners() {
        return winners;
    }

    public void setWinners(Set<ChallengeWinner> winners) {
        this.winners = winners;
    }

    public Set<ChallengeCriteria> getCriteria() {
        return criteria;
    }

    public void setCriteria(Set<ChallengeCriteria> criteria) {
        this.criteria = criteria;
    }

    public Boolean getAuthor() {
        return isAuthor;
    }

    public void setAuthor(Boolean author) {
        isAuthor = author;
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

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    public List<String> getReceivedEmails() {
        return receivedEmails;
    }

    public void setReceivedEmails(List<String> receivedEmails) {
        this.receivedEmails = receivedEmails;
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

    public String getChallengeOverview() {
        return challengeOverview;
    }

    public void setChallengeOverview(String challengeOverview) {
        this.challengeOverview = challengeOverview;
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

    public Long getNumberOfRegistrants() {
        return numberOfRegistrants;
    }

    public void setNumberOfRegistrants(Long numberOfRegistrants) {
        this.numberOfRegistrants = numberOfRegistrants;
    }

    public Boolean getIsAuthor() {
        return isAuthor;
    }

    public void setIsAuthor(Boolean isAuthor) {
        this.isAuthor = isAuthor;
    }

    public Boolean getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(Boolean isClosed) {
        this.isClosed = isClosed;
    }
}
