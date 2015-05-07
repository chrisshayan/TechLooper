package com.techlooper.model;

import java.util.List;

/**
 * Created by phuonghqh on 5/5/15.
 */
public class JobOfferEvaluation {

  private Integer totalPay;

  private Double percentRank;

  private List<JobOfferRange> jobOfferRanges;

  public Integer getTotalPay() {
    return totalPay;
  }

  public void setTotalPay(Integer totalPay) {
    this.totalPay = totalPay;
  }

  public Double getPercentRank() {
    return percentRank;
  }

  public void setPercentRank(Double percentRank) {
    this.percentRank = percentRank;
  }

  public List<JobOfferRange> getJobOfferRanges() {
    return jobOfferRanges;
  }

  public void setJobOfferRanges(List<JobOfferRange> jobOfferRanges) {
    this.jobOfferRanges = jobOfferRanges;
  }
}
