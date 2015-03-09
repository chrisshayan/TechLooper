package com.techlooper.entity.userimport;

import com.techlooper.model.SocialProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NguyenDangKhoa on 2/11/15.
 */
public class GithubUserImportProfile implements UserImportProfile {

    private String email;

    private String originalEmail;

    private String fullName;

    private SocialProvider crawlerSource;

    private String username;

    private String imageUrl;

    private String location;

    private String dateJoin;

    private String company;

    private String website;

    private String description;

    private String followers;

    private String following;

    private List<String> organizations;

    private List<String> popularRepositories;

    private List<String> contributedRepositories;

    private String contributedLongestStreakTotal;

    private String lastContributedDateTime;

    private String contributeNumberLastYear;

    private String contributeNumberLastYearPeriod;

    private String contributeLongestStreakPeriod;

    private String contributeCurrentStreakTotal;

    private List<String> skills = new ArrayList<>();

    private int numberOfRepositories;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOriginalEmail() {
        return originalEmail;
    }

    public void setOriginalEmail(String originalEmail) {
        this.originalEmail = originalEmail;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public SocialProvider getCrawlerSource() {
        return crawlerSource;
    }

    public void setCrawlerSource(SocialProvider crawlerSource) {
        this.crawlerSource = crawlerSource;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDateJoin() {
        return dateJoin;
    }

    public void setDateJoin(String dateJoin) {
        this.dateJoin = dateJoin;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public List<String> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<String> organizations) {
        this.organizations = organizations;
    }

    public List<String> getPopularRepositories() {
        return popularRepositories;
    }

    public void setPopularRepositories(List<String> popularRepositories) {
        this.popularRepositories = popularRepositories;
    }

    public List<String> getContributedRepositories() {
        return contributedRepositories;
    }

    public void setContributedRepositories(List<String> contributedRepositories) {
        this.contributedRepositories = contributedRepositories;
    }

    public String getContributedLongestStreakTotal() {
        return contributedLongestStreakTotal;
    }

    public void setContributedLongestStreakTotal(String contributedLongestStreakTotal) {
        this.contributedLongestStreakTotal = contributedLongestStreakTotal;
    }

    public String getLastContributedDateTime() {
        return lastContributedDateTime;
    }

    public void setLastContributedDateTime(String lastContributedDateTime) {
        this.lastContributedDateTime = lastContributedDateTime;
    }

    public String getContributeNumberLastYear() {
        return contributeNumberLastYear;
    }

    public void setContributeNumberLastYear(String contributeNumberLastYear) {
        this.contributeNumberLastYear = contributeNumberLastYear;
    }

    public String getContributeNumberLastYearPeriod() {
        return contributeNumberLastYearPeriod;
    }

    public void setContributeNumberLastYearPeriod(String contributeNumberLastYearPeriod) {
        this.contributeNumberLastYearPeriod = contributeNumberLastYearPeriod;
    }

    public String getContributeLongestStreakPeriod() {
        return contributeLongestStreakPeriod;
    }

    public void setContributeLongestStreakPeriod(String contributeLongestStreakPeriod) {
        this.contributeLongestStreakPeriod = contributeLongestStreakPeriod;
    }

    public String getContributeCurrentStreakTotal() {
        return contributeCurrentStreakTotal;
    }

    public void setContributeCurrentStreakTotal(String contributeCurrentStreakTotal) {
        this.contributeCurrentStreakTotal = contributeCurrentStreakTotal;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public int getNumberOfRepositories() {
        return numberOfRepositories;
    }

    public void setNumberOfRepositories(int numberOfRepositories) {
        this.numberOfRepositories = numberOfRepositories;
    }
}
