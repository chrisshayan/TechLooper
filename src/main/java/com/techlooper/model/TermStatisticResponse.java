package com.techlooper.model;

import com.techlooper.entity.Company;
import com.techlooper.entity.CompanyEntity;

import java.util.List;

/**
 * Created by phuonghqh on 4/17/15.
 */
public class TermStatisticResponse {

  private String term;

  private Integer jobLevelId;

  private Double averageSalaryMin;

  private Double averageSalaryMax;

  private List<Company> companies;

  private List<SkillStatistic> skills;

  public String getTerm() {
    return term;
  }

  public void setTerm(String term) {
    this.term = term;
  }

  public Integer getJobLevelId() {
    return jobLevelId;
  }

  public void setJobLevelId(Integer jobLevelId) {
    this.jobLevelId = jobLevelId;
  }

  public Double getAverageSalaryMin() {
    return averageSalaryMin;
  }

  public void setAverageSalaryMin(Double averageSalaryMin) {
    this.averageSalaryMin = averageSalaryMin;
  }

  public Double getAverageSalaryMax() {
    return averageSalaryMax;
  }

  public void setAverageSalaryMax(Double averageSalaryMax) {
    this.averageSalaryMax = averageSalaryMax;
  }

  public List<Company> getCompanies() {
    return companies;
  }

  public void setCompanies(List<Company> companies) {
    this.companies = companies;
  }

  public List<SkillStatistic> getSkills() {
    return skills;
  }

  public void setSkills(List<SkillStatistic> skills) {
    this.skills = skills;
  }
}
