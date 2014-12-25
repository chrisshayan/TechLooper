package com.techlooper.model;

import java.io.Serializable;

/**
 * Created by phuonghqh on 10/14/14.
 */
public class JobSearchRequest implements Serializable {

  private String terms;

  private String pageNumber;

  public String getTerms() {
    return terms;
  }

  public void setTerms(String terms) {
    this.terms = terms;
  }

  public String getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(String pageNumber) {
    this.pageNumber = pageNumber;
  }
}
