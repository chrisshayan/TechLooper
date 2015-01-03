package com.techlooper.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by phuonghqh on 1/3/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubApiUser {

  @JsonProperty("avatar_url")
  private String profileImageUrl;

  public String getProfileImageUrl() {
    return profileImageUrl;
  }

  public void setProfileImageUrl(String profileImageUrl) {
    this.profileImageUrl = profileImageUrl;
  }
}
