package com.techlooper.entity;

import com.techlooper.model.GetPromotedResponse;
import com.techlooper.model.GetPromotedSurvey;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.util.List;

import static org.springframework.data.elasticsearch.annotations.FieldType.*;

@Document(indexName = "techlooper", type = "getPromoted")
public class GetPromotedEntity {

    @Id
    private Long createdDateTime;

    @Field(type = String)
    private String jobTitle;

    @Field(type = Long)
    private List<Integer> jobLevelIds;

    @Field(type = Long)
    private List<Long> jobCategoryIds;

    @Field(type = String)
    private String email;

    @Field(type = Boolean)
    private Boolean hasResult;

    @Field(type = String)
    private String campaign;

    @Field(type = Nested)
    private GetPromotedResponse getPromotedResult;

    @Field(type = Nested)
    private GetPromotedSurvey getPromotedSurvey;

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

    public List<Long> getJobCategoryIds() {
        return jobCategoryIds;
    }

    public void setJobCategoryIds(List<Long> jobCategoryIds) {
        this.jobCategoryIds = jobCategoryIds;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getHasResult() {
        return hasResult;
    }

    public void setHasResult(Boolean hasResult) {
        this.hasResult = hasResult;
    }

    public String getCampaign() {
        return campaign;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public GetPromotedResponse getGetPromotedResult() {
        return getPromotedResult;
    }

    public void setGetPromotedResult(GetPromotedResponse getPromotedResult) {
        this.getPromotedResult = getPromotedResult;
    }

    public GetPromotedSurvey getGetPromotedSurvey() {
        return getPromotedSurvey;
    }

    public void setGetPromotedSurvey(GetPromotedSurvey getPromotedSurvey) {
        this.getPromotedSurvey = getPromotedSurvey;
    }
}
