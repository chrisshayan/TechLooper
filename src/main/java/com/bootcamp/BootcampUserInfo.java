package com.bootcamp;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by phuonghqh on 1/29/15.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BootcampUserInfo {

  private String firstName;

  private String lastName;

  private String currentJobTitle;

  private String emailAddress;

  private String phoneNumber;

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

  public String getCurrentJobTitle() {
    return currentJobTitle;
  }

  public void setCurrentJobTitle(String currentJobTitle) {
    this.currentJobTitle = currentJobTitle;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }
}
