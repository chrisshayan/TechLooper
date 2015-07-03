package com.techlooper.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import static org.springframework.data.elasticsearch.annotations.FieldType.Long;
import static org.springframework.data.elasticsearch.annotations.FieldType.String;

/**
 * Created by NguyenDangKhoa on 7/3/15.
 */
@Document(indexName = "techlooper", type = "challengeRegistrant")
public class ChallengeRegistrantEntity {

    @Id
    private Long registrantId;

    @Field(type = Long)
    private Long challengeId;

    @Field(type = String)
    private String registrantEmail;

    @Field(type = String)
    private String registrantFirstName;

    @Field(type = String)
    private String registrantLastName;

    public Long getRegistrantId() {
        return registrantId;
    }

    public void setRegistrantId(Long registrantId) {
        this.registrantId = registrantId;
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
}
