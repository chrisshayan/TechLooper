package com.techlooper.entity;

import com.techlooper.model.ChallengePhaseEnum;
import com.techlooper.model.ChallengeSubmissionDto;
import com.techlooper.model.Language;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 7/6/15.
 */
public class ChallengeRegistrantDto {

  private Long challengeId;

  private String registrantEmail;

  private String registrantFirstName;

  private String registrantLastName;

  private Language lang;

  private Double score;

  private Long registrantId;

  private Boolean disqualified;

  private String disqualifiedReason;

  private String lastEmailSentDateTime;

  private List<ChallengeSubmissionDto> submissions;

  private ChallengePhaseEnum currentStatus;

  public ChallengePhaseEnum getCurrentStatus() {
    return currentStatus;
  }

  public void setCurrentStatus(ChallengePhaseEnum currentStatus) {
    this.currentStatus = currentStatus;
  }

  public String getDisqualifiedReason() {
    return disqualifiedReason;
  }

  public void setDisqualifiedReason(String disqualifiedReason) {
    this.disqualifiedReason = disqualifiedReason;
  }

  public Boolean getDisqualified() {
    return disqualified;
  }

  public void setDisqualified(Boolean disqualified) {
    this.disqualified = disqualified;
  }

  public Long getRegistrantId() {
    return registrantId;
  }

  public void setRegistrantId(Long registrantId) {
    this.registrantId = registrantId;
  }

  public Double getScore() {
    return score;
  }

  public void setScore(Double score) {
    this.score = score;
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

  public Language getLang() {
    return lang;
  }

  public void setLang(Language lang) {
    this.lang = lang;
  }

  public List<ChallengeSubmissionDto> getSubmissions() {
    return submissions;
  }

  public void setSubmissions(List<ChallengeSubmissionDto> submissions) {
    this.submissions = submissions;
  }

  public String getLastEmailSentDateTime() {
    return lastEmailSentDateTime;
  }

  public void setLastEmailSentDateTime(String lastEmailSentDateTime) {
    this.lastEmailSentDateTime = lastEmailSentDateTime;
  }
}
