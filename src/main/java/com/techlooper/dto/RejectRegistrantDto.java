package com.techlooper.dto;

import java.io.Serializable;

/**
 * Created by phuonghqh on 11/16/15.
 */
public class RejectRegistrantDto implements Serializable {

  private Long registrantId;

  private String reason;

  public Long getRegistrantId() {
    return registrantId;
  }

  public void setRegistrantId(Long registrantId) {
    this.registrantId = registrantId;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }
}
