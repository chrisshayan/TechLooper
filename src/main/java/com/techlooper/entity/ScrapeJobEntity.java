package com.techlooper.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import static org.springframework.data.elasticsearch.annotations.FieldType.String;

@Document(indexName = "techlooper", type = "job")
public class ScrapeJobEntity {

    @Id
    private String jobTitleUrl;

    @Field(type = String)
    private String jobTitle;

    @Field(type = String)
    private String company;

    @Field(type = String)
    private String salary;

    @Field(type = String)
    private String location;

    @Field(type = String, index = FieldIndex.not_analyzed)
    private String companyLogoUrl;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "dd/MM/yyyy")
    private String createdDateTime;

    @Field(type = String, index = FieldIndex.not_analyzed)
    private String crawlSource;

    public String getJobTitleUrl() {
        return jobTitleUrl;
    }

    public void setJobTitleUrl(String jobTitleUrl) {
        this.jobTitleUrl = jobTitleUrl;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCompanyLogoUrl() {
        return companyLogoUrl;
    }

    public void setCompanyLogoUrl(String companyLogoUrl) {
        this.companyLogoUrl = companyLogoUrl;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getCrawlSource() {
        return crawlSource;
    }

    public void setCrawlSource(String crawlSource) {
        this.crawlSource = crawlSource;
    }
}
