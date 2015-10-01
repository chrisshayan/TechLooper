package com.techlooper.model;

import java.io.Serializable;

/**
 * Created by phuonghqh on 10/1/15.
 */
public class EmailContent implements Serializable {

  private String subject;

  private String content;

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
