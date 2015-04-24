package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 4/24/15.
 */
public class Employer {

    private Long userId;

    private int isActive;

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
}
