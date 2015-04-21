package com.techlooper.model;


public class TechnicalTermResponse {

    private String term;

    private String label;

    private Long count;

    private Double averageSalaryMin;

    private Double averageSalaryMax;

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Double getAverageSalaryMin() {
        return averageSalaryMin;
    }

    public void setAverageSalaryMin(Double averageSalaryMin) {
        this.averageSalaryMin = averageSalaryMin;
    }

    public Double getAverageSalaryMax() {
        return averageSalaryMax;
    }

    public void setAverageSalaryMax(Double averageSalaryMax) {
        this.averageSalaryMax = averageSalaryMax;
    }

    public static class Builder {

        private TechnicalTermResponse instance = new TechnicalTermResponse();

        public Builder withCount(Long count) {
            instance.count = count;
            return this;
        }

        public Builder withTerm(String term) {
            instance.term = term;
            return this;
        }

        public Builder withLabel(String label) {
            instance.label = label;
            return this;
        }

        public Builder withAverageSalaryMin(Double averageSalaryMin) {
            instance.averageSalaryMin = averageSalaryMin;
            return this;
        }

        public Builder withAverageSalaryMax(Double averageSalaryMax) {
            instance.averageSalaryMax = averageSalaryMax;
            return this;
        }

        public TechnicalTermResponse build() {
            return instance;
        }
    }
}
