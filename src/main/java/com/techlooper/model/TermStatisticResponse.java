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

  private Double salaryMin;

  private Double salaryMax;

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

  public Double getSalaryMin() {
    return salaryMin;
  }

  public void setSalaryMin(Double salaryMin) {
    this.salaryMin = salaryMin;
  }

  public Double getSalaryMax() {
    return salaryMax;
  }

  public void setSalaryMax(Double salaryMax) {
    this.salaryMax = salaryMax;
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
