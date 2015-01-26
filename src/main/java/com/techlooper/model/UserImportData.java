package com.techlooper.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Created by khoa-nd on 27/01/15.
 */
public class UserImportData {

  private String email;

  private String fullName;

  private SocialProvider crawlerSource;

  private UserImportProfile profile;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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

  public UserImportProfile getProfile() {
    return profile;
  }

  public void setProfile(UserImportProfile profile) {
    this.profile = profile;
  }
}
