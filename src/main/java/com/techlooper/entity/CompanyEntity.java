package com.techlooper.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phuonghqh on 4/1/15.
 */
@Document(indexName = "techlooper", type = "company")
public class CompanyEntity {

  @Id
  private Long companyId;

  private String companyLogoURL;

  private String companyName;

  private String companyCategory;

  private Long companySizeId;

  private String website;

  private String address;

  private List<CompanySkillEntity> skills;

  private List<CompanyBenefit> benefits;

  private List<CompanyJob> jobs;

  private List<Object> employees = new ArrayList<>();

  private List<CompanyIndustry> industries;

  public Long getCompanyId() {
    return companyId;
  }

  public void setCompanyId(Long companyId) {
    this.companyId = companyId;
  }

  public String getCompanyLogoURL() {
    return companyLogoURL;
  }

  public void setCompanyLogoURL(String companyLogoURL) {
    this.companyLogoURL = companyLogoURL;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getCompanyCategory() {
    return companyCategory;
  }

  public void setCompanyCategory(String companyCategory) {
    this.companyCategory = companyCategory;
  }

  public Long getCompanySizeId() {
    return companySizeId;
  }

  public void setCompanySizeId(Long companySizeId) {
    this.companySizeId = companySizeId;
  }

  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    this.website = website;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public List<CompanySkillEntity> getSkills() {
    return skills;
  }

  public void setSkills(List<CompanySkillEntity> skills) {
    this.skills = skills;
  }

  public List<CompanyBenefit> getBenefits() {
    return benefits;
  }

  public void setBenefits(List<CompanyBenefit> benefits) {
    this.benefits = benefits;
  }

  public List<CompanyJob> getJobs() {
    return jobs;
  }

  public void setJobs(List<CompanyJob> jobs) {
    this.jobs = jobs;
  }

  public List<Object> getEmployees() {
    return employees;
  }

  public void setEmployees(List<Object> employees) {
    this.employees = employees;
  }

  public List<CompanyIndustry> getIndustries() {
    return industries;
  }

  public void setIndustries(List<CompanyIndustry> industries) {
    this.industries = industries;
  }
}
