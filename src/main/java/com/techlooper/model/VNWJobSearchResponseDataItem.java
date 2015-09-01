package com.techlooper.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.techlooper.entity.CompanyBenefit;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class VNWJobSearchResponseDataItem {

    public static final String JOB_LOCATION = "job_location";

    public static final String JOB_LEVEL = "top_level";

    @JsonProperty(value = "job_id")
    private Long jobId;

    @JsonProperty(value = "job_detail_url")
    private String url;

    @JsonProperty(value = "job_title")
    private String title;

    @JsonProperty(value = "job_location")
    private String location;

    @JsonProperty(value = "top_level")
    private String level;

    @JsonProperty(value = "posted_date")
    private String postedOn;

    @JsonProperty(value = "job_company")
    private String company;

    @JsonProperty(value = "job_logo_url")
    private String logoUrl;

    @JsonProperty(value = "job_video_url")
    private String videoUrl = "";

    @JsonProperty(value = "salary_min")
    private Long salaryMin;

    @JsonProperty(value = "salary_max")
    private Long salaryMax;

    @JsonProperty(value = "benefits")
    private List<CompanyBenefit> benefits;

    @JsonProperty(value = "skills")
    private List<JobSkill> skills;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(String postedOn) {
        this.postedOn = postedOn;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
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

    public JobResponse toJobResponse() {
        final JobResponse.Builder builder = new JobResponse.Builder();
        return builder.withCompany(getCompany())
                .withLevel(getLevel())
                .withLocation(getLocation())
                .withLogoUrl(getLogoUrl())
                .withPostedOn(getPostedOn())
                .withTitle(getTitle())
                .withUrl(getUrl())
                .withVideoUrl(getVideoUrl())
                .build();
    }
}
