package com.techlooper.model;

import java.util.Date;

/**
 * Created by NguyenDangKhoa on 4/24/15.
 */
public class Employer {

    private Long userId;

    private int isActive;

    private Date createdDate;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
