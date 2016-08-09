package com.olympics.olympicsandroid.networkLayer.cache.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.olympics.olympicsandroid.model.presentationModel.EventReminder;
import com.olympics.olympicsandroid.networkLayer.cache.database.iFace.DBOperationsHelper;
import com.olympics.olympicsandroid.networkLayer.cache.database.iFace.DBTablesDef;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tkmagz4 on 8/8/16.
 */
public class DBReminderHelper extends DBOperationsHelper {

    private DBReminderHelper helper;

    public DBReminderHelper getHelper() {
        if (helper == null) {
            helper = new DBReminderHelper();
        }
        return helper;
    }

    public void insertReminder(EventReminder eventReminder) {

        SQLiteDatabase db = getWritableDb();
        long returnVal = insertReminderToDB(DBTablesDef.T_REMINDER_RELATION, eventReminder, db);
        dispose(db);
    }

    public void deleteReminder(String unitId) {

        SQLiteDatabase db = getWritableDb();
        delete(DBTablesDef.T_REMINDER_RELATION, DBTablesDef.C_UNIT_ID, unitId);
        dispose(db);
    }

    public List<EventReminder> getReminders() {

        List<EventReminder> eventReminderList = new ArrayList<>();
        String[] columns = new String[]{DBTablesDef.C_EVENT_ID, DBTablesDef.C_UNIT_NAME,
                DBTablesDef.C_UNIT_ID, DBTablesDef.C_DISCIPLINE_NAME, DBTablesDef.C_UNIT_START_DATE};
        Cursor cursor = getReadableDb().query(DBTablesDef.T_REMINDER_RELATION, columns, null,
                null, null, null, null);

        while (cursor.moveToNext()) {
            EventReminder eventReminder = new EventReminder();
            eventReminder.setEventId(cursor.getString(cursor.getColumnIndex(DBTablesDef.C_EVENT_ID)));
            eventReminder.setUnitName(cursor.getString(cursor.getColumnIndex(DBTablesDef.C_UNIT_NAME)));
            eventReminder.setUnitId(cursor.getString(cursor.getColumnIndex(DBTablesDef.C_UNIT_ID)));
            eventReminder.setDisciplineName(cursor.getString(cursor.getColumnIndex(DBTablesDef
                    .C_DISCIPLINE_NAME)));
            eventReminder.setUnitStartDate(cursor.getString(cursor.getColumnIndex(DBTablesDef
                    .C_UNIT_START_DATE)));
            eventReminderList.add(eventReminder);
        }
        return eventReminderList;
    }
}
