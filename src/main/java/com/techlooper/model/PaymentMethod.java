package com.techlooper.model;

/**
 * Created by phuonghqh on 5/27/15.
 */
public enum PaymentMethod {
  CASH("cash"), BANK_TRANSFER("bank transfer");

  private String value;

  private PaymentMethod(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
