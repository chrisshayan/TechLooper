package com.techlooper.entity;

import com.techlooper.model.Language;

/**
 * Created by NguyenDangKhoa on 7/6/15.
 */
public class ChallengeRegistrantDto {

    private Long challengeId;

    private String registrantEmail;

    private String registrantFirstName;

    private String registrantLastName;

    private Language lang;

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
}
