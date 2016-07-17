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
    COUNT("SELECT COUNT(1) COUNT FROM ?"),
    DROP_TABLE_UNIT_STATUS("DROP TABLE IF EXISTS " + DBTablesDef.T_UNIT_RELATION);

    private String query;

    private DBQueries(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}

