package com.techlooper.entity;

import com.techlooper.model.Language;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.List;

import static org.springframework.data.elasticsearch.annotations.FieldType.String;

/**
 * Created by NguyenDangKhoa on 7/10/15.
 */
@Document(indexName = "techlooper", type = "project")
public class ProjectEntity {

    @Id
    private Long projectId;

    @Field(type = String)
    private String projectTitle;

    @Field(type = String)
    private String projectDescription;

    @Field(type = String, index = FieldIndex.not_analyzed)
    private List<String> skills;

    @Field(type = String, index = FieldIndex.not_analyzed)
    private String payMethod;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "dd/MM/yyyy")
    private String estimatedEndDate;

    @Field(type = FieldType.Double)
    private Double budget;

    @Field(type = String, index = FieldIndex.not_analyzed)
    private String estimatedDuration;

    @Field(type = String, index = FieldIndex.not_analyzed)
    private String estimatedWorkload;

    @Field(type = FieldType.Double)
    private Double hourlyRate;

    @Field(type = FieldType.Long)
    private Long numberOfHires;

    @Field(type = String, index = FieldIndex.not_analyzed)
    private String authorEmail;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "dd/MM/yyyy")
    private String createdDate;

    @Field(type = String, index = FieldIndex.not_analyzed)
    private Language lang;

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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Language getLang() {
        return lang;
    }

    public void setLang(Language lang) {
        this.lang = lang;
    }
}
