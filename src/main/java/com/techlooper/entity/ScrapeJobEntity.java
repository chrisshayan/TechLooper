package com.techlooper.entity;

import com.techlooper.model.JobSkill;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.List;

import static org.springframework.data.elasticsearch.annotations.FieldType.*;

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

    @Field(type = Long)
    private Long salaryMin;

    @Field(type = Long)
    private Long salaryMax;

    @Field(type = Boolean)
    private Boolean topPriority;

    @Field(type = Nested)
    private List<CompanyBenefit> benefits;

    @Field(type = Nested)
    private List<JobSkill> skills;

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

    public Long getSalaryMin() {
        return salaryMin;
    }

    public void setSalaryMin(Long salaryMin) {
        this.salaryMin = salaryMin;
    }

    public Long getSalaryMax() {
        return salaryMax;
    }

    public void setSalaryMax(Long salaryMax) {
        this.salaryMax = salaryMax;
    }

    public Boolean getTopPriority() {
        return topPriority;
    }

    public void setTopPriority(Boolean topPriority) {
        this.topPriority = topPriority;
    }

    public List<CompanyBenefit> getBenefits() {
        return benefits;
    }

    public void setBenefits(List<CompanyBenefit> benefits) {
        this.benefits = benefits;
    }

    public List<JobSkill> getSkills() {
        return skills;
    }

    public void setSkills(List<JobSkill> skills) {
        this.skills = skills;
    }
}
