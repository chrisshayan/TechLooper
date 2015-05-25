package com.techlooper.entity;

import com.techlooper.model.PriceJobReport;
import com.techlooper.model.PriceJobSurvey;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 5/21/15.
 */
@Document(indexName = "techlooper", type = "priceJob")
public class PriceJobEntity {

    @Id
    private Long createdDateTime;

    @Field
    private Integer locationId;

    @Field
    private Integer companySizeId;

    @Field
    private List<Long> jobCategories;

    @Field
    private String jobTitle;

    @Field
    private List<Integer> jobLevelIds;

    @Field
    private Integer yearsExperienceId;

    @Field
    private Integer educationRequiredId;

    @Field
    private List<String> skills;

    @Field
    private List<String> languages;

    @Field(type = FieldType.Nested)
    private PriceJobReport priceJobReport;

    @Field
    private PriceJobSurvey priceJobSurvey;

    public Long getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Long createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getCompanySizeId() {
        return companySizeId;
    }

    public void setCompanySizeId(Integer companySizeId) {
        this.companySizeId = companySizeId;
    }

    public List<Long> getJobCategories() {
        return jobCategories;
    }

    public void setJobCategories(List<Long> jobCategories) {
        this.jobCategories = jobCategories;
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

    public Integer getYearsExperienceId() {
        return yearsExperienceId;
    }

    public void setYearsExperienceId(Integer yearsExperienceId) {
        this.yearsExperienceId = yearsExperienceId;
    }

    public Integer getEducationRequiredId() {
        return educationRequiredId;
    }

    public void setEducationRequiredId(Integer educationRequiredId) {
        this.educationRequiredId = educationRequiredId;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public PriceJobReport getPriceJobReport() {
        return priceJobReport;
    }

    public void setPriceJobReport(PriceJobReport priceJobReport) {
        this.priceJobReport = priceJobReport;
    }

    public PriceJobSurvey getPriceJobSurvey() {
        return priceJobSurvey;
    }

    public void setPriceJobSurvey(PriceJobSurvey priceJobSurvey) {
        this.priceJobSurvey = priceJobSurvey;
    }
}
