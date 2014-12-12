package com.techlooper.entity;

import com.techlooper.model.SocialProvider;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import java.util.Map;

/**
 * Created by NguyenDangKhoa on 12/10/14.
 */
@Document
public class UserEntity {

  @Id
  private String id;

  @Field
  private String emailAddress;

  @Field
  private String firstName;

  @Field
  private String lastName;

  @Field
  private SocialProvider loginSource;

  @Field
  private Map<SocialProvider, ProfileEntity> profiles;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

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

  public SocialProvider getLoginSource() {
    return loginSource;
  }

  public void setLoginSource(SocialProvider loginSource) {
    this.loginSource = loginSource;
  }

  public Map<SocialProvider, ProfileEntity> getProfiles() {
    return profiles;
  }

  public void setProfiles(Map<SocialProvider, ProfileEntity> profiles) {
    this.profiles = profiles;
  }

  public static class Builder {
    private UserEntity instance = new UserEntity();

    public Builder withLoginSource(SocialProvider socialProvider) {
      instance.loginSource = socialProvider;
      return this;
    }

    public Builder withLastName(String lastName) {
      instance.lastName = lastName;
      return this;
    }

    public Builder withEmailAddress(String email) {
      instance.id = email;
      instance.emailAddress = email;
      return this;
    }

    public Builder withFirstName(String firstName) {
      instance.firstName = firstName;
      return this;
    }

    public UserEntity build() {
      return instance;
    }
  }
}
