package com.techlooper.model;

import java.io.Serializable;

/**
 * Created by NguyenDangKhoa on 10/6/15.
 */
public class ChallengeSubmissionDto implements Serializable {

  private String registrantEmail;

  private String registrantFirstName;

  private String registrantLastName;

  private Long challengeId;

  private String submissionURL;

  private String submissionDescription;

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

  public String getRegistrantEmail() {
    return registrantEmail;
  }

  public void setRegistrantEmail(String registrantEmail) {
    this.registrantEmail = registrantEmail;
  }

  public Long getChallengeId() {
    return challengeId;
  }

  public void setChallengeId(Long challengeId) {
    this.challengeId = challengeId;
  }

  public String getSubmissionURL() {
    return submissionURL;
  }

  public void setSubmissionURL(String submissionURL) {
    this.submissionURL = submissionURL;
  }

  public String getSubmissionDescription() {
    return submissionDescription;
  }

  public void setSubmissionDescription(String submissionDescription) {
    this.submissionDescription = submissionDescription;
  }
}
