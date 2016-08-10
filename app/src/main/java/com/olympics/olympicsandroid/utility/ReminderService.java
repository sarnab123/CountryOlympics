package com.olympics.olympicsandroid.utility;

/**
 * Created by tkmagz4 on 8/8/16.
 */

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.olympics.olympicsandroid.R;
import com.olympics.olympicsandroid.view.activity.eventActivities.EventActivity;


public class ReminderService extends IntentService {

    private static final String REMINDER_SUFFIX_TXT = " has started!";

    public ReminderService() {
        super("ReminderService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Intent notificationIntent = new Intent(this, EventActivity.class);
        final Bundle intentExtras = intent.getExtras();
        notificationIntent.putExtra(UtilityMethods.EXTRA_EVENT_ID, intentExtras.getString
                (UtilityMethods.EXTRA_EVENT_ID));
        notificationIntent.putExtra(UtilityMethods.EXTRA_UNIT_NAME, intentExtras.getString
                (UtilityMethods.EXTRA_UNIT_NAME));
        notificationIntent.putExtra(UtilityMethods.EXTRA_DESCIPLINE_NAME ,intentExtras.getString
                (UtilityMethods.EXTRA_DESCIPLINE_NAME));
        notificationIntent.putExtra(UtilityMethods.EXTRA_UNIT_ID,intentExtras.getString
                (UtilityMethods.EXTRA_UNIT_ID));
        notificationIntent.putExtra(UtilityMethods.EXTRA_UNIT_START_DATE,intentExtras.getString(UtilityMethods.EXTRA_UNIT_START_DATE));
        PendingIntent contentIntent = PendingIntent.getActivity(this, intentExtras.getString
                (UtilityMethods.EXTRA_UNIT_ID).hashCode(), notificationIntent, 0);

        if (intentExtras != null) {
            StringBuffer notificationTxt = new StringBuffer(intentExtras.getString(UtilityMethods
                    .EXTRA_DESCIPLINE_NAME) + " " + intentExtras
                    .getString(UtilityMethods.EXTRA_UNIT_NAME) + REMINDER_SUFFIX_TXT);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(getString(R.string.app_name)).setContentText
                            (notificationTxt).setSmallIcon(R.drawable.app_icon).setDefaults
                            (Notification.DEFAULT_SOUND).setAutoCancel(true).setWhen(System
                            .currentTimeMillis()).setContentIntent(contentIntent);
            NotificationManager mNotifyMgr = (NotificationManager) getSystemService
                    (NOTIFICATION_SERVICE);
            // Builds the notification and issues it.
            mNotifyMgr.notify(intentExtras.getString(UtilityMethods.EXTRA_UNIT_ID).hashCode()
                    , mBuilder.build());
        }
    }
}




