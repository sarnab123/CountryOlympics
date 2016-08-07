package com.olympics.olympicsandroid;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.olympics.olympicsandroid.model.AppVersionData;
import com.olympics.olympicsandroid.model.ErrorModel;
import com.olympics.olympicsandroid.networkLayer.cache.database.OlympicsPrefs;
import com.olympics.olympicsandroid.networkLayer.cache.file.DataCacheHelper;
import com.olympics.olympicsandroid.networkLayer.cache.service.AppCacheService;
import com.olympics.olympicsandroid.networkLayer.controller.AppVersionController;
import com.olympics.olympicsandroid.networkLayer.controller.IConfigListener;
import com.olympics.olympicsandroid.utility.Logger;

import java.util.TimeZone;

/**
 * Created by sarnab.poddar on 7/24/16.
 */
public class OlympicsLifeCyclecallbacks implements Application.ActivityLifecycleCallbacks {


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
        ++resumed;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        ++paused;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (started == stopped) {
            Logger.logs("OlympicLifeCyCallbacks", "On Start of App " + activity);
            FirebaseAnalytics.getInstance(OlympicsApplication.getAppContext()).logEvent(FirebaseAnalytics.Event.APP_OPEN, null);
            AppVersionController appVersionController = new AppVersionController();
            appVersionController.getAppConfiguration(createNewIconfligListener());
        }


        ++started;
    }

    private IConfigListener createNewIconfligListener() {
        return new IConfigListener() {
            @Override
            public void onConfigSuccess(AppVersionData appVersionData) {
                if (appVersionData != null) {

                    if (!TextUtils.isEmpty(appVersionData.getApiKey()) && !TextUtils.isEmpty(appVersionData.getBaseURL())) {
                        //Set APIKey and BaseURL from the configuration file
                        OlympicsPrefs.getInstance(null).setAPIKey(appVersionData.getApiKey());
                        OlympicsPrefs.getInstance(null).setBaseURL(appVersionData.getBaseURL());
                    }

                    if (!TextUtils.isEmpty(appVersionData.getHardCheckDate())) {
                        OlympicsPrefs.getInstance(null).setTimeReset(appVersionData.getHardCheckDate());
                    }

                    if (!TextUtils.isEmpty(appVersionData.getIsAdsEnabled())) {
                        OlympicsPrefs.getInstance(null).setAdsEnabled(appVersionData.getIsAdsEnabled());
                    }

                    if (!TextUtils.isEmpty(appVersionData.getCacheConfigDate())) {
                        OlympicsApplication.getAppInstance().setCacheStartDate(Long.parseLong(appVersionData.getCacheConfigDate()));
                    }

                    String oldTimezone = OlympicsPrefs.getInstance(null).getPrevTimeZone();
                    String newTimezone = TimeZone.getDefault().getID();

                    long now = System.currentTimeMillis();

                    if (oldTimezone == null)
                    {
                        OlympicsPrefs.getInstance(null).setPrevTimeZone(newTimezone);
                    }
                    else if(TimeZone.getTimeZone(oldTimezone).getOffset(now) != TimeZone.getTimeZone(newTimezone).getOffset(now)) {
                        OlympicsPrefs.getInstance(null).setPrevTimeZone(newTimezone);
                        OlympicsPrefs.getInstance(null).setCacheChecksum(appVersionData.getCacheCountryChecksum());

                        DataCacheHelper.getInstance().getDataModel(DataCacheHelper.CACHE_COUNTRY_MODEL, null, null, true);
                    }
                    else if (appVersionData != null && !TextUtils.isEmpty(appVersionData.getCacheCountryChecksum())) {
                        if (!TextUtils.isEmpty(appVersionData.getOnDemandCountryAlias())) {
                            OlympicsPrefs.getInstance(null).setCacheCountry(appVersionData.getOnDemandCountryAlias());
                        } else {
                            OlympicsPrefs.getInstance(null).setCacheCountry(DataCacheHelper.countryToCache);
                        }

                        if (TextUtils.isEmpty(OlympicsPrefs.getInstance(null).getCacheChecksum())) {
                            OlympicsPrefs.getInstance(null).setCacheChecksum(appVersionData.getCacheCountryChecksum());
                            Intent msgIntent = new Intent(OlympicsApplication.getAppContext(), AppCacheService.class);
                            OlympicsApplication.getAppContext().startService(msgIntent);
                        } else if (!OlympicsPrefs.getInstance(null).getCacheChecksum().equalsIgnoreCase(appVersionData.getCacheCountryChecksum())) {
                            OlympicsPrefs.getInstance(null).setCacheChecksum(appVersionData.getCacheCountryChecksum());

                            DataCacheHelper.getInstance().getDataModel(DataCacheHelper.CACHE_COUNTRY_MODEL, null, null, true);
                        }
                    }
                }
            }

            @Override
            public void onConfigFailure(ErrorModel errorModel) {

            }
        };
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ++stopped;
    }

    // If you want a static function you can use to check if your application is
    // foreground/background, you can use the following:
    // Replace the four variables above with these four
    private static int resumed;
    private static int paused;
    private static int started;
    private static int stopped;

    // And these two public static functions
    public static boolean isApplicationVisible() {
        return started > stopped;
    }

    public static boolean isApplicationInForeground() {
        return resumed > paused;
    }
}
