package com.techlooper.entity;

import com.techlooper.model.GetPromotedResponse;
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
    private List<Long> jobCategories;

    @Field(type = String)
    private String email;

    @Field(type = Boolean)
    private Boolean hasResult;

    @Field(type = Nested)
    private GetPromotedResponse getPromotedResult;

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

    public List<Long> getJobCategories() {
        return jobCategories;
    }

    public void setJobCategories(List<Long> jobCategories) {
        this.jobCategories = jobCategories;
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

    public GetPromotedResponse getGetPromotedResult() {
        return getPromotedResult;
    }

    public void setGetPromotedResult(GetPromotedResponse getPromotedResult) {
        this.getPromotedResult = getPromotedResult;
    }
}
