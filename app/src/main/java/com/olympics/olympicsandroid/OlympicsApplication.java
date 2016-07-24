package com.olympics.olympicsandroid;

import android.app.Application;
import android.content.Context;

import com.olympics.olympicsandroid.networkLayer.VolleySingleton;
import com.olympics.olympicsandroid.networkLayer.cache.database.OlympicsPrefs;
import com.olympics.olympicsandroid.utility.SportsUtility;

/**
 * Created by sarnab.poddar on 7/8/16.
 */
public class OlympicsApplication extends Application
{

    OlympicsPrefs olympicsPrefs;
    VolleySingleton volleySingleton;
    private static OlympicsApplication instance;

    private SportsUtility sportsUtility;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        olympicsPrefs = OlympicsPrefs.getInstance(this);
        volleySingleton = VolleySingleton.getInstance(this);
        sportsUtility = SportsUtility.getInstance();
        registerActivityLifecycleCallbacks(new OlympicsLifeCyclecallbacks());
    }

    public static Context getAppContext()
    {
        return instance.getApplicationContext();
    }


}
