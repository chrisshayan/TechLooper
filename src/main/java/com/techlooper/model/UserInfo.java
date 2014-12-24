package com.techlooper.model;

import com.techlooper.entity.AccessGrant;
import com.techlooper.entity.UserEntity;
import org.springframework.data.annotation.Id;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by phuonghqh on 12/12/14.
 */
public class UserInfo {

  private String id;

  private String emailAddress;

  private String firstName;

  private String lastName;

  private SocialProvider loginSource;

  private String key;

  private String username;

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

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
