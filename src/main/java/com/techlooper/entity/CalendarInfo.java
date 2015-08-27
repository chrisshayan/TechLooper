package com.techlooper.entity;

import java.io.Serializable;

/**
 * Created by phuonghqh on 8/27/15.
 */
public class CalendarInfo implements Serializable {

  private String id;

  private String htmlLink;

  private String hangoutLink;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getHtmlLink() {
    return htmlLink;
  }

  public void setHtmlLink(String htmlLink) {
    this.htmlLink = htmlLink;
  }

  public String getHangoutLink() {
    return hangoutLink;
  }

  public void setHangoutLink(String hangoutLink) {
    this.hangoutLink = hangoutLink;
  }
}
