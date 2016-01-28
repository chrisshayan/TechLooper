package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 8/17/15.
 */
public class JobSearchCriteria {

    private String keyword;

    private String location;

    private Integer locationId;

    private Integer page;

    private Boolean topPriority;

    private String crawlSource;

    private boolean fromJobAlert;

    public JobSearchCriteria() {
        this.page = 1;
    }

    public JobSearchCriteria(Integer page) {
        this.page = page;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Boolean getTopPriority() {
        return topPriority;
    }

    public void setTopPriority(Boolean topPriority) {
        this.topPriority = topPriority;
    }

    public String getCrawlSource() {
        return crawlSource;
    }

    public void setCrawlSource(String crawlSource) {
        this.crawlSource = crawlSource;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public boolean isFromJobAlert() {
        return fromJobAlert;
    }

    public void setFromJobAlert(boolean fromJobAlert) {
        this.fromJobAlert = fromJobAlert;
    }
}
