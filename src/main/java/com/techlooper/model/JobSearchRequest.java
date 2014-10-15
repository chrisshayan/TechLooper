package com.techlooper.model;

/**
 * Created by phuonghqh on 10/14/14.
 */
public class JobSearchRequest {

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
