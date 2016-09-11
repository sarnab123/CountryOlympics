package com.olympics.olympicsandroid.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.olympics.olympicsandroid.OlympicsApplication;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by sarnab.poddar on 7/9/16.
 */
public class UtilityMethods
{

    public static boolean isSimulated = false;
    public static final int CACHE_DIR = 1001;

    public static final int EXTERNAL_CACHE_DIR = CACHE_DIR + 1;

    public static final String ERROR_INTERNET = "800";

    public static final String EXTRA_SELECTED_COUNTRY = "SELECTED_COUNTRY";
    public static final String EXTRA_REMINDER_DATA   = "reminder_data";
    public static final String EXTRA_EVENT_ID   = "event_id";
    public static final String EXTRA_UNIT_ID  = "event_unit_id";
    public static final String EXTRA_UNIT_NAME   = "event_unit_name";
    public static final String EXTRA_DESCIPLINE_NAME   = "discipline_name";
    public static final String EXTRA_UNIT_START_DATE  = "event_unit_date";
    public static final String EVENT_RESULTS  = "_EVENT_RESULTS";
    public static final String MEDAL_TALLY_BY_ORGANIZATION  = "_MEDAL_TALLY_BY_ORGANIZATION";
    public static final String COUNTRY_CONFIG  = "_COUNTRY_CONFIG";



    /**
     * Helper Function to Load json From Assets Folder
     */
    public static String loadDataFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }


    public static File createFile(String mFileName) {
        // Get the file object - 1) Check if there is SD card present, if
        // present - use it, or move to the application Internal Memory cache

        File mFile = new File(UtilityMethods.getFileData(OlympicsApplication.getAppContext(),
                UtilityMethods.hasSDCard(OlympicsApplication.getAppContext()) ? EXTERNAL_CACHE_DIR
                        : CACHE_DIR),
                mFileName);
        // IF file object does not exists, create it.
        if (!mFile.exists()) {
            try {
                mFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        return mFile;
    }

    public static void deleteCache(Context context) {
        try {
            File dir = getFileData(OlympicsApplication.getAppContext(),
                    UtilityMethods.hasSDCard(OlympicsApplication.getAppContext()) ? EXTERNAL_CACHE_DIR
                            : CACHE_DIR);
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public static boolean doesExist(String mFileName)
    {
        File mFile = new File(UtilityMethods.getFileData(OlympicsApplication.getAppContext(),
                UtilityMethods.hasSDCard(OlympicsApplication.getAppContext()) ? EXTERNAL_CACHE_DIR
                        : CACHE_DIR),
                mFileName);

        if(mFile != null) {
            return mFile.exists();
        }
        return false;
    }

    public static boolean hasSDCard(Context ctx) {
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            return true;
//        }
        return false;
    }

    public static File getFileData(Context ctx, int type) {
        switch (type) {
            case CACHE_DIR:
                return ctx.getFilesDir();

            case EXTERNAL_CACHE_DIR:
                return ctx.getExternalCacheDir();
        }

        return null;
    }

    public static boolean isConnectedToInternet()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) OlympicsApplication.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
