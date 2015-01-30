package com.techlooper.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khoa-nd on 27/01/15.
 */
public class UserImportData {

  @JsonProperty("email")
  private String email;

  private String originalEmail;

  @JsonProperty("fullname")
  private String fullName;

  @JsonProperty("crawlersource")
  private SocialProvider crawlerSource;

  @JsonProperty("username")
  private String username;

  @JsonProperty("imageurl")
  private String imageUrl;

  @JsonProperty("location")
  private String location;

  @JsonProperty("datejoin")
  private String dateJoin;

  @JsonProperty("company")
  private String company;

  @JsonProperty("website")
  private String website;

  @JsonProperty("description")
  private String description;

  @JsonProperty("followers")
  private String followers;

  @JsonProperty("following")
  private String following;

  @JsonProperty("organizations")
  private List<String> organizations;

  @JsonProperty("popular_repos")
  private List<String> popularRepositories;

  @JsonProperty("contributed_repos")
  private List<String> contributedRepositories;

  @JsonProperty("contribute_longest_streak_total")
  private String contributedLongestStreakTotal;

  @JsonProperty("last_contributed_datetime")
  private String lastContributedDateTime;

  @JsonProperty("contribute_number_last_year")
  private String contributeNumberLastYear;

  @JsonProperty("contribute_number_last_year_period")
  private String contributeNumberLastYearPeriod;

  @JsonProperty("contribute_longest_streak_period")
  private String contributeLongestStreakPeriod;

  @JsonProperty("contribute_current_streak_total")
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
