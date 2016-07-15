package com.olympics.olympicsandroid.view.activity.factory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.olympics.olympicsandroid.view.activity.AthleteActivity;
import com.olympics.olympicsandroid.view.activity.CountrySelectionActivity;
import com.olympics.olympicsandroid.view.activity.MedalTallyActivity;
import com.olympics.olympicsandroid.view.activity.OlympicsActivity;

/**
 * Created by sarnab.poddar on 7/9/16.
 */
public class ActivityFactory
{

    public static void openCountrySelectionScreen(Context originCTX,Bundle extras)
    {
        Intent countrySelectionLaunch = new Intent(originCTX,CountrySelectionActivity.class);

        originCTX.startActivity(countrySelectionLaunch,extras);
    }

    public static void openMainActivity(Context originCTX, Bundle extras)
    {
        Intent homeLaunch = new Intent(originCTX,OlympicsActivity.class);
        originCTX.startActivity(homeLaunch);
    }

    public static void openAthleteActivity(Activity originAct,Bundle extras)
    {
        Intent homeLaunch = new Intent(originAct,AthleteActivity.class);
        originAct.startActivity(homeLaunch);

    }

    public static void openMedalActivity(Activity originAct,Bundle extras)
    {
        Intent homeLaunch = new Intent(originAct,MedalTallyActivity.class);
        originAct.startActivity(homeLaunch);

    }
}
