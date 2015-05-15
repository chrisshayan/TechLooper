package com.techlooper.entity;

import com.techlooper.model.SalaryReport;
import com.techlooper.model.SalaryReviewSurvey;
import com.techlooper.model.TopPaidJob;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

/**
 * Created by phuonghqh on 5/5/15.
 */
@Document(indexName = "techlooper", type = "salaryReview")
public class SalaryReview {

    @Id
    private Long createdDateTime;

    @Field
    private String jobTitle;

    @Field
    private List<Integer> jobLevelIds;

    @Field
    private Long locationId;

    @Field
    private Integer netSalary;

    @Field
    private List<String> skills;

    @Field
    private String reportTo;

    @Field
    private List<Long> jobCategories;

    @Field
    private Long companySizeId;

    @Field
    private int gender;

    @Field
    private int age;

    @Field(type = FieldType.Nested)
    private SalaryReport salaryReport;

    private List<TopPaidJob> topPaidJobs;

    @Field(type = FieldType.Nested)
    private SalaryReviewSurvey salaryReviewSurvey;

    @Field
    private String campaign;

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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public SalaryReport getSalaryReport() {
        return salaryReport;
    }

    public void setSalaryReport(SalaryReport salaryReport) {
        this.salaryReport = salaryReport;
    }

    public List<TopPaidJob> getTopPaidJobs() {
        return topPaidJobs;
    }

    public void setTopPaidJobs(List<TopPaidJob> topPaidJobs) {
        this.topPaidJobs = topPaidJobs;
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
}
