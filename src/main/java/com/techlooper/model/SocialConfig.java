package com.techlooper.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by phuonghqh on 12/11/14.
 */
public class SocialConfig {

  private SocialProvider provider;

  private String apiKey;

  private String secretKey;

  private String redirectUri;

  public SocialProvider getProvider() {
    return provider;
  }

  public void setProvider(SocialProvider provider) {
    this.provider = provider;
  }

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  @JsonIgnore
  public String getSecretKey() {
    return secretKey;
  }

  @JsonProperty
  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }

  public String getRedirectUri() {
    return redirectUri;
  }

  public void setRedirectUri(String redirectUri) {
    this.redirectUri = redirectUri;
  }

  public static class Builder {
    private SocialConfig instance = new SocialConfig();

    public Builder withRedirectUri(String redirectUri) {
      instance.redirectUri = redirectUri;
      return this;
    }

    public Builder withApiKey(String apiKey) {
      instance.apiKey = apiKey;
      return this;
    }

    public SocialConfig build() {
      return instance;
    }
  }
}
