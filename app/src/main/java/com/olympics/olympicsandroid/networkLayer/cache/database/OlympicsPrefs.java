package com.olympics.olympicsandroid.networkLayer.cache.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.olympics.olympicsandroid.model.Organization;

/**
 * Created by sarnab.poddar on 7/8/16.
 */
public class OlympicsPrefs
{

    private final Context appContext;

    private Gson gson;

    private final String USER_SET_COUNTRY = "USER_SET_COUNTRY";

    private static OlympicsPrefs instance;

    public static OlympicsPrefs getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new OlympicsPrefs(context);
        }

        return instance;
    }

    private OlympicsPrefs(Context context) {
        this.appContext = context;
    }

    private Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    private SharedPreferences getDefaultSharePreference() {
        return appContext.getSharedPreferences("olympicsprefs", Context.MODE_PRIVATE);
    }

    public void setSelectedCountry(Organization selectedCountry)
    {
        SharedPreferences.Editor editor = getDefaultSharePreference().edit();
        String json = getGson().toJson(selectedCountry);
        editor.putString(USER_SET_COUNTRY, json);
        editor.apply();
    }

    public Organization getUserSelectedCountry() {
        String orgJSON = getDefaultSharePreference().getString(USER_SET_COUNTRY, null);
        if (orgJSON != null && orgJSON.length() > 0) {
            Organization mOrganisation = getGson().fromJson(orgJSON, Organization.class);
            return mOrganisation;
        }
        return null;
    }

}
