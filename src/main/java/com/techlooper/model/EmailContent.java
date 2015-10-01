package com.techlooper.model;

import javax.mail.Address;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by phuonghqh on 10/1/15.
 */
public class EmailContent implements Serializable {

  private String subject;

  private String content;

  private Address[] bcc;

  public Address[] getBcc() {
    return bcc;
  }

  public void setBcc(Address[] bcc) {
    this.bcc = bcc;
  }

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
