package com.techlooper.entity;

import java.util.List;

/**
 * Created by phuonghqh on 12/12/14.
 */
public class Company {

    private String companyId;

    private String websiteUrl;

    private String name;

    private String companyLogoURL;

    private String logoUrl;

    private List<Long> employerIds;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyLogoURL() {
        return companyLogoURL;
    }

    public void setCompanyLogoURL(String companyLogoURL) {
        this.companyLogoURL = companyLogoURL;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public List<Long> getEmployerIds() {
        return employerIds;
    }

    public void setEmployerIds(List<Long> employerIds) {
        this.employerIds = employerIds;
    }
}
