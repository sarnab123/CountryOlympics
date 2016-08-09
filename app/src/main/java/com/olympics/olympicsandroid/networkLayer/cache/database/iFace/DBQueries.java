package com.olympics.olympicsandroid.networkLayer.cache.database.iFace;

/**
 * Created by sarnab.poddar on 7/16/16.
 */
public enum DBQueries {
    CREATE_TABLE_UNIT_STATUS("CREATE TABLE IF NOT EXISTS " + DBTablesDef.T_UNIT_RELATION
            + "("
            + DBTablesDef.C_UNIT_ID
            + " text, "
            + DBTablesDef.C_UNIT_ID_INDEX
            + " text, "
            + DBTablesDef.C_UNIT_STATUS
            + " text,"
            + DBTablesDef.C_UNIT_STATUS_ID
            + " text,"
            + " PRIMARY KEY ("
            + DBTablesDef.C_UNIT_ID
            + "))"),
    CREATE_TABLE_COMPETITOR_LIST("CREATE TABLE IF NOT EXISTS " + DBTablesDef.T_COMPETITOR_RELATION
            + "("
            + DBTablesDef.C_COMPETITOR_ID
            + " text, "
            + DBTablesDef.C_COMPETITOR_NAME
            + " text, "
            + DBTablesDef.C_ORG_ALIAS
            + " text,"
            + " PRIMARY KEY ("
            + DBTablesDef.C_COMPETITOR_ID
            + "))"),
    CREATE_TABLE_REMINDER_LIST("CREATE TABLE IF NOT EXISTS " + DBTablesDef.T_REMINDER_RELATION
            + "("
            + DBTablesDef.C_EVENT_ID
            + " text, "
            + DBTablesDef.C_UNIT_NAME
            + " text, "
            + DBTablesDef.C_UNIT_START_DATE
            + " text, "
            + DBTablesDef.C_DISCIPLINE_NAME
            + " text, "
            + DBTablesDef.C_UNIT_ID
            + " text, "
            + " PRIMARY KEY ("
            + DBTablesDef.C_UNIT_ID
            + "))"),
    COUNT("SELECT COUNT(1) COUNT FROM ?"),
    DROP_TABLE_UNIT_STATUS("DROP TABLE IF EXISTS " + DBTablesDef.T_UNIT_RELATION),
    DROP_TABLE_COMPETITOR_LIST("DROP TABLE IF EXISTS " + DBTablesDef.T_COMPETITOR_RELATION),
    DROP_TABLE_REMINDER_LIST("DROP TABLE IF EXISTS " + DBTablesDef.T_REMINDER_RELATION);



    private String query;

    private DBQueries(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}

