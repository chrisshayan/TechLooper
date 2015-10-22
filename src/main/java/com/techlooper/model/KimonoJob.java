package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 8/11/15.
 */
public class KimonoJob {

    private HtmlLink jobTitle;

    private String company;

    private String salary;

    private String location;

    private HtmlLink companyLogoUrl;

    public HtmlLink getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(HtmlLink jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public HtmlLink getCompanyLogoUrl() {
        return companyLogoUrl;
    }

    public void setCompanyLogoUrl(HtmlLink companyLogoUrl) {
        this.companyLogoUrl = companyLogoUrl;
    }
}
