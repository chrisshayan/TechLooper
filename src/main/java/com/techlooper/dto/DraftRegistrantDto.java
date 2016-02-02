package com.techlooper.dto;

import com.techlooper.model.Language;

/**
 * Created by phuonghqh on 2/2/16.
 */
public class DraftRegistrantDto {
  private Long registrantId;
  private String registrantEmail;
  private String registrantInternalEmail;
  private Long challengeId;
  private String registrantFirstName;
  private String registrantLastName;
  private Language lang;

  public Long getRegistrantId() {
    return registrantId;
  }

  public void setRegistrantId(Long registrantId) {
    this.registrantId = registrantId;
  }

  public String getRegistrantEmail() {
    return registrantEmail;
  }

  public void setRegistrantEmail(String registrantEmail) {
    this.registrantEmail = registrantEmail;
  }

  public String getRegistrantInternalEmail() {
    return registrantInternalEmail;
  }

  public void setRegistrantInternalEmail(String registrantInternalEmail) {
    this.registrantInternalEmail = registrantInternalEmail;
  }

  public Long getChallengeId() {
    return challengeId;
  }

  public void setChallengeId(Long challengeId) {
    this.challengeId = challengeId;
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
}
