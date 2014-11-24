package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 11/4/14.
 */
public class SkillStatisticRequest {

    private TechnicalTerm term;

    private HistogramEnum[] histograms;

    public HistogramEnum[] getHistograms() {
        return histograms;
    }

    public void setHistograms(HistogramEnum[] histograms) {
        this.histograms = histograms;
    }

    public TechnicalTerm getTerm() {
        return term;
    }

    public void setTerm(TechnicalTerm term) {
        this.term = term;
    }
}