package com.techlooper.model;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 11/4/14.
 */
public class SkillStatisticItem {

  private String skill;
  private Long currentCount;
  private Long previousCount;
  private List<Long> histogramData;

  public String getSkill() {
    return skill;
  }

  public void setSkill(String skill) {
    this.skill = skill;
  }

  public Long getCurrentCount() {
    return currentCount;
  }

  public void setCurrentCount(Long currentCount) {
    this.currentCount = currentCount;
  }

  public Long getPreviousCount() {
    return previousCount;
  }

  public void setPreviousCount(Long previousCount) {
    this.previousCount = previousCount;
  }

  public List<Long> getHistogramData() {
    return histogramData;
  }

  public void setHistogramData(List<Long> histogramData) {
    this.histogramData = histogramData;
  }

  public static class Builder {
    private SkillStatisticItem instance = new SkillStatisticItem();

    public Builder withPreviousCount(Long previousCount) {
      instance.previousCount = previousCount;
      return this;
    }

    public Builder withCurrentCount(Long currentCount) {
      instance.currentCount = currentCount;
      return this;
    }

    public Builder withHistogramData(List<Long> histogramData) {
      instance.histogramData = histogramData;
      return this;
    }

    public Builder withSkill(String skill) {
      instance.skill = skill;
      return this;
    }

    public SkillStatisticItem build() {
      return instance;
    }
  }

}
