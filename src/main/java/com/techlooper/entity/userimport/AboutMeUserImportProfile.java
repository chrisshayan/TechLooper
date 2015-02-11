package com.techlooper.entity.userimport;

import com.techlooper.model.SocialProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NguyenDangKhoa on 2/11/15.
 */
public class AboutMeUserImportProfile implements UserImportProfile {

  private String profile;

  private String bio;

  private String username;

  private String email;

  private String fullName;

  private SocialProvider crawlerSource;

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

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public SocialProvider getCrawlerSource() {
    return crawlerSource;
  }

  public void setCrawlerSource(SocialProvider crawlerSource) {
    this.crawlerSource = crawlerSource;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }
}
