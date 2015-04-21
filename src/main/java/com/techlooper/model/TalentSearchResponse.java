package com.techlooper.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NguyenDangKhoa on 3/13/15.
 */
public class TalentSearchResponse {

    private long total = 0;

    private List<Talent> result = new ArrayList<>();

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<Talent> getResult() {
        return result;
    }

    public void setResult(List<Talent> result) {
        this.result = result;
    }

    public static class Builder {

        private TalentSearchResponse instance = new TalentSearchResponse();

        public Builder withTotal(long total) {
            instance.total += total;
            return this;
        }

        public Builder withResult(List<Talent> result) {
            instance.result.addAll(result);
            return this;
        }

        public TalentSearchResponse build() {
            return instance;
        }
    }
}
