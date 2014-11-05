package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 11/4/14.
 */
public class SkillStatisticRequest {

    private TechnicalTermEnum term;

    private String period;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public TechnicalTermEnum getTerm() {
        return term;
    }

    public void setTerm(TechnicalTermEnum term) {
        this.term = term;
    }
}