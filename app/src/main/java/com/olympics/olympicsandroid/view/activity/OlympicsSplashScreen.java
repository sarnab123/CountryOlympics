package com.olympics.olympicsandroid.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.olympics.olympicsandroid.OlympicsApplication;
import com.olympics.olympicsandroid.R;
import com.olympics.olympicsandroid.model.AppVersionData;
import com.olympics.olympicsandroid.model.ErrorModel;
import com.olympics.olympicsandroid.networkLayer.cache.database.OlympicsPrefs;
import com.olympics.olympicsandroid.networkLayer.cache.file.DataCacheHelper;
import com.olympics.olympicsandroid.networkLayer.controller.AppVersionController;
import com.olympics.olympicsandroid.networkLayer.controller.IConfigListener;
import com.olympics.olympicsandroid.networkLayer.controller.ScheduleController;
import com.olympics.olympicsandroid.utility.DateUtils;
import com.olympics.olympicsandroid.view.activity.factory.ActivityFactory;

public class OlympicsSplashScreen extends Activity {

    private Handler mHandler;

    private Runnable myRunnable;
    private static final long SPLASH_TIME_OUT = 2000;

    private static final String DEFAULT_UPGRADE_MESSAGE = "Please upgrade your app.";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_olympics_splash_screen);
        createApplicationShortcut();
        checkAppVersion();
    }

    private void decideLaunchActivity() {
        if (OlympicsPrefs.getInstance(null).getUserSelectedCountry() == null) {
            Intent intent = new Intent();
            intent.putExtra("first_launch", true);
            ActivityFactory.openCountrySelectionScreen(this, intent);
        } else {
            ActivityFactory.openMainActivity(this, null);
        }
        finish();
    }


    /**
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void createApplicationShortcut() {

        if (!OlympicsPrefs.getInstance(null).isAppShortcutCreated()) {
            Intent HomeScreenShortCut = new Intent(getApplicationContext(), OlympicsSplashScreen.class);

            HomeScreenShortCut.setAction(Intent.ACTION_MAIN);
            HomeScreenShortCut.putExtra("duplicate", false);
            //shortcutIntent is added with addIntent
            Intent addIntent = new Intent();
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, HomeScreenShortCut);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(getApplicationInfo()
                    .labelRes));
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.app_icon));
            addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            getApplicationContext().sendBroadcast(addIntent);

            OlympicsPrefs.getInstance(null).setAppShortcutStatus();
        }
    }

    private void checkAppVersion() {

        AppVersionController appVersionController = new AppVersionController();
        appVersionController.getAppConfiguration(createConfigListener());

        ScheduleController.getInstance().getScheduleData(null);
    }

    private IConfigListener createConfigListener() {
        return new IConfigListener() {
            @Override
            public void onConfigSuccess(AppVersionData appVersionData) {
                if (appVersionData != null ) {
                    if (appVersionData != null) {
                        if(!TextUtils.isEmpty(appVersionData.getOnDemandCountryAlias()))
                        {
                            OlympicsPrefs.getInstance(null).setCacheCountry(appVersionData.getOnDemandCountryAlias());
                        }
                        else{
                            OlympicsPrefs.getInstance(null).setCacheCountry(DataCacheHelper.countryToCache);
                        }

                        performVersionValidationTask(appVersionData);
                        //Set APIKey and BaseURL from the configuration file
                        OlympicsPrefs.getInstance(null).setAPIKey(appVersionData.getApiKey());
                        OlympicsPrefs.getInstance(null).setBaseURL(appVersionData.getBaseURL());
                        if(!TextUtils.isEmpty(appVersionData.getCacheConfigDate())) {
                            OlympicsApplication.getAppInstance().setCacheStartDate(Long.parseLong(appVersionData.getCacheConfigDate()));
                        }
                        else{
                            OlympicsApplication.getAppInstance().setCacheStartDate(DateUtils.OLYMPIC_EVENT_START_DATE);

                        }
                    }
                }
            }

            @Override
            public void onConfigFailure(ErrorModel errorModel) {
                decideLaunchActivity();
            }
        };
    }

    /**
     * Validates the version of android
     */
    public void performVersionValidationTask(AppVersionData appVersionData) {

        PackageInfo pInfo = null;
        try {
            pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            if (pInfo != null) {
                if (appVersionData.getAndroid() > pInfo.versionCode) {
                    // Display Alert Dialog

                    new AlertDialog.Builder(this).setTitle("Upgrade App").setMessage(TextUtils.isEmpty(appVersionData.getMessage()) ? DEFAULT_UPGRADE_MESSAGE :
                            appVersionData.getMessage()).setPositiveButton(R.string.upgrade_str,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    openApplicationInGooglePlayStore();
                                }
                            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            decideLaunchActivity();
                        }
                    }).setCancelable(true).show();
                } else {
                    decideLaunchActivity();
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the Google play store.
     */
    private void openApplicationInGooglePlayStore() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" +
                    getPackageName()));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}

