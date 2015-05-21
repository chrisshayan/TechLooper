package com.techlooper.entity;

import com.techlooper.model.PriceJobReport;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 5/21/15.
 */
public class PriceJobEntity {

    private Integer locationId;

    private Integer companySizeId;

    private List<Long> jobCategories;

    private String jobTitle;

    private List<Integer> jobLevelIds;

    private Integer yearsExperienceId;

    private Integer educationRequiredId;

    private List<String> skills;

    private List<String> languages;

    private PriceJobReport priceJobReport;

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
}
