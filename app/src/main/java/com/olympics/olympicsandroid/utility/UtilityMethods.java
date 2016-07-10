package com.olympics.olympicsandroid.utility;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by sarnab.poddar on 7/9/16.
 */
public class UtilityMethods
{

    public static boolean isSimulated = true;
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
}
