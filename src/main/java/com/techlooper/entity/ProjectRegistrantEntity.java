package com.techlooper.entity;

import com.techlooper.model.Language;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;

import static org.springframework.data.elasticsearch.annotations.FieldType.Boolean;
import static org.springframework.data.elasticsearch.annotations.FieldType.Long;
import static org.springframework.data.elasticsearch.annotations.FieldType.String;

/**
 * Created by NguyenDangKhoa on 7/20/15.
 */
@Document(indexName = "techlooper", type = "projectRegistrant")
public class ProjectRegistrantEntity {

    @Id
    private Long projectRegistrantId;

    @Field(type = String, index = FieldIndex.not_analyzed)
    private String registrantEmail;

    @Field(type = Long)
    private Long projectId;

    @Field(type = String)
    private String registrantFirstName;

    @Field(type = String)
    private String registrantLastName;

    @Field(type = String, index = FieldIndex.not_analyzed)
    private String registrantPhoneNumber;

    @Field(type = String)
    private String resumeLink;

    @Field(type = Boolean)
    private Boolean mailSent;

    @Field(type = String)
    private Language lang;

    public Long getProjectRegistrantId() {
        return projectRegistrantId;
    }

    public void setProjectRegistrantId(Long projectRegistrantId) {
        this.projectRegistrantId = projectRegistrantId;
    }

    public String getRegistrantEmail() {
        return registrantEmail;
    }

    public void setRegistrantEmail(String registrantEmail) {
        this.registrantEmail = registrantEmail;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
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

    public Boolean getMailSent() {
        return mailSent;
    }

    public void setMailSent(Boolean mailSent) {
        this.mailSent = mailSent;
    }

    public Language getLang() {
        return lang;
    }

    public void setLang(Language lang) {
        this.lang = lang;
    }
}
