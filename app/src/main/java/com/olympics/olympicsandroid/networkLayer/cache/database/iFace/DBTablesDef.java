package com.olympics.olympicsandroid.networkLayer.cache.database.iFace;

/**
 * Created by sarnab.poddar on 7/16/16.
 */
public interface DBTablesDef
{

    String T_UNIT_RELATION = "T_UNIT_RELATION";
    String T_COMPETITOR_RELATION = "T_COMPETITOR_RELATION";
    String T_REMINDER_RELATION = "T_REMINDER_RELATION";

    String C_UNIT_ID = "C_UNIT_ID";
    String C_COMPETITOR_ID = "C_COMPETITOR_ID";
    String C_COMPETITOR_NAME = "C_COMPETITOR_NAME";
    String C_ORG_ALIAS = "C_ORG_ALIAS";

    String C_UNIT_ID_INDEX = "itemIndex";
    String C_UNIT_STATUS = "C_UNIT_STATUS";
    String C_UNIT_STATUS_ID = "status_id";


    String C_EVENT_ID = "C_EVENT_ID";
    String C_UNIT_NAME = "C_UNIT_NAME";
    String C_DISCIPLINE_NAME = "C_DISCIPLINE_NAME";
    String C_UNIT_START_DATE = "C_UNIT_START_DATE";
}
