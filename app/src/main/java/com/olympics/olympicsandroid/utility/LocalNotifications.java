package com.olympics.olympicsandroid.utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.olympics.olympicsandroid.OlympicsApplication;
import com.olympics.olympicsandroid.model.presentationModel.EventReminder;
import com.olympics.olympicsandroid.networkLayer.cache.database.DBReminderHelper;

import java.util.Calendar;

/**
 * Created by Jui Joshi on 8/8/16.
 */
public class LocalNotifications {

    private static final String ERROR_MSG = "\"Sorry! We are not able to set reminder for this " +
            "event at this time. Please try again later";
    private static final String REMINDER_SET_MSG = "Reminder for selected event has been set!";
    private static final long MILISECONDS_IN_FIVE_MINUTES = 300000;

    public void createLocalNotification(Context context, EventReminder eventReminder) {
        try {

            Bundle params = new Bundle();
            params.putString("user_reminder", "true");
            FirebaseAnalytics.getInstance(OlympicsApplication.getAppContext()).logEvent("reminder", params);


            if (eventReminder != null) {
                AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                Intent intent = new Intent(context, ReminderService.class);
                if (!TextUtils.isEmpty(eventReminder.getEventId()) &&
                        !TextUtils.isEmpty(eventReminder.getUnitId())) {
                    intent.putExtra(UtilityMethods.EXTRA_EVENT_ID, eventReminder.getEventId());
                    intent.putExtra(UtilityMethods.EXTRA_UNIT_NAME, eventReminder.getUnitName());
                    intent.putExtra(UtilityMethods.EXTRA_DESCIPLINE_NAME, eventReminder
                            .getDisciplineName());
                    intent.putExtra(UtilityMethods.EXTRA_UNIT_ID, eventReminder.getUnitId());
                    intent.putExtra(UtilityMethods.EXTRA_UNIT_START_DATE, eventReminder
                            .getUnitStartDate());

                    PendingIntent alarmIntent = PendingIntent.getService(context, eventReminder
                            .getUnitId().hashCode(), intent, 0);

                alarmMgr.set(AlarmManager.RTC_WAKEUP,getReminderInterval(DateUtils
                                .getEventStartDate(eventReminder.getUnitStartDate())),
                        alarmIntent);
                new DBReminderHelper().insertReminder(new EventReminder(eventReminder
                        .getEventId(), eventReminder.getUnitName(), eventReminder
                        .getUnitStartDate(), eventReminder.getUnitId(), eventReminder
                        .getDisciplineName()));
                    Toast.makeText(context, REMINDER_SET_MSG, Toast.LENGTH_LONG).show();
            } else {
                    Toast.makeText(context, ERROR_MSG, Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception ex) {
            Toast.makeText(context, ERROR_MSG, Toast.LENGTH_LONG).show();
        }
    }

    private long getReminderInterval(long eventStartDate) {
        return (System.currentTimeMillis() + (eventStartDate - Calendar.getInstance()
                .getTime()
                .getTime() - MILISECONDS_IN_FIVE_MINUTES));
    }

    public void cancelLocalNotification(Context context, String unitId) {

        Intent intent = new Intent(context, ReminderService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, unitId.hashCode() , intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();

        //Delete database entry
        new DBReminderHelper().deleteReminder(unitId);

    }
}


