package com.techlooper.entity;

import java.util.Date;
import java.util.List;

/**
 * Created by NguyenDangKhoa on 6/29/15.
 */
public class ContestEntity {

    private String challengeName;

    private String businessRequirement;

    private String generalNote;

    private List<String> technologies;

    private String document;

    private String deliverables;

    private List<String> emails;

    private String reviewStyle;

    private Date dateChallengeStart;

    private Date dateChallengeRegister;

    private Date dateChallengeSubmit;

    private Integer firstReward;

    private Integer secondReward;

    private Integer thirdReward;

    private String quality;

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

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getDeliverables() {
        return deliverables;
    }

    public void setDeliverables(String deliverables) {
        this.deliverables = deliverables;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public String getReviewStyle() {
        return reviewStyle;
    }

    public void setReviewStyle(String reviewStyle) {
        this.reviewStyle = reviewStyle;
    }

    public Date getDateChallengeStart() {
        return dateChallengeStart;
    }

    public void setDateChallengeStart(Date dateChallengeStart) {
        this.dateChallengeStart = dateChallengeStart;
    }

    public Date getDateChallengeRegister() {
        return dateChallengeRegister;
    }

    public void setDateChallengeRegister(Date dateChallengeRegister) {
        this.dateChallengeRegister = dateChallengeRegister;
    }

    public Date getDateChallengeSubmit() {
        return dateChallengeSubmit;
    }

    public void setDateChallengeSubmit(Date dateChallengeSubmit) {
        this.dateChallengeSubmit = dateChallengeSubmit;
    }

    public Integer getFirstReward() {
        return firstReward;
    }

    public void setFirstReward(Integer firstReward) {
        this.firstReward = firstReward;
    }

    public Integer getSecondReward() {
        return secondReward;
    }

    public void setSecondReward(Integer secondReward) {
        this.secondReward = secondReward;
    }

    public Integer getThirdReward() {
        return thirdReward;
    }

    public void setThirdReward(Integer thirdReward) {
        this.thirdReward = thirdReward;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }
}
