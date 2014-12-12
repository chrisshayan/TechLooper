package com.techlooper.entity;



import org.dozer.Mapping;

import java.io.Serializable;
import java.util.List;

/**
 * Created by NguyenDangKhoa on 12/10/14.
 */
public class LinkedInProfile implements Serializable {

  private String firstName;
  private String lastName;
  private String headline;
  private String industry;
  private String location;
  private Integer numConnections;
  private String profilePictureUrl;
  private String publicProfileUrl;
  private Integer numRecommenders;
  private List<Position> threeCurrentPositions;
  private String summary;

  private List<String> skills;

  private List<PhoneNumber> phoneNumbers;

  private String interests;

  private List<TwitterAccount> twitterAccounts;

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getHeadline() {
    return headline;
  }

  public void setHeadline(String headline) {
    this.headline = headline;
  }

  public String getIndustry() {
    return industry;
  }

  public void setIndustry(String industry) {
    this.industry = industry;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Integer getNumConnections() {
    return numConnections;
  }

  public void setNumConnections(Integer numConnections) {
    this.numConnections = numConnections;
  }

  public String getProfilePictureUrl() {
    return profilePictureUrl;
  }

  public void setProfilePictureUrl(String profilePictureUrl) {
    this.profilePictureUrl = profilePictureUrl;
  }

  public String getPublicProfileUrl() {
    return publicProfileUrl;
  }

  public void setPublicProfileUrl(String publicProfileUrl) {
    this.publicProfileUrl = publicProfileUrl;
  }

  public Integer getNumRecommenders() {
    return numRecommenders;
  }

  public void setNumRecommenders(Integer numRecommenders) {
    this.numRecommenders = numRecommenders;
  }

  public List<Position> getThreeCurrentPositions() {
    return threeCurrentPositions;
  }

  public void setThreeCurrentPositions(List<Position> threeCurrentPositions) {
    this.threeCurrentPositions = threeCurrentPositions;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public List<String> getSkills() {
    return skills;
  }

  public void setSkills(List<String> skills) {
    this.skills = skills;
  }

  public List<PhoneNumber> getPhoneNumbers() {
    return phoneNumbers;
  }

  public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
    this.phoneNumbers = phoneNumbers;
  }

  public String getInterests() {
    return interests;
  }

  public void setInterests(String interests) {
    this.interests = interests;
  }

  public List<TwitterAccount> getTwitterAccounts() {
    return twitterAccounts;
  }

  public void setTwitterAccounts(List<TwitterAccount> twitterAccounts) {
    this.twitterAccounts = twitterAccounts;
  }
}
