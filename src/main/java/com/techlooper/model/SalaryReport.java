package com.techlooper.model;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by phuonghqh on 5/5/15.
 */
public class SalaryReport {

    private Integer netSalary;

    private Double percentRank;

    private Integer numberOfJobs;

    private Integer numberOfSurveys;

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

    public Integer getNumberOfJobs() {
        return numberOfJobs;
    }

    public void setNumberOfJobs(Integer numberOfJobs) {
        this.numberOfJobs = numberOfJobs;
    }

    public Integer getNumberOfSurveys() {
        return numberOfSurveys;
    }

    public void setNumberOfSurveys(Integer numberOfSurveys) {
        this.numberOfSurveys = numberOfSurveys;
    }

    public List<SalaryRange> getSalaryRanges() {
        return salaryRanges;
    }

    public void setSalaryRanges(List<SalaryRange> salaryRanges) {
        this.salaryRanges = salaryRanges;
    }

    public String percentRankToString() {
        DecimalFormat df = new DecimalFormat("###.#");
        return df.format(percentRank);
    }
}
