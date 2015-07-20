package com.techlooper.model;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 7/10/15.
 */
public class ProjectDto {

    private Long projectId;

    private String projectTitle;

    private String projectDescription;

    private List<String> skills;

    private String payMethod;

    private String estimatedEndDate;

    private Double budget;

    private String estimatedDuration;

    private String estimatedWorkload;

    private Double hourlyRate;

    private Long numberOfHires;

    private String authorEmail;

    private Long numberOfApplications;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getEstimatedEndDate() {
        return estimatedEndDate;
    }

    public void setEstimatedEndDate(String estimatedEndDate) {
        this.estimatedEndDate = estimatedEndDate;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public String getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(String estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public String getEstimatedWorkload() {
        return estimatedWorkload;
    }

    public void setEstimatedWorkload(String estimatedWorkload) {
        this.estimatedWorkload = estimatedWorkload;
    }

    public Double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public Long getNumberOfHires() {
        return numberOfHires;
    }

    public void setNumberOfHires(Long numberOfHires) {
        this.numberOfHires = numberOfHires;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public Long getNumberOfApplications() {
        return numberOfApplications;
    }

    public void setNumberOfApplications(Long numberOfApplications) {
        this.numberOfApplications = numberOfApplications;
    }
}
