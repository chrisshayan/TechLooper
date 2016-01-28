package com.techlooper.entity;

import com.techlooper.model.Language;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;

import static org.springframework.data.elasticsearch.annotations.FieldType.Long;
import static org.springframework.data.elasticsearch.annotations.FieldType.String;

/**
 * Created by phuonghqh on 1/26/16.
 */
@Document(indexName = "techlooper", type = "draftRegistrant")
public class DraftRegistrantEntity {

  @Id
  private Long registrantId;

  @Field(type = String, index = FieldIndex.not_analyzed)
  private String registrantEmail;

  @Field(type = String, index = FieldIndex.not_analyzed)
  private String registrantInternalEmail;

  @Field(type = Long)
  private Long challengeId;

  @Field(type = String)
  private String registrantFirstName;

  @Field(type = String)
  private String registrantLastName;

  @Field(type = Long)
  private Integer passcode;

  @Field(type = String, index = FieldIndex.not_analyzed)
  private Language lang;

  public java.lang.Long getRegistrantId() {
    return registrantId;
  }

  public void setRegistrantId(java.lang.Long registrantId) {
    this.registrantId = registrantId;
  }

  public java.lang.String getRegistrantEmail() {
    return registrantEmail;
  }

  public void setRegistrantEmail(java.lang.String registrantEmail) {
    this.registrantEmail = registrantEmail;
  }

  public java.lang.String getRegistrantInternalEmail() {
    return registrantInternalEmail;
  }

  public void setRegistrantInternalEmail(java.lang.String registrantInternalEmail) {
    this.registrantInternalEmail = registrantInternalEmail;
  }

  public java.lang.Long getChallengeId() {
    return challengeId;
  }

  public void setChallengeId(java.lang.Long challengeId) {
    this.challengeId = challengeId;
  }

  public java.lang.String getRegistrantFirstName() {
    return registrantFirstName;
  }

  public void setRegistrantFirstName(java.lang.String registrantFirstName) {
    this.registrantFirstName = registrantFirstName;
  }

  public java.lang.String getRegistrantLastName() {
    return registrantLastName;
  }

  public void setRegistrantLastName(java.lang.String registrantLastName) {
    this.registrantLastName = registrantLastName;
  }

  public Integer getPasscode() {
    return passcode;
  }

  public void setPasscode(Integer passcode) {
    this.passcode = passcode;
  }

  public Language getLang() {
    return lang;
  }

  public void setLang(Language lang) {
    this.lang = lang;
  }
}
