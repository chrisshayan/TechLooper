package com.techlooper.model;

/**
 * Created by phuonghqh on 10/15/14.
 */
public class JobResponse {

  private String title;

  private String detailUrl;

  public static class Builder {

    private JobResponse instance = new JobResponse();

    public Builder withTitle(String title) {
      instance.title = title;
      return this;
    }

    public Builder withDetailUrl(String detailUrl) {
      instance.detailUrl = detailUrl;
      return this;
    }

    public JobResponse build() {
      return instance;
    }
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    JobResponse that = (JobResponse) o;

    if (detailUrl != null ? !detailUrl.equals(that.detailUrl) : that.detailUrl != null) return false;

    return true;
  }

  public int hashCode() {
    return detailUrl != null ? detailUrl.hashCode() : 0;
  }

  public String getDetailUrl() {
    return detailUrl;
  }

  public void setDetailUrl(String detailUrl) {
    this.detailUrl = detailUrl;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
