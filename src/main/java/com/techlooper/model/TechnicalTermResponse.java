package com.techlooper.model;


public class TechnicalTermResponse {

    private String term;

    private Long count;

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
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

        public Builder withTerm(String term) {
            instance.term = term;
            return this;
        }

        public TechnicalTermResponse build() {
            return instance;
        }
    }
}
