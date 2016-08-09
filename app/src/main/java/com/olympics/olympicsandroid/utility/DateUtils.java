package com.olympics.olympicsandroid.utility;

import android.text.TextUtils;

import com.olympics.olympicsandroid.OlympicsApplication;
import com.olympics.olympicsandroid.networkLayer.cache.database.OlympicsPrefs;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by tkmagz4 on 7/10/16.
 */
public class DateUtils {

    public static final String DATE_TIME_WITH_TIMEZONE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZZZZZ";
    public static final String DATE_TIME_WITHOUT_TIMEZONE_FORMAT_24HOUR = "MMM-dd'    Time - 'HH:mm";
    public static final String DATE_TIME_WITHOUT_TIMEZONE_FORMAT_12HOUR = "MMM-dd'    Time - 'hh:mm aa";

    public static final String DATE_TIME_WITHOUT_DATE_TIMEZONE_FORMAT_24HOUR = "HH:mm";
    public static final String DATE_TIME_WITHOUT_DATE_TIMEZONE_FORMAT_12HOUR = "hh:mm aa";

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    // Aug-03-2016
    public static final String OLYMPIC_EVENT_START_DATE = "2016-08-03T13:00:00-03:00";

    // Aug-21-2016

    public static final String OLYMPIC_EVENT_END_DATE = "2016-08-21T23:59:59+00:00";

    public static final long NUM_OF_MILISECONDS_IN_DAY = 86400000;

    public static long getOlympicEventStartDate()
    {
        Date date = null;
        try {
            date = new SimpleDateFormat(DATE_TIME_WITH_TIMEZONE_FORMAT).parse(OLYMPIC_EVENT_START_DATE);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DATE);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(year, month, day, 0, 0, 0);
            calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
            return calendar.getTime().getTime();

        } catch (ParseException e) {
            return 0L;
        }
    }


    public static long getOlympicEventEndDate()
    {
        Date date = null;
        try {
            date = new SimpleDateFormat(DATE_TIME_WITH_TIMEZONE_FORMAT).parse(OLYMPIC_EVENT_END_DATE);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DATE);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(year, month, day, 0, 0, 0);
            calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
            return calendar.getTime().getTime();

        } catch (ParseException e) {
            return 0L;
        }
    }


    public static String getDateTimeInMillis(String dateStr) {

        if (TextUtils.isEmpty(dateStr)) {
            return null;
        } else {
            Date date = null;
            try {
                date = new SimpleDateFormat(DATE_TIME_WITH_TIMEZONE_FORMAT).parse(dateStr);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DATE);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(year, month, day, 0, 0, 0);
                calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
                return String.valueOf(calendar.getTime().getTime());

            } catch (ParseException e) {
                return "0";
            }
        }
    }

    public static boolean isCurrentDateInOlympics()
    {
        if(System.currentTimeMillis() < OlympicsApplication.getAppInstance().getCacheStartDate())
        {
            return false;
        }
        return true;
    }

    public static String setUpUnitDate(String timeInMiliSeconds) {

        try {
            DateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            Calendar localCalendar = Calendar.getInstance();
            localCalendar.setTimeInMillis(Long.parseLong(timeInMiliSeconds));
            return formatter.format(localCalendar.getTime());
        } catch (Exception ex) {
            return "";
        }

    }

    public static String getUnitDateWithTime(String timeInMiliSeconds) {

        try {

            SimpleDateFormat f = new SimpleDateFormat(DATE_TIME_WITH_TIMEZONE_FORMAT);
            Date d = f.parse(timeInMiliSeconds);
            long milliseconds = d.getTime();


            DateFormat formatter = new SimpleDateFormat(DATE_TIME_WITHOUT_TIMEZONE_FORMAT_24HOUR, Locale.getDefault());

            if(OlympicsPrefs.getInstance(null).getIsUnit24Hour())
            {
                formatter = new SimpleDateFormat(DATE_TIME_WITHOUT_TIMEZONE_FORMAT_24HOUR, Locale.getDefault());
            }
            else{
                formatter = new SimpleDateFormat(DATE_TIME_WITHOUT_TIMEZONE_FORMAT_12HOUR, Locale.getDefault());
            }
            Calendar localCalendar = Calendar.getInstance();
            localCalendar.setTimeInMillis(milliseconds);
            return formatter.format(localCalendar.getTime());
        } catch (Exception ex) {
            return "";
        }

    }


    public static String getUnitTime(String timeInMiliSeconds) {

        try {

            SimpleDateFormat f = new SimpleDateFormat(DATE_TIME_WITH_TIMEZONE_FORMAT);
            Date d = f.parse(timeInMiliSeconds);
            long milliseconds = d.getTime();


            DateFormat formatter = new SimpleDateFormat(DATE_TIME_WITHOUT_DATE_TIMEZONE_FORMAT_24HOUR, Locale.getDefault());
            if(OlympicsPrefs.getInstance(null).getIsUnit24Hour())
            {
                formatter = new SimpleDateFormat(DATE_TIME_WITHOUT_DATE_TIMEZONE_FORMAT_24HOUR, Locale.getDefault());
            }
            else{
                formatter = new SimpleDateFormat(DATE_TIME_WITHOUT_DATE_TIMEZONE_FORMAT_12HOUR, Locale.getDefault());
            }

            Calendar localCalendar = Calendar.getInstance();
            localCalendar.setTimeInMillis(milliseconds);
            return formatter.format(localCalendar.getTime());
        } catch (Exception ex) {
            return "";
        }

    }

    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm a");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    public static long getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(year, month, day, 0, 0, 0);
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        return calendar.getTime().getTime();
    }

    public static Long getEventStartDate(String dateStr) {

        try {
            if (TextUtils.isEmpty(dateStr)) {
                return 0L;
            } else {
                Date date = null;
                try {
                    date = new SimpleDateFormat(DATE_TIME_WITH_TIMEZONE_FORMAT).parse(dateStr);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    return calendar.getTime().getTime();
                } catch (ParseException e) {
                    return 0L;
                }
            }
        } catch (Exception ex) {
            return 0L;
        }
    }

}




