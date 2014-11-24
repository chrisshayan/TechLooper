package com.techlooper.model;


public class TechnicalTermResponse {

    private TechnicalTerm term;

    private Long count;

    public TechnicalTerm getTerm() {
        return term;
    }

    public void setTerm(TechnicalTerm term) {
        this.term = term;
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

        public Builder withTerm(TechnicalTerm term) {
            instance.term = term;
            return this;
        }

        public TechnicalTermResponse build() {
            return instance;
        }
    }
}
