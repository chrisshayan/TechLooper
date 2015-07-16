package com.techlooper.entity.vnw.dto;

import com.techlooper.entity.vnw.RoleName;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

/**
 * Created by phuonghqh on 6/26/15.
 */
public class VnwUserDto {

  private Long userId;

  private String username;

  private RoleName roleName;

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

  public RoleName getRoleName() {
    return roleName;
  }

  public void setRoleName(RoleName roleName) {
    this.roleName = roleName;
  }
}
