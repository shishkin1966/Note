package com.cleanarchitecture.common.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Shishkin on 11.01.2018.
 */

public class DateUtils {

    private DateUtils() {
    }

    public static long beginingDay() {
        return beginingDay(System.currentTimeMillis());
    }

    public static long endDay() {
        return endDay(System.currentTimeMillis());
    }

    public static long beginingDay(long date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(date));
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar.getTimeInMillis();
    }

    public static long endDay(long date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(date));
        calendar.set(Calendar.MILLISECOND, 999);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        return calendar.getTimeInMillis();
    }

}
