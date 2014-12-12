package com.techlooper.model;

/**
 * Created by phuonghqh on 12/11/14.
 */
public class SocialAccessToken {

  private String token;

  public SocialAccessToken(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
