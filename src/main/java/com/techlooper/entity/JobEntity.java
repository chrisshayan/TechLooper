package com.techlooper.entity;

import com.techlooper.model.JobSkill;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

import static org.springframework.data.elasticsearch.annotations.FieldType.Long;
import static org.springframework.data.elasticsearch.annotations.FieldType.String;

/**
 * Created by chrisshayan on 7/10/14.
 */
@Document(indexName = "vietnamworks", type = "job")
public class JobEntity {

    @Id
    private String id;

    @Field(type = String, store = true, indexAnalyzer = "index_analyzer", searchAnalyzer = "search_analyzer")
    private String jobTitle;

    @Field(type = Long, store = true)
    private Long salaryMin;

    @Field(type = Long, store = true)
    private Long salaryMax;

    @Field(type = String, store = true, indexAnalyzer = "index_analyzer", searchAnalyzer = "search_analyzer")
    private String companyDesc;

    @Field(type = FieldType.Nested)
    private List<JobSkill> skills;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
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

    public String getCompanyDesc() {
        return companyDesc;
    }

    public void setCompanyDesc(String companyDesc) {
        this.companyDesc = companyDesc;
    }

    public List<JobSkill> getSkills() {
        return skills;
    }

    public void setSkills(List<JobSkill> skills) {
        this.skills = skills;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JobEntity jobEntity = (JobEntity) o;

        if (!id.equals(jobEntity.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
