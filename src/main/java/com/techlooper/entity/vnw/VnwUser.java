package com.techlooper.entity.vnw;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
}
