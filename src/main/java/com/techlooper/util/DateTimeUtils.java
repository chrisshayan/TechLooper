package com.techlooper.util;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public static Date string2Date(String datetime, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        try {
            return formatter.parse(datetime);
        } catch (ParseException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    public static String date2String(Date datetime, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(datetime);
    }

    public static String currentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat(BASIC_DATE_PATTERN);
        return formatter.format(new Date());
    }

    public static String yesterdayDate() {
        SimpleDateFormat formatter = new SimpleDateFormat(BASIC_DATE_PATTERN);
        DateTime yesterday = DateTime.now().minusDays(1);
        return formatter.format(yesterday.toDate());
    }

    public static String yesterdayDate(String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        DateTime yesterday = DateTime.now().minusDays(1);
        return formatter.format(yesterday.toDate());
    }

    public static String currentDate(String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(new Date());
    }

    public static Long currentDateTime() {
        return new Date().getTime();
    }

    public static int daysBetween(Date firstDate, Date secondDate) {
        return Days.daysBetween(new DateTime(firstDate), new DateTime(secondDate)).getDays();
    }

    public static int daysBetween(String firstDateStr, String secondDateStr) {
        DateTime firstDateTime = new DateTime(string2Date(firstDateStr, BASIC_DATE_PATTERN));
        DateTime secondDateTime = new DateTime(string2Date(secondDateStr, BASIC_DATE_PATTERN));
        return Days.daysBetween(firstDateTime, secondDateTime).getDays();
    }

}
