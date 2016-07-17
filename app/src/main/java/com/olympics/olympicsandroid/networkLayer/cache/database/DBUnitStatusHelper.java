package com.olympics.olympicsandroid.networkLayer.cache.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.olympics.olympicsandroid.model.presentationModel.EventUnitModel;
import com.olympics.olympicsandroid.networkLayer.cache.database.iFace.DBOperationsHelper;
import com.olympics.olympicsandroid.networkLayer.cache.database.iFace.DBTablesDef;

/**
 * Created by sarnab.poddar on 7/16/16.
 */
public class DBUnitStatusHelper extends DBOperationsHelper
{

    private DBUnitStatusHelper helper;

    protected DBUnitStatusHelper getHelper() {
        if (helper == null) {
            helper = new DBUnitStatusHelper();
        }
        return helper;
    }


    @Override
    public int count(String tableName) {
        return super.count(tableName);
    }

    public int updateStatusOfUnit(String status, String itemIndex)
    {
        ContentValues value = new ContentValues();

        value.put(DBTablesDef.C_UNIT_STATUS, status);

        SQLiteDatabase db = getWritableDb();
        int returnValue = db.update(DBTablesDef.T_UNIT_RELATION,
                value,
                DBTablesDef.C_UNIT_ID + " = ?",
                new String[]{
                        itemIndex
                });

        return returnValue;
    }

    public String getStatusofUnit(String itemIndex)
    {
        String itemStatus = EventUnitModel.UNIT_STATUS_NOT_SCHEDULED;
        SQLiteDatabase db = getReadableDb();
        Cursor cursor = db.query(DBTablesDef.T_UNIT_RELATION,
                null,
                DBTablesDef.C_UNIT_ID + " = ?",
                new String[]{
                        String.valueOf(itemIndex)
                },
                null,
                null,
                null,
                null);

        while (cursor.moveToNext()) {
            itemStatus = cursor.getString(cursor.getColumnIndex(DBTablesDef.C_UNIT_STATUS));
        }
        dispose(db);
        return itemStatus;
    }
}
