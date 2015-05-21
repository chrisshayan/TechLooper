package com.techlooper.model;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 5/21/15.
 */
public class PriceJobReport {

    private double targetPay;

    private double averageSalary;

    private List<SalaryRange> priceJobSalaries;

    public double getTargetPay() {
        return targetPay;
    }

    public void setTargetPay(double targetPay) {
        this.targetPay = targetPay;
    }

    public double getAverageSalary() {
        return averageSalary;
    }

    public void setAverageSalary(double averageSalary) {
        this.averageSalary = averageSalary;
    }

    public List<SalaryRange> getPriceJobSalaries() {
        return priceJobSalaries;
    }

    public void setPriceJobSalaries(List<SalaryRange> priceJobSalaries) {
        this.priceJobSalaries = priceJobSalaries;
    }
}
