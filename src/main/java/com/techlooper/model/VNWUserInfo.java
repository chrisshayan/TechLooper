package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 1/14/15.
 */
public class VNWUserInfo {

  private String email;

  private String firstname;

  private String lastname;

  public VNWUserInfo(UserInfo userInfo) {
    this.email = userInfo.getEmailAddress();
    this.firstname = userInfo.getFirstName();
    this.lastname = userInfo.getLastName();
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }
}
