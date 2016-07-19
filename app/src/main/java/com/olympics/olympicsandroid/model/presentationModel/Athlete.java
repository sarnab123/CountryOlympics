package com.olympics.olympicsandroid.model.presentationModel;

import java.io.Serializable;

/**
 * Created by sarnab.poddar on 7/10/16.
 */
public class Athlete implements Serializable
{
    private String athleteName;

    private String athleteGender;

    private String sportName;

    private String sportsAlias;

    public String getAthleteGender() {
        return athleteGender;
    }

    public String getAthleteName() {
        return athleteName;
    }

    public void setAthleteGender(String athleteGender) {
        this.athleteGender = athleteGender;
    }

    public void setAthleteName(String athleteName) {
        this.athleteName = athleteName;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String getSportsAlias() {
        return sportsAlias;
    }

    public void setSportsAlias(String sportsAlias) {
        this.sportsAlias = sportsAlias;
    }
}
