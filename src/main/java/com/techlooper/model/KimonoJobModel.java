package com.techlooper.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by NguyenDangKhoa on 8/10/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class KimonoJobModel {

    @JsonProperty("name")
    private String crawlSource;

    private KimonoJobList results;

    public String getCrawlSource() {
        return crawlSource;
    }

    public void setCrawlSource(String crawlSource) {
        this.crawlSource = crawlSource;
    }

    public KimonoJobList getResults() {
        return results;
    }

    public void setResults(KimonoJobList results) {
        this.results = results;
    }
}
