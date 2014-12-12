package com.techlooper.entity;

import com.techlooper.model.SocialProvider;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import java.io.Serializable;
import java.util.HashMap;
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
  private Map<SocialProvider, Serializable> profiles = new HashMap<>();

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

  public Map<SocialProvider, Serializable> getProfiles() {
    return profiles;
  }

  public void setProfiles(Map<SocialProvider, Serializable> profiles) {
    this.profiles = profiles;
  }

  public static class Builder {

    private UserEntity instance = new UserEntity();

    public Builder() {}

    public Builder(UserEntity userEntity) {
      instance = userEntity;
    }

    public static Builder get(UserEntity userEntity) {
      return new Builder(userEntity);
    }

    public static Builder get() {
      return new Builder();
    }

    public Builder withId(String id) {
      instance.id = id;
      return this;
    }

    public Builder withEmailAddress(String emailAddress) {
      instance.emailAddress = emailAddress;
      return this;
    }

    public Builder withFirstName(String firstName) {
      instance.firstName = firstName;
      return this;
    }

    public Builder withLastName(String lastName) {
      instance.lastName = lastName;
      return this;
    }

    public Builder withLoginSource(SocialProvider loginSource) {
      instance.loginSource = loginSource;
      return this;
    }

    public Builder withProfiles(Map<SocialProvider, Serializable> profiles) {
      instance.profiles = profiles;
      return this;
    }

    public Builder withProfile(SocialProvider socialProvider, Serializable profile) {
      instance.profiles.put(socialProvider, profile);
      return this;
    }

    public UserEntity build() {
      return instance;
    }
  }
}
