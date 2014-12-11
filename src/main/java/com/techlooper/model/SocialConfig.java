package com.techlooper.model;

/**
 * Created by phuonghqh on 12/11/14.
 */
public class SocialConfig {

  private String provider;

  private String apiKey;

  private String secretKey;

  public String getProvider() {
    return provider;
  }

  public void setProvider(String provider) {
    this.provider = provider;
  }

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public String getSecretKey() {
    return secretKey;
  }

  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }
}
