package com.olympics.olympicsandroid.model;

/**
 * Created by sarnab.poddar on 7/24/16.
 */
public class CompetitorVOModel extends Object
{
    private String competitorID;

    private String competitorName;

    private String orgAlias;

    public void setCompetitorID(String competitorID) {
        this.competitorID = competitorID;
    }

    public String getCompetitorID() {
        return competitorID;
    }

    public String getCompetitorName() {
        return competitorName;
    }

    public String getOrgAlias() {
        return orgAlias;
    }

    public void setCompetitorName(String competitorName) {
        this.competitorName = competitorName;
    }

    public void setOrgAlias(String orgAlias) {
        this.orgAlias = orgAlias;
    }
}
