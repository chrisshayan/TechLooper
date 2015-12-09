package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 9/22/15.
 */
public enum SalaryPercentileEnum {

    VERY_LOW(0, 10),
    LOW(10, 25),
    MEDIUM(25, 50),
    SATISFACTORY(50, 75),
    HIGH(75, 90),
    VERY_HIGH(90, 100);

    private double fromPercent;

    private double toPercent;

    SalaryPercentileEnum(double fromPercent, double toPercent) {
        this.fromPercent = fromPercent;
        this.toPercent = toPercent;
    }

    public double getFromPercent() {
        return fromPercent;
    }

    public void setFromPercent(double fromPercent) {
        this.fromPercent = fromPercent;
    }

    public double getToPercent() {
        return toPercent;
    }

    public void setToPercent(double toPercent) {
        this.toPercent = toPercent;
    }
}
