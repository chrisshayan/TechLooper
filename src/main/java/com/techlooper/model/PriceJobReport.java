package com.techlooper.model;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 5/21/15.
 */
public class PriceJobReport {

    private Double targetPay;

    private Double averageSalary;

    private List<SalaryRange> priceJobSalaries;

    public Double getTargetPay() {
        return targetPay;
    }

    public void setTargetPay(Double targetPay) {
        this.targetPay = targetPay;
    }

    public Double getAverageSalary() {
        return averageSalary;
    }

    public void setAverageSalary(Double averageSalary) {
        this.averageSalary = averageSalary;
    }

    public List<SalaryRange> getPriceJobSalaries() {
        return priceJobSalaries;
    }

    public void setPriceJobSalaries(List<SalaryRange> priceJobSalaries) {
        this.priceJobSalaries = priceJobSalaries;
    }
}
