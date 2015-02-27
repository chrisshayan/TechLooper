package com.techlooper.model;

/**
 * Created by phuonghqh on 12/12/14.
 */
public enum SocialProvider {

  LINKEDIN("linkedin"), FACEBOOK("facebook"), GITHUB("github"), GOOGLE("google"), TWITTER("twitter"), VIETNAMWORKS("vietnamworks"),
  ABOUTME("aboutme"), STACKOVERFLOW("stackoverflow");

  private String provider;

  private SocialProvider(String provider) {
    this.provider = provider;
  }
}
