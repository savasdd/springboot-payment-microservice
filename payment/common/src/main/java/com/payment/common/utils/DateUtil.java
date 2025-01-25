package com.payment.common.utils;

import lombok.experimental.UtilityClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@UtilityClass
public class DateUtil {
    private static final String defaultFormat = "yyyy-MM-dd HH:mm";


    public static Date format(Date date) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(defaultFormat);
            String format = df.format(date);
            return df.parse(format);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date addMinute(Date date, int time) {
        Date newDate = format(date);

        Calendar cal = getCalendar();
        cal.setTime(newDate);
        cal.add(Calendar.MINUTE, time);
        return cal.getTime();
    }

    public static Date addHour(Date date, int time) {
        Calendar cal = getCalendar();
        cal.setTime(date);
        cal.add(Calendar.HOUR, time);
        return cal.getTime();
    }

    public static Date addDay(Date date, int time) {
        Calendar cal = getCalendar();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, time);
        return cal.getTime();
    }

    public static int getYear(Date date) {
        return getCalendar(date).get(Calendar.YEAR);
    }

    public static int getMonth(Date date) {
        return getCalendar(date).get(Calendar.MONTH);
    }

    public static int getDay(Date date) {
        return getCalendar(date).get(Calendar.DAY_OF_MONTH);
    }

    private static Calendar getCalendar(Date date) {
        Calendar cal = getCalendar();
        cal.setTime(date);
        return cal;
    }

    private static Calendar getCalendar() {
        return Calendar.getInstance(TimeZone.getTimeZone("Europe/Istanbul"));
    }

}
