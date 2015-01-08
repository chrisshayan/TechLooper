package com.techlooper.model;

import javax.validation.constraints.NotNull;

/**
 * Created by phuonghqh on 12/12/14.
 */
public class SocialRequest {

  @NotNull
  private String key;

  private String emailAddress;

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }
}
