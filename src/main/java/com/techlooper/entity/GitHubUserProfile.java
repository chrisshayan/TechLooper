package com.techlooper.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by phuonghqh on 12/16/14.
 */
public class GitHubUserProfile extends UserProfile implements Serializable {
  private long id;
  private String name;
  private String username;
  private String location;
  private String company;
  private String blog;
  private String email;
  private Date createdDate;
  private String profileImageUrl;
  private List<GitHubRepo> repos;

  private List<GitHubFollower> followers;

  public List<GitHubFollower> getFollowers() {
    return followers;
  }

  public void setFollowers(List<GitHubFollower> followers) {
    this.followers = followers;
  }

  public List<GitHubRepo> getRepos() {
    return repos;
  }

  public void setRepos(List<GitHubRepo> repos) {
    this.repos = repos;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public String getBlog() {
    return blog;
  }

  public void setBlog(String blog) {
    this.blog = blog;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public String getProfileImageUrl() {
    return profileImageUrl;
  }

  public void setProfileImageUrl(String profileImageUrl) {
    this.profileImageUrl = profileImageUrl;
  }
}
