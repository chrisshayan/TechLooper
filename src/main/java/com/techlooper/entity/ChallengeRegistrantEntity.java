package com.techlooper.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import static org.springframework.data.elasticsearch.annotations.FieldType.Boolean;
import static org.springframework.data.elasticsearch.annotations.FieldType.Long;
import static org.springframework.data.elasticsearch.annotations.FieldType.String;

/**
 * Created by NguyenDangKhoa on 7/3/15.
 */
@Document(indexName = "techlooper", type = "challengeRegistrant")
public class ChallengeRegistrantEntity {

    @Id
    private String registrantEmail;

    @Field(type = Long)
    private Long challengeId;

    @Field(type = String)
    private String registrantFirstName;

    @Field(type = String)
    private String registrantLastName;

    @Field(type = Boolean)
    private Boolean mailSent;

    public ChallengeRegistrantEntity() {}

    public ChallengeRegistrantEntity(java.lang.String registrantEmail, java.lang.String registrantFirstName, String registrantLastName) {
        this.registrantEmail = registrantEmail;
        this.registrantLastName = registrantLastName;
        this.registrantFirstName = registrantFirstName;
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
}
