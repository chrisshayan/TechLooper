package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 7/20/15.
 */
public class ProjectRegistrantDto {

    private Long projectId;

    private String registrantEmail;

    private String registrantFirstName;

    private String registrantLastName;

    private String registrantPhoneNumber;

    private String resumeLink;

    private Language lang;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
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

    public String getRegistrantPhoneNumber() {
        return registrantPhoneNumber;
    }

    public void setRegistrantPhoneNumber(String registrantPhoneNumber) {
        this.registrantPhoneNumber = registrantPhoneNumber;
    }

    public String getResumeLink() {
        return resumeLink;
    }

    public void setResumeLink(String resumeLink) {
        this.resumeLink = resumeLink;
    }

    public Language getLang() {
        return lang;
    }

    public void setLang(Language lang) {
        this.lang = lang;
    }
}
