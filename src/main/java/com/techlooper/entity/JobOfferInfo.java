package com.techlooper.entity;

import com.techlooper.model.JobLevelEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Created by phuonghqh on 5/5/15.
 */
@Document(indexName = "jobOffer")
public class JobOfferInfo {

  @Id
  private Long id;

  @Field
  private String jobTitle;

  @Field(type = FieldType.String)
  private JobLevelEnum jobLevel;

  @Field
  private String location;

  @Field
  private Integer baseSalary;

  @Field
  private Integer bonus;

  @Field
  private Integer profitShare;

  @Field
  private String[] skills;

  @Field
  private String reportTo;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getJobTitle() {
    return jobTitle;
  }

  public void setJobTitle(String jobTitle) {
    this.jobTitle = jobTitle;
  }

  public JobLevelEnum getJobLevel() {
    return jobLevel;
  }

  public void setJobLevel(JobLevelEnum jobLevel) {
    this.jobLevel = jobLevel;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Integer getBaseSalary() {
    return baseSalary;
  }

  public void setBaseSalary(Integer baseSalary) {
    this.baseSalary = baseSalary;
  }

  public Integer getBonus() {
    return bonus;
  }

  public void setBonus(Integer bonus) {
    this.bonus = bonus;
  }

  public Integer getProfitShare() {
    return profitShare;
  }

  public void setProfitShare(Integer profitShare) {
    this.profitShare = profitShare;
  }

  public String[] getSkills() {
    return skills;
  }

  public void setSkills(String[] skills) {
    this.skills = skills;
  }

  public String getReportTo() {
    return reportTo;
  }

  public void setReportTo(String reportTo) {
    this.reportTo = reportTo;
  }
}
