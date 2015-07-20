package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 7/20/15.
 */
public class ProjectRegistrantDto {

    private Long projectId;

    private String projectEmail;

    private String projectFirstName;

    private String projectLastName;

    private String phoneNumber;

    private String resumeLink;

    private Language lang;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectEmail() {
        return projectEmail;
    }

    public void setProjectEmail(String projectEmail) {
        this.projectEmail = projectEmail;
    }

    public String getProjectFirstName() {
        return projectFirstName;
    }

    public void setProjectFirstName(String projectFirstName) {
        this.projectFirstName = projectFirstName;
    }

    public String getProjectLastName() {
        return projectLastName;
    }

    public void setProjectLastName(String projectLastName) {
        this.projectLastName = projectLastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
