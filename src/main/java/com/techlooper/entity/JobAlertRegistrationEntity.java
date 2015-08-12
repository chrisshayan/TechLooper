package com.techlooper.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

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

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "dd/MM/yyyy")
    private String createdDate;

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
}
