package com.techlooper.util;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by NguyenDangKhoa on 8/13/15.
 */
public class DateTimeUtils {

    public static final Logger LOGGER = LoggerFactory.getLogger(DateTimeUtils.class);

    public static final String BASIC_DATE_PATTERN = "dd/MM/yyyy";

    public static final String BASIC_DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm";

//  public static final String ISO_DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZZ";

    public static DateTime parseBasicDate(String text) {
        if (StringUtils.hasText(text)) {
            try {
                return DateTimeFormat.forPattern(BASIC_DATE_PATTERN).parseDateTime(text);
            } catch (Exception e) {
                LOGGER.debug("Couldn't parse date-time [{}] by format [{}]", text, BASIC_DATE_PATTERN);
            }
        }
        return null;
    }

    public static Date string2Date(String datetime, String pattern) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.parse(datetime);
    }

//    public static String date2String(Date datetime, String pattern) {
//        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
//        return formatter.format(datetime);
//    }

    public static String currentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat(BASIC_DATE_PATTERN);
        return formatter.format(new Date());
    }

    public static String yesterdayDate() {
        SimpleDateFormat formatter = new SimpleDateFormat(BASIC_DATE_PATTERN);
        DateTime yesterday = DateTime.now().minusDays(1);
        return formatter.format(yesterday.toDate());
    }

    public static String currentDate(String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(new Date());
    }

    public static int daysBetween(Date firstDate, Date secondDate) {
        return Days.daysBetween(new DateTime(firstDate), new DateTime(secondDate)).getDays();
    }

    public static int daysBetween(String firstDateStr, String secondDateStr) throws ParseException {
        DateTime firstDateTime = new DateTime(string2Date(firstDateStr, BASIC_DATE_PATTERN));
        DateTime secondDateTime = new DateTime(string2Date(secondDateStr, BASIC_DATE_PATTERN));
        return Days.daysBetween(firstDateTime, secondDateTime).getDays();
    }

    public static void main(String args[]) {
        System.out.println(yesterdayDate());
    }

}
