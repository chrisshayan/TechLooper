package com.techlooper.service.impl;

/**
 * Created by NguyenDangKhoa on 11/10/14.
 */
public class ConfigurableJobQueryBuilder extends AbstractJobQueryBuilder {

    private int lastNumberOfDays;

    public ConfigurableJobQueryBuilder(int lastNumberOfDays) {
        this.lastNumberOfDays = lastNumberOfDays;
    }

    public int getLastNumberOfDays() {
        return lastNumberOfDays;
    }

}
