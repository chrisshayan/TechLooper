package com.techlooper.model;


import org.elasticsearch.Build;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NguyenDangKhoa on 11/4/14.
 */
public class SkillStatistic {

  private String skillName;

  private List<Histogram> histograms = new ArrayList<>();

  public static class Builder {

    private SkillStatistic instance = new SkillStatistic();

    public Builder withHistogram(Histogram histogram) {
      instance.histograms.add(histogram);
      return this;
    }

    public Builder withSkillName(String skillName) {
      instance.skillName = skillName;
      return this;
    }

    public SkillStatistic build() {
      return instance;
    }
  }

  public List<Histogram> getHistograms() {
    return histograms;
  }

  public void setHistograms(List<Histogram> histograms) {
    this.histograms = histograms;
  }

  public String getSkillName() {
    return skillName;
  }

  public void setSkillName(String skillName) {
    this.skillName = skillName;
  }
}
