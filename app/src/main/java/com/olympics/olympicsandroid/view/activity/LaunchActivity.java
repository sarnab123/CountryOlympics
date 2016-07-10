package com.olympics.olympicsandroid.view.activity;

import android.app.Activity;
import android.os.Bundle;

import com.olympics.olympicsandroid.networkLayer.database.OlympicsPrefs;
import com.olympics.olympicsandroid.view.activity.factory.ActivityFactory;

/**
 * Created by sarnab.poddar on 7/8/16.
 */
public class LaunchActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        decideLaunchActivity();
    }

    private void decideLaunchActivity() {
        if(OlympicsPrefs.getInstance(null).getUserSelectedCountry() == null)
        {
            Bundle extras = new Bundle();
            extras.putBoolean("first_launch", true);
            ActivityFactory.openCountrySelectionScreen(this,extras);
        }
        else{
            ActivityFactory.openMainActivity(this,null);
        }
        finish();
    }
}
