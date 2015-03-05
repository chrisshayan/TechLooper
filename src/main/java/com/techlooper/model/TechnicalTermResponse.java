package com.techlooper.model;


public class TechnicalTermResponse {

    private String term;

    private String label;

    private Long count;

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

        public TechnicalTermResponse build() {
            return instance;
        }
    }
}
