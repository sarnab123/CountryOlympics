package com.olympics.olympicsandroid.networkLayer.cache.database.iFace;

import android.content.ContentValues;
import android.database.Cursor;

import com.olympics.olympicsandroid.model.CompetitorVOModel;
import com.olympics.olympicsandroid.model.UnitVOModel;
import com.olympics.olympicsandroid.model.presentationModel.EventReminder;

/**
 * Created by sarnab.poddar on 7/16/16.
 */
public class VOProcessor {
    private static VOProcessor ourInstance = new VOProcessor();

    public static VOProcessor getInstance() {
        return ourInstance;
    }

    private VOProcessor() {
    }

    public Object getVO(String tableName,
                        final Cursor cursor) {
        Object vo = null;
        if (DBTablesDef.T_UNIT_RELATION.equalsIgnoreCase(tableName)) {
            vo = getUnitVO(cursor);
        } else if (DBTablesDef.T_COMPETITOR_RELATION.equalsIgnoreCase(tableName)) {
            vo = getCompetitorVO(cursor);
        }

        return vo;
    }

    private UnitVOModel getUnitVO(Cursor cursor)
    {
        UnitVOModel unitVOModel = new UnitVOModel();
        unitVOModel.setUnitID(cursor.getString(cursor.getColumnIndex(DBTablesDef.C_UNIT_ID_INDEX)));
        unitVOModel.setUnitStatus(cursor.getString(cursor.getColumnIndex(DBTablesDef.C_UNIT_STATUS_ID)));
        return unitVOModel;
    }

    private CompetitorVOModel getCompetitorVO(Cursor cursor)
    {
        CompetitorVOModel competitorVOModel = new CompetitorVOModel();
        competitorVOModel.setCompetitorID(cursor.getString(cursor.getColumnIndex(DBTablesDef.C_COMPETITOR_ID)));
        competitorVOModel.setCompetitorName(cursor.getString(cursor.getColumnIndex(DBTablesDef.C_COMPETITOR_NAME)));
        competitorVOModel.setOrgAlias(cursor.getString(cursor.getColumnIndex(DBTablesDef.C_ORG_ALIAS)));

        return competitorVOModel;
    }

    public ContentValues getContentValues(String tableName,
                                          Object vo) {
        ContentValues values = null;
        if (DBTablesDef.T_UNIT_RELATION.equalsIgnoreCase(tableName)) {
            values = getUnitValues(vo);
        } else  if (DBTablesDef.T_COMPETITOR_RELATION.equalsIgnoreCase(tableName)) {
            values = getCompetitorValues(vo);
        } else  if (DBTablesDef.T_REMINDER_RELATION.equalsIgnoreCase(tableName)) {
            values = getReminderValues(vo);
        }
        return values;
    }

    private ContentValues getCompetitorValues(Object ivo) {
        ContentValues values = new ContentValues();
        CompetitorVOModel vo = (CompetitorVOModel) ivo;
        if (vo.getCompetitorID() != null) {
            values.put(DBTablesDef.C_COMPETITOR_ID, vo.getCompetitorID());
        }
        values.put(DBTablesDef.C_COMPETITOR_NAME, vo.getCompetitorName());
        values.put(DBTablesDef.C_ORG_ALIAS, vo.getOrgAlias());

        return values;
    }

    private ContentValues getUnitValues(final Object ivo) {
            ContentValues values = new ContentValues();
        UnitVOModel vo = (UnitVOModel) ivo;
            if (vo.getUnitID() != null) {
                values.put(DBTablesDef.C_UNIT_ID, vo.getUnitID());
            }
            values.put(DBTablesDef.C_UNIT_STATUS, vo.getUnitStatus());

        return values;
    }

    private ContentValues getReminderValues(Object ivo) {
        ContentValues values = new ContentValues();
        EventReminder vo = (EventReminder) ivo;
        if (vo.getEventId() != null) {
            values.put(DBTablesDef.C_EVENT_ID, vo.getEventId());
        }
        if (vo.getUnitName() != null) {
            values.put(DBTablesDef.C_UNIT_NAME, vo.getUnitName());
        }
        if (vo.getUnitStartDate() != null) {
            values.put(DBTablesDef.C_UNIT_START_DATE, vo.getUnitStartDate());
        }
        if (vo.getUnitId() != null) {
            values.put(DBTablesDef.C_UNIT_ID, vo.getUnitId());
        }
        if (vo.getDisciplineName() != null) {
            values.put(DBTablesDef.C_DISCIPLINE_NAME, vo.getDisciplineName());
        }
        return values;
    }
}
