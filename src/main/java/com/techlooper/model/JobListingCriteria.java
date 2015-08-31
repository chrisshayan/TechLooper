package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 8/17/15.
 */
public class JobListingCriteria {

    private String keyword;

    private String location;

    private Integer page;

    public JobListingCriteria(Integer page) {
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
}
