package com.techlooper.entity;

import com.techlooper.model.Language;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import static org.springframework.data.elasticsearch.annotations.FieldType.Integer;
import static org.springframework.data.elasticsearch.annotations.FieldType.String;

@Document(indexName = "techlooper", type = "jobAlertRegistration")
public class JobAlertRegistrationEntity {

    @Id
    private Long jobAlertRegistrationId;

    @Field(type = String, index = FieldIndex.not_analyzed)
    private String email;

    @Field(type = String)
    private String keyword;

    @Field(type = String)
    private String location;

    @Field(type = Integer)
    private Integer locationId;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "dd/MM/yyyy")
    private String createdDate;

    @Field(type = Integer)
    private Integer bucket;

    @Field(type = String, index = FieldIndex.not_analyzed)
    private Language lang;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "dd/MM/yyyy HH:mm")
    private String lastEmailSentDateTime;

    public Long getJobAlertRegistrationId() {
        return jobAlertRegistrationId;
    }

    public void setJobAlertRegistrationId(Long jobAlertRegistrationId) {
        this.jobAlertRegistrationId = jobAlertRegistrationId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getBucket() {
        return bucket;
    }

    public void setBucket(Integer bucket) {
        this.bucket = bucket;
    }

    public Language getLang() {
        return lang;
    }

    public void setLang(Language lang) {
        this.lang = lang;
    }

    public String getLastEmailSentDateTime() {
        return lastEmailSentDateTime;
    }

    public void setLastEmailSentDateTime(String lastEmailSentDateTime) {
        this.lastEmailSentDateTime = lastEmailSentDateTime;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }
}
