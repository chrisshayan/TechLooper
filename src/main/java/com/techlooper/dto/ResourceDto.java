package com.techlooper.dto;

import java.io.Serializable;

/**
 * Created by phuonghqh on 10/9/15.
 */
public class ResourceDto implements Serializable {

  private String url;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
