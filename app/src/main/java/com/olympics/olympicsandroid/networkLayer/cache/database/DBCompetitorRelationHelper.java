package com.olympics.olympicsandroid.networkLayer.cache.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.olympics.olympicsandroid.model.CompetitorVOModel;
import com.olympics.olympicsandroid.networkLayer.cache.database.iFace.DBOperationsHelper;
import com.olympics.olympicsandroid.networkLayer.cache.database.iFace.DBTablesDef;

import java.util.List;

/**
 * Created by sarnab.poddar on 7/16/16.
 */
public class DBCompetitorRelationHelper extends DBOperationsHelper
{

    private DBCompetitorRelationHelper helper;

    protected DBCompetitorRelationHelper getHelper() {
        if (helper == null) {
            helper = new DBCompetitorRelationHelper();
        }
        return helper;
    }


    @Override
    public int count(String tableName) {
        return super.count(tableName);
    }


    public int updateStatusOfCompetitor(String competitorName,String orgAlias, String competitorID)
    {
        ContentValues value = new ContentValues();

        value.put(DBTablesDef.C_COMPETITOR_NAME, competitorName);
        value.put(DBTablesDef.C_ORG_ALIAS, orgAlias);

        SQLiteDatabase db = getWritableDb();
        int returnValue = db.update(DBTablesDef.T_COMPETITOR_RELATION,
                value,
                DBTablesDef.C_COMPETITOR_ID + " = ?",
                new String[]{
                        competitorID
                });

        return returnValue;
    }

    public long insertAll(String tableName,
                          List<CompetitorVOModel> list) {
        SQLiteDatabase db = getWritableDb();
        long returnVal = 0;
        if (list != null) {
            for (CompetitorVOModel vo : list) {
                returnVal = performInsert(tableName, vo, db);
                if (returnVal <= -1) {
                    break;
                }
            }
        }

        dispose(db);
        return returnVal;
    }

    public CompetitorVOModel getCompetitorByID(String competitorID)
    {
        CompetitorVOModel competitorVOModel = null;
        try {
            competitorVOModel = (CompetitorVOModel)get(DBTablesDef.T_COMPETITOR_RELATION,DBTablesDef.C_COMPETITOR_ID,competitorID,CompetitorVOModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(competitorVOModel == null)
        {
            return new CompetitorVOModel();
        }
        return competitorVOModel;
    }
}
