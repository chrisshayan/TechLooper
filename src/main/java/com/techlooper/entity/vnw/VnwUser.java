package com.techlooper.entity.vnw;

import javax.persistence.*;

/**
 * Created by phuonghqh on 6/25/15.
 */
@Entity
@Table(name = "tblregistrationinfo")
public class VnwUser {

  @Id
  @Column(name = "userid")
  private Long userId;

  private String username;

  @Column(name = "userpass")
  private String userPass;

  @Column(name = "email1")
  private String email;

  @Enumerated(EnumType.ORDINAL)
  @Column(name = "youareid")
  private RoleName roleName;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public RoleName getRoleName() {
    return roleName;
  }

  public void setRoleName(RoleName roleName) {
    this.roleName = roleName;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getUserPass() {
    return userPass;
  }

  public void setUserPass(String userPass) {
    this.userPass = userPass;
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    VnwUser vnwUser = (VnwUser) o;
    return !(userId != null ? !userId.equals(vnwUser.userId) : vnwUser.userId != null);

  }

  public int hashCode() {
    return userId != null ? userId.hashCode() : 0;
  }

  public static class VnwUserBuilder {
    private VnwUser vnwUser;

    private VnwUserBuilder() {
      vnwUser = new VnwUser();
    }

    public VnwUserBuilder withUserId(Long userId) {
      vnwUser.userId = userId;
      return this;
    }

    public VnwUserBuilder withUsername(String username) {
      vnwUser.username = username;
      return this;
    }

    public VnwUserBuilder withUserPass(String userPass) {
      vnwUser.userPass = userPass;
      return this;
    }

    public VnwUserBuilder withRoleName(RoleName roleName) {
      vnwUser.roleName = roleName;
      return this;
    }

    public static VnwUserBuilder vnwUser() {
      return new VnwUserBuilder();
    }

    public VnwUser build() {
      return vnwUser;
    }
  }
}
