package com.olympics.olympicsandroid.utility;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by tkmagz4 on 7/10/16.
 */
public class DateUtils {

    public static final String DATE_TIME_WITH_TIMEZONE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZZZZZ";
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    // Aug-03-2016
    public static final long OLYMPIC_EVENT_START_DATE = 1470207600000L;

    // Aug-21-2016
    public static final long OLYMPIC_EVENT_END_DATE = 1471762800000L;
    public static final long NUM_OF_MILISECONDS_IN_DAY = 86400000;

    public static String getDateTimeInMillis(String dateStr) {

        if (TextUtils.isEmpty(dateStr)) {
            return null;
        } else {
            Date date = null;
            try {
                date = new SimpleDateFormat(DATE_TIME_WITH_TIMEZONE_FORMAT).parse(dateStr);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.set(Calendar.HOUR, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                return String.valueOf(calendar.getTime().getTime());

            } catch (ParseException e) {
                return "0";
            }
        }

    }

}
