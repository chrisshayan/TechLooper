package com.techlooper.entity.vnw;

/**
 * Created by phuonghqh on 6/25/15.
 */
public enum RoleName {
  PRE_EMPLOYER(0), EMPLOYER(1), JOB_SEEKER(2);

  private Integer value;

  RoleName(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }

}
