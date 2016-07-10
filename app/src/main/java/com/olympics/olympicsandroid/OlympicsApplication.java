package com.olympics.olympicsandroid;

import android.app.Application;

import com.olympics.olympicsandroid.networkLayer.VolleySingleton;
import com.olympics.olympicsandroid.networkLayer.database.OlympicsPrefs;

/**
 * Created by sarnab.poddar on 7/8/16.
 */
public class OlympicsApplication extends Application
{

    OlympicsPrefs olympicsPrefs;
    VolleySingleton volleySingleton;

    @Override
    public void onCreate() {
        super.onCreate();

        olympicsPrefs = OlympicsPrefs.getInstance(this);
        volleySingleton = VolleySingleton.getInstance(this);

    }


}
