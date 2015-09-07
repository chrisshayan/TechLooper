package com.techlooper.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by NguyenDangKhoa on 8/13/15.
 */
public class DateTimeUtils {

    public static final String BASIC_DATE_PATTERN = "dd/MM/yyyy";

    public static Date string2Date(String datetime, String pattern) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.parse(datetime);
    }

    public static String date2String(Date datetime, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(datetime);
    }

    public static String currentDate(String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(new Date());
    }

}
