package com.olympics.olympicsandroid.view.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
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

import java.util.List;

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
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkAppVersion();
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

            //shortcutIntent is added with addIntent
            Intent addIntent = new Intent();
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, HomeScreenShortCut);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(getApplicationInfo()
                    .labelRes));
            addIntent.putExtra("duplicate", false);
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
                            OlympicsApplication.getAppInstance().setCacheStartDate(DateUtils.getOlympicEventStartDate());

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
                if (!isFinishing() && isRunning(OlympicsApplication.getAppContext())) {
                    if (appVersionData.getAndroid() > pInfo.versionCode) {
                        // Display Alert Dialog

                        new AlertDialog.Builder(this).setTitle("Upgrade App").setMessage(TextUtils.isEmpty(appVersionData.getMessage()) ? DEFAULT_UPGRADE_MESSAGE : appVersionData.getMessage()).setPositiveButton(R.string.upgrade_str, new DialogInterface.OnClickListener() {

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
            }
        } catch (Exception e) {
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

    public boolean isRunning(Context ctx) {
        if (ctx != null) {
            ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
            if (activityManager != null) {
                List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);
                for (ActivityManager.RunningTaskInfo task : tasks) {
                    if (ctx.getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName()))
                    return true;
                }
            }
        }
        return false;
    }
}

