package com.olympics.olympicsandroid.networkLayer.cache.database.iFace;

/**
 * Created by sarnab.poddar on 7/16/16.
 */

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.olympics.olympicsandroid.OlympicsApplication;

/**
 * This class will be used as a Database Helper for high level database
 * functions. i.e. Create Database, Create Tables, etc
 */
public final class DBHelper extends SQLiteOpenHelper {

    @SuppressWarnings("unused")
    private static final String TAG = DBHelper.class.getSimpleName();

    // Database name.
    private static final String DATABASE_NAME = "OlympicsAndroid.db";

    // Version of database
    private static final int DATABASE_VERSION = 3;

    private static DBHelper mInstance;

    private DBHelper() {
        super(OlympicsApplication.getAppContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Get instance of database helper.
     *
     * @return instance of database helper
     */
    public static DBHelper getInstance() {
        if (mInstance == null) {
            mInstance = new DBHelper();
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        // Creating Tables
        database.execSQL(DBQueries.CREATE_TABLE_UNIT_STATUS.getQuery());
        database.execSQL(DBQueries.CREATE_TABLE_COMPETITOR_LIST.getQuery());
        database.execSQL(DBQueries.CREATE_TABLE_REMINDER_LIST.getQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion,
                          int newVersion) {
        deleteTablesInUpgrade(oldVersion,db);
        onCreate(db);
    }

    private void deleteTablesInUpgrade(int oldVersion,SQLiteDatabase db) {
        if(oldVersion <= 3) {
            if(oldVersion < 3) {
                db.execSQL(DBQueries.DROP_TABLE_UNIT_STATUS.getQuery());
                db.execSQL(DBQueries.DROP_TABLE_COMPETITOR_LIST.getQuery());
            }
            db.execSQL(DBQueries.DROP_TABLE_REMINDER_LIST.getQuery());
        }

    }

}

