package com.techlooper.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by NguyenDangKhoa on 8/13/15.
 */
public class DateTimeUtils {

    public static Date parseString2Date(String datetime, String pattern) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.parse(datetime);
    }

    public static String parseDate2String(Date datetime, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(datetime);
    }

}
