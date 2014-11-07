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

    public SkillStatisticItem(String skill, Long currentCount, Long previousCount, List<Long> histogramData) {
        this.skill = skill;
        this.currentCount = currentCount;
        this.previousCount = previousCount;
        this.histogramData = histogramData;
    }

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

    public List<Long>  getHistogramData() {
        return histogramData;
    }

    public void setHistogramData(List<Long> histogramData) {
        this.histogramData = histogramData;
    }

}
