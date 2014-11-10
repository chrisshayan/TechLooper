package com.techlooper.service.impl;

import java.util.Calendar;

/**
 * Created by NguyenDangKhoa on 11/10/14.
 */
public class MonthlyJobQueryBuilder extends AbstractJobQueryBuilder {

    public int getLastNumberOfDays() {
        return Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
    }

}
