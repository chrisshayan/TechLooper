package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 9/22/15.
 */
public enum RewardEnum {

  FIRST_PLACE("1st place", 1),
  SECOND_PLACE("2nd place", 2),
  THIRD_PLACE("3rd place", 3);

  private String value;

  private int order;

  RewardEnum(String value, int order) {
    this.value = value;
    this.order = order;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public int getOrder() {
    return order;
  }

  public void setOrder(int order) {
    this.order = order;
  }
}
