package com.techlooper.entity;

/**
 * Created by phuonghqh on 12/25/14.
 */
public abstract class UserProfile {

  private AccessGrant accessGrant;

  public AccessGrant getAccessGrant() {
    return accessGrant;
  }

  public void setAccessGrant(AccessGrant accessGrant) {
    this.accessGrant = accessGrant;
  }
}
