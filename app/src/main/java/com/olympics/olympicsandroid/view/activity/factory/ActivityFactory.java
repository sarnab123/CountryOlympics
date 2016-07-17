package com.olympics.olympicsandroid.view.activity.factory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.olympics.olympicsandroid.view.activity.AthleteActivity;
import com.olympics.olympicsandroid.view.activity.CountrySelectionActivity;
import com.olympics.olympicsandroid.view.activity.MedalTallyActivity;
import com.olympics.olympicsandroid.view.activity.OlympicsActivity;
import com.olympics.olympicsandroid.view.activity.eventActivities.EventActivity;
import com.olympics.olympicsandroid.view.fragment.ExpandableListAdapter;

/**
 * Created by sarnab.poddar on 7/9/16.
 */
public class ActivityFactory
{

    public static void openCountrySelectionScreen(Context originCTX,Intent countrySelectionLaunch)
    {
        countrySelectionLaunch.setClass(originCTX,CountrySelectionActivity.class);

        originCTX.startActivity(countrySelectionLaunch);
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

    public static void handleItemClickActivity(Activity ctx,ExpandableListAdapter.Item item)
    {
            Intent intentExtra = new Intent(ctx, EventActivity.class);
            intentExtra.putExtra("event_id",item.eventUnitModel.getEventID());
            intentExtra.putExtra("event_unit_name",item.eventUnitModel.getUnitName());
            intentExtra.putExtra("discipline_name",item.eventUnitModel.getParentDisciple());
            intentExtra.putExtra("event_unit_id",item.eventUnitModel.getUnitID());

            ctx.startActivity(intentExtra);
    }

}
