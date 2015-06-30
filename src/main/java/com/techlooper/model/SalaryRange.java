package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 5/7/15.
 */
public class SalaryRange {

    private Double percent;

    private Double percentile;

    public SalaryRange(){}

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SalaryRange that = (SalaryRange) o;

        if (!percentile.equals(that.percentile)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return percentile.hashCode();
    }
}
