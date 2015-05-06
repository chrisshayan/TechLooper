package com.techlooper.model;

import java.util.List;

/**
 * Created by phuonghqh on 5/5/15.
 */
public class SalaryReport {

    private Integer netSalary;

    private Double percentRank;

    private List<SalaryRange> salaryRanges;

    public Integer getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(Integer netSalary) {
        this.netSalary = netSalary;
    }

    public Double getPercentRank() {
        return percentRank;
    }

    public void setPercentRank(Double percentRank) {
        this.percentRank = percentRank;
    }

    public List<SalaryRange> getSalaryRanges() {
        return salaryRanges;
    }

    public void setSalaryRanges(List<SalaryRange> salaryRanges) {
        this.salaryRanges = salaryRanges;
    }
}
