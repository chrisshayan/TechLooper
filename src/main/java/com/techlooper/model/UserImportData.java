package com.techlooper.model;

/**
 * Created by khoa-nd on 27/01/15.
 */
public class UserImportData {

  private String email;

  private String originalEmail;

  private String fullName;

  private SocialProvider crawlerSource;

  private String username;

  private String imageUrl;

  private String location;

  private String dateJoin;

  private String company;

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
}
