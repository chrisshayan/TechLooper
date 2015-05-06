package com.techlooper.model;

/**
 * Created by phuonghqh on 5/5/15.
 */
public class SalaryRange {

    private Double percent;

    private Double percentile;

    public SalaryRange() {
    }

    public SalaryRange(Double percent, Double percentile) {
        this.percent = percent;
        this.percentile = percentile;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public Double getPercentile() {
        return percentile;
    }

    public void setPercentile(Double percentile) {
        this.percentile = percentile;
    }
}
