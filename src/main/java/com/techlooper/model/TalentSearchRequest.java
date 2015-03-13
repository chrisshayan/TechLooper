package com.techlooper.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NguyenDangKhoa on 3/10/15.
 */
public class TalentSearchRequest {

    private List<String> skills = new ArrayList<>();

    private List<String> locations = new ArrayList<>();

    private List<String> companies = new ArrayList<>();

    private String sortByField = "profiles.GITHUB.numberOfRepositories";

    private int pageIndex = 0;

    private int pageSize = 20;

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public List<String> getCompanies() {
        return companies;
    }

    public void setCompanies(List<String> companies) {
        this.companies = companies;
    }

    public String getSortByField() {
        return sortByField;
    }

    public void setSortByField(String sortByField) {
        this.sortByField = sortByField;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public static class Builder {

        private TalentSearchRequest instance = new TalentSearchRequest();

        public Builder withSkills(String skill) {
            instance.skills.add(skill);
            return this;
        }

        public Builder withSkills(List<String> skills) {
            instance.skills.addAll(skills);
            return this;
        }

        public Builder withLocations(String location) {
            instance.locations.add(location);
            return this;
        }

        public Builder withLocations(List<String> locations) {
            instance.locations.addAll(locations);
            return this;
        }

        public Builder withCompanies(String company) {
            instance.companies.add(company);
            return this;
        }

        public Builder withCompanies(List<String> companies) {
            instance.companies.addAll(companies);
            return this;
        }

        public Builder withSortByField(String sortByField) {
            instance.sortByField = sortByField;
            return this;
        }

        public Builder withPageSize(int pageSize) {
            instance.pageSize = pageSize;
            return this;
        }

        public Builder withPageIndex(int pageIndex) {
            instance.pageIndex = pageIndex;
            return this;
        }

        public TalentSearchRequest build() {
            return instance;
        }
    }
}
