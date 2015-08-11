package com.techlooper.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by NguyenDangKhoa on 8/10/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class KimonoDataModel {

    @JsonProperty("name")
    private String crawlSource;

    @JsonProperty("thisversionrun")
    private String crawlDateTime;

    private KimonoJobList results;

    public KimonoDataModel() {
    }

    public String getCrawlSource() {
        return crawlSource;
    }

    public void setCrawlSource(String crawlSource) {
        this.crawlSource = crawlSource;
    }

    public String getCrawlDateTime() {
        return crawlDateTime;
    }

    public void setCrawlDateTime(String crawlDateTime) {
        this.crawlDateTime = crawlDateTime;
    }

    public KimonoJobList getResults() {
        return results;
    }

    public void setResults(KimonoJobList results) {
        this.results = results;
    }
}
