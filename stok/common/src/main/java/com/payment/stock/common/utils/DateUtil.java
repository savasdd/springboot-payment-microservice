package com.payment.stock.common.utils;

import lombok.experimental.UtilityClass;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@UtilityClass
public class DateUtil {

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
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Istanbul"));
        cal.setTime(date);
        return cal;
    }
}
