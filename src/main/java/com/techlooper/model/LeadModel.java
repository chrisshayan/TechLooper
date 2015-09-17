package com.techlooper.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by NguyenDangKhoa on 9/15/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LeadModel {

    @JsonProperty(value = "Subject")
    private String subject;

    @JsonProperty(value = "Telephone1")
    private String telephone1;

    @JsonProperty(value = "FirstName")
    private String firstName;

    @JsonProperty(value = "Qntt_Source")
    private Integer source;

    @JsonProperty(value = "LeadQualityCode")
    private String leadQualityCode;

    @JsonProperty(value = "Qntt_LegalName")
    private String legalName;

    @JsonProperty(value = "CampaignId")
    private String campaignId;

    @JsonProperty(value = "CompanyName")
    private String companyName;

    @JsonProperty(value = "MobilePhone")
    private String mobilePhone;

    @JsonProperty(value = "JobTitle")
    private String jobTitle;

    @JsonProperty(value = "EmailAddress1")
    private String emailAddress1;

    @JsonProperty(value = "EmailAddress2")
    private String emailAddress2;

    @JsonProperty(value = "Fax")
    private String fax;

    @JsonProperty(value = "WebsiteUrl")
    private String websiteUrl;

    @JsonProperty(value = "Address1_Line1")
    private String address;

    @JsonProperty(value = "Qntt_District")
    private String district;

    @JsonProperty(value = "Qntt_City")
    private Integer city;

    @JsonProperty(value = "Qntt_Country")
    private Integer country;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTelephone1() {
        return telephone1;
    }

    public void setTelephone1(String telephone1) {
        this.telephone1 = telephone1;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getLeadQualityCode() {
        return leadQualityCode;
    }

    public void setLeadQualityCode(String leadQualityCode) {
        this.leadQualityCode = leadQualityCode;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getEmailAddress1() {
        return emailAddress1;
    }

    public void setEmailAddress1(String emailAddress1) {
        this.emailAddress1 = emailAddress1;
    }

    public String getEmailAddress2() {
        return emailAddress2;
    }

    public void setEmailAddress2(String emailAddress2) {
        this.emailAddress2 = emailAddress2;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public Integer getCountry() {
        return country;
    }

    public void setCountry(Integer country) {
        this.country = country;
    }
}
