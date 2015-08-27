package com.techlooper.model;

import com.techlooper.entity.vnw.RoleName;

import java.io.Serializable;

/**
 * Created by NguyenDangKhoa on 7/30/15.
 */
public class UserProfileDto implements Serializable {

  private String username;

  private String name;

  private String profileImageUrl;

  private String email;

  private String firstName;

  private String lastName;

  private RoleName roleName;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public RoleName getRoleName() {
    return roleName;
  }

  public void setRoleName(RoleName roleName) {
    this.roleName = roleName;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getProfileImageUrl() {
    return profileImageUrl;
  }

  public void setProfileImageUrl(String profileImageUrl) {
    this.profileImageUrl = profileImageUrl;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    UserProfileDto that = (UserProfileDto) o;

    if (username != null ? !username.equals(that.username) : that.username != null) return false;
    return !(email != null ? !email.equals(that.email) : that.email != null);

  }

  public int hashCode() {
    int result = username != null ? username.hashCode() : 0;
    result = 31 * result + (email != null ? email.hashCode() : 0);
    return result;
  }

  public static class UserProfileDtoBuilder {
    private UserProfileDto userProfileDto;

    private UserProfileDtoBuilder() {
      userProfileDto = new UserProfileDto();
    }

    public UserProfileDtoBuilder withUsername(String username) {
      userProfileDto.username = username;
      return this;
    }

    public UserProfileDtoBuilder withName(String name) {
      userProfileDto.name = name;
      return this;
    }

    public UserProfileDtoBuilder withProfileImageUrl(String profileImageUrl) {
      userProfileDto.profileImageUrl = profileImageUrl;
      return this;
    }

    public UserProfileDtoBuilder withEmail(String email) {
      userProfileDto.email = email;
      return this;
    }

    public UserProfileDtoBuilder withFirstName(String firstName) {
      userProfileDto.firstName = firstName;
      return this;
    }

    public UserProfileDtoBuilder withLastName(String lastName) {
      userProfileDto.lastName = lastName;
      return this;
    }

    public UserProfileDtoBuilder withRoleName(RoleName roleName) {
      userProfileDto.roleName = roleName;
      return this;
    }

    public static UserProfileDtoBuilder userProfileDto() {
      return new UserProfileDtoBuilder();
    }

    public UserProfileDto build() {
      return userProfileDto;
    }
  }
}
