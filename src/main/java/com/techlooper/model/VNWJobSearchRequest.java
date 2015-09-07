package com.techlooper.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 10/24/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class VNWJobSearchRequest {

    @JsonProperty(value = "job_title")
    private String jobTitle;

    @JsonProperty(value = "job_location")
    private String jobLocation;

    @JsonProperty(value = "job_category")
    private String jobCategories;

    @JsonProperty(value = "job_level")
    private List<Long> jobLevel;

    @JsonProperty(value = "page_number")
    private Integer pageNumber;

    @JsonProperty(value = "job_salary")
    private Integer jobSalary;

    @JsonProperty(value = "tl_type")
    private Integer techlooperJobType;

    @JsonProperty(value = "page_size")
    private Integer pageSize;

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public String getJobCategories() {
        return jobCategories;
    }

    public void setJobCategories(String jobCategories) {
        this.jobCategories = jobCategories;
    }

    public List<Long> getJobLevel() {
        return jobLevel;
    }

    public void setJobLevel(List<Long> jobLevel) {
        this.jobLevel = jobLevel;
    }

    public Integer getJobSalary() {
        return jobSalary;
    }

    public void setJobSalary(Integer jobSalary) {
        this.jobSalary = jobSalary;
    }

    public Integer getTechlooperJobType() {
        return techlooperJobType;
    }

    public void setTechlooperJobType(Integer techlooperJobType) {
        this.techlooperJobType = techlooperJobType;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public static class Builder {

        private VNWJobSearchRequest instance = new VNWJobSearchRequest();

        public Builder withJobCategories(String jobCategories) {
            instance.jobCategories = jobCategories;
            return this;
        }

        public Builder withTechlooperJobType(Integer techlooperJobType) {
            instance.techlooperJobType = techlooperJobType;
            return this;
        }

        public Builder withPageNumber(Integer pageNumber) {
            instance.pageNumber = pageNumber;
            return this;
        }

        public Builder withPageSize(Integer pageSize) {
            instance.pageSize = pageSize;
            return this;
        }

        public VNWJobSearchRequest build() {
            return instance;
        }
    }
}
