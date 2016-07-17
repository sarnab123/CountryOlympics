package com.olympics.olympicsandroid.networkLayer.cache.database.iFace;

import android.content.ContentValues;
import android.database.Cursor;

import com.olympics.olympicsandroid.model.UnitVOModel;

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


    public ContentValues getContentValues(String tableName,
                                          Object vo) {
        ContentValues values = null;
        if (DBTablesDef.T_UNIT_RELATION.equalsIgnoreCase(tableName)) {
            values = getUnitValues(vo);
        }
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
}
