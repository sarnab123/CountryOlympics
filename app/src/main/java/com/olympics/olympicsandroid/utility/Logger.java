package com.olympics.olympicsandroid.utility;

import android.util.Log;

import com.olympics.olympicsandroid.BuildConfig;

/**
 * Created by sarnab.poddar on 7/25/16.
 */
public class Logger
{
    public static void logs(String TAG,String logText)
    {
        if(BuildConfig.DEBUG)
        {
            Log.d(TAG,logText);
        }
    }
}
