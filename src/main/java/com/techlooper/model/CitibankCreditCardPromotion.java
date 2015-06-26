package com.techlooper.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * Created by phuonghqh on 5/27/15.
 */
public class CitibankCreditCardPromotion {

  @NotNull
  private Long salaryReviewId;

  @NotNull
  private String fullName;

  @NotNull
  @Length(min = 10, max = 20)
  private String mobileNumber;

  @Email
  private String email;

  @NotNull
  private PaymentMethod paymentMethod;

  private String location;

  private String netIncome;

  private Boolean agree;

  public String getNetIncome() {
    return netIncome;
  }

  public void setNetIncome(String netIncome) {
    this.netIncome = netIncome;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Long getSalaryReviewId() {
    return salaryReviewId;
  }

  public void setSalaryReviewId(Long salaryReviewId) {
    this.salaryReviewId = salaryReviewId;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public PaymentMethod getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(PaymentMethod paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public Boolean getAgree() {
    return agree;
  }

  public void setAgree(Boolean agree) {
    this.agree = agree;
  }
}
