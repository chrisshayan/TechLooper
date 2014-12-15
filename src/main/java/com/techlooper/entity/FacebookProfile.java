package com.techlooper.entity;


import org.springframework.social.facebook.api.AgeRange;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by phuonghqh on 12/15/14.
 */
public class FacebookProfile implements Serializable {
  private  String id;
  private  String username;
  private  String name;
  private  String firstName;
  private String middleName;
  private  String lastName;
  private  String gender;
  private Locale locale;
  private String link;
  private String website;
  private String email;
  private String thirdPartyId;
  private Float timezone;
  private Date updatedTime;
  private Boolean verified;
  private String about;
  private String bio;
  private String birthday;
  private Reference location;
  private Reference hometown;
  private List<String> interestedIn;
  private List<Reference> inspirationalPeople;
  private List<Reference> languages;
  private List<Reference> sports;
  private List<Reference> favoriteTeams;
  private List<Reference> favoriteAthletes;
  private String religion;
  private String political;
  private String quotes;
  private String relationshipStatus;
  private Reference significantOther;
  private List<WorkEntry> work;
  private List<EducationEntry> education;
  private AgeRange ageRange;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public Locale getLocale() {
    return locale;
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    this.website = website;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getThirdPartyId() {
    return thirdPartyId;
  }

  public void setThirdPartyId(String thirdPartyId) {
    this.thirdPartyId = thirdPartyId;
  }

  public Float getTimezone() {
    return timezone;
  }

  public void setTimezone(Float timezone) {
    this.timezone = timezone;
  }

  public Date getUpdatedTime() {
    return updatedTime;
  }

  public void setUpdatedTime(Date updatedTime) {
    this.updatedTime = updatedTime;
  }

  public Boolean getVerified() {
    return verified;
  }

  public void setVerified(Boolean verified) {
    this.verified = verified;
  }

  public String getAbout() {
    return about;
  }

  public void setAbout(String about) {
    this.about = about;
  }

  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }

  public String getBirthday() {
    return birthday;
  }

  public void setBirthday(String birthday) {
    this.birthday = birthday;
  }

  public Reference getLocation() {
    return location;
  }

  public void setLocation(Reference location) {
    this.location = location;
  }

  public Reference getHometown() {
    return hometown;
  }

  public void setHometown(Reference hometown) {
    this.hometown = hometown;
  }

  public List<String> getInterestedIn() {
    return interestedIn;
  }

  public void setInterestedIn(List<String> interestedIn) {
    this.interestedIn = interestedIn;
  }

  public List<Reference> getInspirationalPeople() {
    return inspirationalPeople;
  }

  public void setInspirationalPeople(List<Reference> inspirationalPeople) {
    this.inspirationalPeople = inspirationalPeople;
  }

  public List<Reference> getLanguages() {
    return languages;
  }

  public void setLanguages(List<Reference> languages) {
    this.languages = languages;
  }

  public List<Reference> getSports() {
    return sports;
  }

  public void setSports(List<Reference> sports) {
    this.sports = sports;
  }

  public List<Reference> getFavoriteTeams() {
    return favoriteTeams;
  }

  public void setFavoriteTeams(List<Reference> favoriteTeams) {
    this.favoriteTeams = favoriteTeams;
  }

  public List<Reference> getFavoriteAthletes() {
    return favoriteAthletes;
  }

  public void setFavoriteAthletes(List<Reference> favoriteAthletes) {
    this.favoriteAthletes = favoriteAthletes;
  }

  public String getReligion() {
    return religion;
  }

  public void setReligion(String religion) {
    this.religion = religion;
  }

  public String getPolitical() {
    return political;
  }

  public void setPolitical(String political) {
    this.political = political;
  }

  public String getQuotes() {
    return quotes;
  }

  public void setQuotes(String quotes) {
    this.quotes = quotes;
  }

  public String getRelationshipStatus() {
    return relationshipStatus;
  }

  public void setRelationshipStatus(String relationshipStatus) {
    this.relationshipStatus = relationshipStatus;
  }

  public Reference getSignificantOther() {
    return significantOther;
  }

  public void setSignificantOther(Reference significantOther) {
    this.significantOther = significantOther;
  }

  public List<WorkEntry> getWork() {
    return work;
  }

  public void setWork(List<WorkEntry> work) {
    this.work = work;
  }

  public List<EducationEntry> getEducation() {
    return education;
  }

  public void setEducation(List<EducationEntry> education) {
    this.education = education;
  }

  public AgeRange getAgeRange() {
    return ageRange;
  }

  public void setAgeRange(AgeRange ageRange) {
    this.ageRange = ageRange;
  }
}
