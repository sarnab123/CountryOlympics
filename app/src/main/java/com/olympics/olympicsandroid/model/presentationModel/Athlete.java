package com.olympics.olympicsandroid.model.presentationModel;

import java.util.List;

/**
 * Created by sarnab.poddar on 7/10/16.
 */
public class Athlete
{
    private String athleteName;

    private String athleteGender;

    private String sportName;

    private List<EventUnitModel> eventsParticipating;

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

    public List<EventUnitModel> getEventsParticipating() {
        return eventsParticipating;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }
}
