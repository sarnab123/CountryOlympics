package com.olympics.olympicsandroid.utility;

import android.content.Context;
import android.os.Environment;

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
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
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
}
