package com.techlooper.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Date;
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

  @JsonProperty("resumeid")
  private Long resumeId;

  @JsonProperty("languageid")
  private int languageId;

  @JsonProperty("resumetitl")
  private String resumeTitle;

  @JsonProperty("alias")
  private String alias;

  @JsonProperty("userId")
  private Long userId;

  @JsonProperty("birthday")
  private Date birthday;

  @JsonProperty("genderid")
  private int genderId;

  @JsonProperty("maritalstatusid")
  private int maritalStatusId;

  @JsonProperty("address")
  private String address;

  @JsonProperty("countryid")
  private int countryId;

  @JsonProperty("cityname")
  private String cityName;

  @JsonProperty("districtid")
  private int districtId;

  @JsonProperty("locationmin")
  private int locationMin;

  @JsonProperty("locationmax")
  private int locationMax;

  @JsonProperty("homephone")
  private String homePhone;

  @JsonProperty("cellphone")
  private String cellPhone;

  @JsonProperty("emailaddress")
  private String emailAddress;

  @JsonProperty("desiredjobtitle")
  private String desiredJobTitle;

  @JsonProperty("joblevelid")
  private int jobLevelId;

  @JsonProperty("desiredjoblevelid")
  private int desiredJobLevelId;

  @JsonProperty("companysizeid")
  private int companySizeId;

  @JsonProperty("mostrecentemployer")
  private String mostRecentEmployer;

  @JsonProperty("mostrecentposition")
  private String mostRecentPosition;

  @JsonProperty("yearsexperienceid")
  private int yearsExperienceId;

  @JsonProperty("workexperience")
  private String workexperience;

  @JsonProperty("skill")
  private String skill;

  @JsonProperty("education")
  private String education;

  @JsonProperty("highestdegreeid")
  private String highestDegreeId;

  @JsonProperty("addinfo")
  private String addInfo;

  @JsonProperty("referenceperson")
  private String referencePerson;

  @JsonProperty("jobdescription")
  private String jobDescription;

  @JsonProperty("careerhighlight")
  private String careerHighlight;

  @JsonProperty("jobcategoryid")
  private int jobCategoryId;

  @JsonProperty("relocateid")
  private int relocateId;

  @JsonProperty("willingtravelid")
  private int willingTravelId;

  @JsonProperty("currentsalary")
  private Long currentSalary;

  @JsonProperty("typeworkingid")
  private int typeWorkingId;

  @JsonProperty("suggestedsalary")
  private Long suggestedSalary;

  @JsonProperty("salaryrangeid")
  private int salaryRangeId;

  @JsonProperty("nationalityid")
  private int nationalityId;

  @JsonProperty("language")
  private int language;

  @JsonProperty("language1")
  private int language1;

  @JsonProperty("languagelevel1")
  private int languageLevel1;

  @JsonProperty("language2")
  private int language2;

  @JsonProperty("languagelevel2")
  private int languageLevel2;

  @JsonProperty("language3")
  private int language3;

  @JsonProperty("languagelevel3")
  private int languageLevel3;

  @JsonProperty("language4")
  private int language4;

  @JsonProperty("languagelevel4")
  private int languageLevel4;

  @JsonProperty("createddate")
  private Date createdDate;

  @JsonProperty("resumepathattachedfile")
  private String resumePathAttachedFile;

  @JsonProperty("resumecontent")
  private String resumeContent;

  @JsonProperty("userpathpicturefile")
  private String userPathPictureFile;

  @JsonProperty("profile")
  private String profile;

  @JsonProperty("bio")
  private String bio;

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

  public Long getResumeId() {
    return resumeId;
  }

  public void setResumeId(Long resumeId) {
    this.resumeId = resumeId;
  }

  public int getLanguageId() {
    return languageId;
  }

  public void setLanguageId(int languageId) {
    this.languageId = languageId;
  }

  public String getResumeTitle() {
    return resumeTitle;
  }

  public void setResumeTitle(String resumeTitle) {
    this.resumeTitle = resumeTitle;
  }

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Date getBirthday() {
    return birthday;
  }

  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }

  public int getGenderId() {
    return genderId;
  }

  public void setGenderId(int genderId) {
    this.genderId = genderId;
  }

  public int getMaritalStatusId() {
    return maritalStatusId;
  }

  public void setMaritalStatusId(int maritalStatusId) {
    this.maritalStatusId = maritalStatusId;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public int getCountryId() {
    return countryId;
  }

  public void setCountryId(int countryId) {
    this.countryId = countryId;
  }

  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public int getDistrictId() {
    return districtId;
  }

  public void setDistrictId(int districtId) {
    this.districtId = districtId;
  }

  public int getLocationMin() {
    return locationMin;
  }

  public void setLocationMin(int locationMin) {
    this.locationMin = locationMin;
  }

  public int getLocationMax() {
    return locationMax;
  }

  public void setLocationMax(int locationMax) {
    this.locationMax = locationMax;
  }

  public String getHomePhone() {
    return homePhone;
  }

  public void setHomePhone(String homePhone) {
    this.homePhone = homePhone;
  }

  public String getCellPhone() {
    return cellPhone;
  }

  public void setCellPhone(String cellPhone) {
    this.cellPhone = cellPhone;
  }

  public String getDesiredJobTitle() {
    return desiredJobTitle;
  }

  public void setDesiredJobTitle(String desiredJobTitle) {
    this.desiredJobTitle = desiredJobTitle;
  }

  public int getJobLevelId() {
    return jobLevelId;
  }

  public void setJobLevelId(int jobLevelId) {
    this.jobLevelId = jobLevelId;
  }

  public int getDesiredJobLevelId() {
    return desiredJobLevelId;
  }

  public void setDesiredJobLevelId(int desiredJobLevelId) {
    this.desiredJobLevelId = desiredJobLevelId;
  }

  public int getCompanySizeId() {
    return companySizeId;
  }

  public void setCompanySizeId(int companySizeId) {
    this.companySizeId = companySizeId;
  }

  public String getMostRecentEmployer() {
    return mostRecentEmployer;
  }

  public void setMostRecentEmployer(String mostRecentEmployer) {
    this.mostRecentEmployer = mostRecentEmployer;
  }

  public String getMostRecentPosition() {
    return mostRecentPosition;
  }

  public void setMostRecentPosition(String mostRecentPosition) {
    this.mostRecentPosition = mostRecentPosition;
  }

  public int getYearsExperienceId() {
    return yearsExperienceId;
  }

  public void setYearsExperienceId(int yearsExperienceId) {
    this.yearsExperienceId = yearsExperienceId;
  }

  public String getWorkexperience() {
    return workexperience;
  }

  public void setWorkexperience(String workexperience) {
    this.workexperience = workexperience;
  }

  public String getSkill() {
    return skill;
  }

  public void setSkill(String skill) {
    this.skill = skill;
  }

  public String getEducation() {
    return education;
  }

  public void setEducation(String education) {
    this.education = education;
  }

  public String getHighestDegreeId() {
    return highestDegreeId;
  }

  public void setHighestDegreeId(String highestDegreeId) {
    this.highestDegreeId = highestDegreeId;
  }

  public String getAddInfo() {
    return addInfo;
  }

  public void setAddInfo(String addInfo) {
    this.addInfo = addInfo;
  }

  public String getReferencePerson() {
    return referencePerson;
  }

  public void setReferencePerson(String referencePerson) {
    this.referencePerson = referencePerson;
  }

  public String getJobDescription() {
    return jobDescription;
  }

  public void setJobDescription(String jobDescription) {
    this.jobDescription = jobDescription;
  }

  public String getCareerHighlight() {
    return careerHighlight;
  }

  public void setCareerHighlight(String careerHighlight) {
    this.careerHighlight = careerHighlight;
  }

  public int getJobCategoryId() {
    return jobCategoryId;
  }

  public void setJobCategoryId(int jobCategoryId) {
    this.jobCategoryId = jobCategoryId;
  }

  public int getRelocateId() {
    return relocateId;
  }

  public void setRelocateId(int relocateId) {
    this.relocateId = relocateId;
  }

  public int getWillingTravelId() {
    return willingTravelId;
  }

  public void setWillingTravelId(int willingTravelId) {
    this.willingTravelId = willingTravelId;
  }

  public Long getCurrentSalary() {
    return currentSalary;
  }

  public void setCurrentSalary(Long currentSalary) {
    this.currentSalary = currentSalary;
  }

  public int getTypeWorkingId() {
    return typeWorkingId;
  }

  public void setTypeWorkingId(int typeWorkingId) {
    this.typeWorkingId = typeWorkingId;
  }

  public Long getSuggestedSalary() {
    return suggestedSalary;
  }

  public void setSuggestedSalary(Long suggestedSalary) {
    this.suggestedSalary = suggestedSalary;
  }

  public int getSalaryRangeId() {
    return salaryRangeId;
  }

  public void setSalaryRangeId(int salaryRangeId) {
    this.salaryRangeId = salaryRangeId;
  }

  public int getNationalityId() {
    return nationalityId;
  }

  public void setNationalityId(int nationalityId) {
    this.nationalityId = nationalityId;
  }

  public int getLanguage() {
    return language;
  }

  public void setLanguage(int language) {
    this.language = language;
  }

  public int getLanguage1() {
    return language1;
  }

  public void setLanguage1(int language1) {
    this.language1 = language1;
  }

  public int getLanguageLevel1() {
    return languageLevel1;
  }

  public void setLanguageLevel1(int languageLevel1) {
    this.languageLevel1 = languageLevel1;
  }

  public int getLanguage2() {
    return language2;
  }

  public void setLanguage2(int language2) {
    this.language2 = language2;
  }

  public int getLanguageLevel2() {
    return languageLevel2;
  }

  public void setLanguageLevel2(int languageLevel2) {
    this.languageLevel2 = languageLevel2;
  }

  public int getLanguage3() {
    return language3;
  }

  public void setLanguage3(int language3) {
    this.language3 = language3;
  }

  public int getLanguageLevel3() {
    return languageLevel3;
  }

  public void setLanguageLevel3(int languageLevel3) {
    this.languageLevel3 = languageLevel3;
  }

  public int getLanguage4() {
    return language4;
  }

  public void setLanguage4(int language4) {
    this.language4 = language4;
  }

  public int getLanguageLevel4() {
    return languageLevel4;
  }

  public void setLanguageLevel4(int languageLevel4) {
    this.languageLevel4 = languageLevel4;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public String getResumePathAttachedFile() {
    return resumePathAttachedFile;
  }

  public void setResumePathAttachedFile(String resumePathAttachedFile) {
    this.resumePathAttachedFile = resumePathAttachedFile;
  }

  public String getResumeContent() {
    return resumeContent;
  }

  public void setResumeContent(String resumeContent) {
    this.resumeContent = resumeContent;
  }

  public String getUserPathPictureFile() {
    return userPathPictureFile;
  }

  public void setUserPathPictureFile(String userPathPictureFile) {
    this.userPathPictureFile = userPathPictureFile;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public String getProfile() {
    return profile;
  }

  public void setProfile(String profile) {
    this.profile = profile;
  }

  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }
}
