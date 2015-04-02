package com.techlooper.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

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

  private List<CompanySize> companySizes;

  private String website;

  private String address;

  private List<CompanySkillEntity> skills;

  private List<CompanyBenefit> companyBenefits;

  private List<CompanyJob> companyJobs;

  private List<CompanyEmployee> companyEmployees;

  private List<CompanyIndustry> companyIndustries;

  public List<CompanyIndustry> getCompanyIndustries() {
    return companyIndustries;
  }

  public void setCompanyIndustries(List<CompanyIndustry> companyIndustries) {
    this.companyIndustries = companyIndustries;
  }

  public List<CompanyEmployee> getCompanyEmployees() {
    return companyEmployees;
  }

  public void setCompanyEmployees(List<CompanyEmployee> companyEmployees) {
    this.companyEmployees = companyEmployees;
  }

  public List<CompanyJob> getCompanyJobs() {
    return companyJobs;
  }

  public void setCompanyJobs(List<CompanyJob> companyJobs) {
    this.companyJobs = companyJobs;
  }

  public List<CompanyBenefit> getCompanyBenefits() {
    return companyBenefits;
  }

  public void setCompanyBenefits(List<CompanyBenefit> companyBenefits) {
    this.companyBenefits = companyBenefits;
  }

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

  public List<CompanySize> getCompanySizes() {
    return companySizes;
  }

  public void setCompanySizes(List<CompanySize> companySizes) {
    this.companySizes = companySizes;
  }
}
