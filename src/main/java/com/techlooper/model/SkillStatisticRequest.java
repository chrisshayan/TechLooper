package com.techlooper.model;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 11/4/14.
 */
public class SkillStatisticRequest {

    private TechnicalTermEnum term;

    private HistogramEnum[] histograms;

    public HistogramEnum[] getHistograms() {
        return histograms;
    }

    public void setHistograms(HistogramEnum[] histograms) {
        this.histograms = histograms;
    }

    public TechnicalTermEnum getTerm() {
        return term;
    }

    public void setTerm(TechnicalTermEnum term) {
        this.term = term;
    }
}