package com.techlooper.model;


public class TechnicalTermResponse {

  private String name;

  private TechnicalTermEnum term;

  private Long count;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public TechnicalTermEnum getTerm() {
    return term;
  }

  public void setTerm(TechnicalTermEnum term) {
    this.term = term;
  }

  public Long getCount() {
    return count;
  }

  public void setCount(Long count) {
    this.count = count;
  }

  public static class Builder {

    private TechnicalTermResponse instance = new TechnicalTermResponse();

    public Builder withCount(Long count) {
      instance.count = count;
      return this;
    }

    public Builder withTerm(TechnicalTermEnum term) {
      instance.term = term;
      instance.name = term.toString();
      return this;
    }

    public TechnicalTermResponse build() {
      return instance;
    }
  }
}
