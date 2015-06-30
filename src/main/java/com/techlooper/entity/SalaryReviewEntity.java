package com.techlooper.entity;

import com.techlooper.model.SalaryReport;
import com.techlooper.model.SalaryReviewSurvey;
import com.techlooper.model.TopPaidJob;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

import static org.springframework.data.elasticsearch.annotations.FieldType.*;

/**
 * Created by phuonghqh on 5/5/15.
 * Modified by khoand on 06/08/15.
 */
@Document(indexName = "techlooper", type = "salaryReview")
public class SalaryReviewEntity {

    @Id
    private Long createdDateTime;

    @Field(type = String)
    private String jobTitle;

    @Field(type = Long)
    private List<Integer> jobLevelIds;

    @Field(type = Long)
    private Long locationId;

    @Field(type = Long)
    private Integer netSalary;

    @Field(type = String)
    private List<String> skills;

    @Field(type = String)
    private String reportTo;

    @Field(type = Long)
    private List<Long> jobCategories;

    @Field(type = Long)
    private Long companySizeId;

    @Field(type = Long)
    private Integer gender;

    @Field(type = Long)
    private Integer age;

    @Field(type = Object)
    private SalaryReport salaryReport;

    @Field(type = Object)
    private SalaryReviewSurvey salaryReviewSurvey;

    @Field(type = String)
    private String campaign;

    @Field(type = String)
    private String email;

    @Field(type = String)
    private String jobAlertEmail;

    private List<TopPaidJob> topPaidJobs;

    public Long getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Long createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public List<Integer> getJobLevelIds() {
        return jobLevelIds;
    }

    public void setJobLevelIds(List<Integer> jobLevelIds) {
        this.jobLevelIds = jobLevelIds;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Integer getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(Integer netSalary) {
        this.netSalary = netSalary;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public String getReportTo() {
        return reportTo;
    }

    public void setReportTo(String reportTo) {
        this.reportTo = reportTo;
    }

    public List<Long> getJobCategories() {
        return jobCategories;
    }

    public void setJobCategories(List<Long> jobCategories) {
        this.jobCategories = jobCategories;
    }

    public Long getCompanySizeId() {
        return companySizeId;
    }

    public void setCompanySizeId(Long companySizeId) {
        this.companySizeId = companySizeId;
    }

    public void setSalaryReport(SalaryReport salaryReport) {
        this.salaryReport = salaryReport;
    }

    public SalaryReviewSurvey getSalaryReviewSurvey() {
        return salaryReviewSurvey;
    }

    public void setSalaryReviewSurvey(SalaryReviewSurvey salaryReviewSurvey) {
        this.salaryReviewSurvey = salaryReviewSurvey;
    }

    public String getCampaign() {
        return campaign;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJobAlertEmail() {
        return jobAlertEmail;
    }

    public void setJobAlertEmail(String jobAlertEmail) {
        this.jobAlertEmail = jobAlertEmail;
    }

    public List<TopPaidJob> getTopPaidJobs() {
        return topPaidJobs;
    }

    public void setTopPaidJobs(List<TopPaidJob> topPaidJobs) {
        this.topPaidJobs = topPaidJobs;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public SalaryReport getSalaryReport() {
        return salaryReport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SalaryReviewEntity that = (SalaryReviewEntity) o;

        if (!createdDateTime.equals(that.createdDateTime)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return createdDateTime.hashCode();
    }
}
