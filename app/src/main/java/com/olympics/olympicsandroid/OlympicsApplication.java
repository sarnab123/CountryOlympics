package com.olympics.olympicsandroid;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.olympics.olympicsandroid.networkLayer.VolleySingleton;
import com.olympics.olympicsandroid.networkLayer.cache.database.OlympicsPrefs;

import com.google.android.gms.analytics.Tracker;
import com.olympics.olympicsandroid.utility.SportsUtility;

/**
 * Created by sarnab.poddar on 7/8/16.
 */
public class OlympicsApplication extends Application
{

    OlympicsPrefs olympicsPrefs;
    VolleySingleton volleySingleton;
    private static OlympicsApplication instance;
    private Tracker mTracker;

    private SportsUtility sportsUtility;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        olympicsPrefs = OlympicsPrefs.getInstance(this);
        volleySingleton = VolleySingleton.getInstance(this);
        sportsUtility = SportsUtility.getInstance();
    }

    public static Context getAppContext()
    {
        return instance.getApplicationContext();
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }


}
