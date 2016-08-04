package com.olympics.olympicsandroid.model.presentationModel;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by sarnab.poddar on 7/10/16.
 */
public class Athlete implements Serializable, Comparable
{
    private String athleteName;

    private String athleteGender;

    private String sportName;

    private String disciplineName;

    private String sportsAlias;

    private String competitorID;

    private Set<String> participatingEventID;

    public Set<String> getParticipatingEventID() {
        return participatingEventID;
    }

    public void insertIntoEventSet(String eventID)
    {
        if(participatingEventID == null)
        {
            participatingEventID = new HashSet<>();
        }
        participatingEventID.add(eventID);
    }

    public String getCompetitorID() {
        return competitorID;
    }

    public String getDisciplineName() {
        return disciplineName;
    }

    public void setDisciplineName(String disciplineName) {
        this.disciplineName = disciplineName;
    }

    public void setCompetitorID(String competitorID) {
        this.competitorID = competitorID;
    }

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

    @Override
    public int compareTo(Object another) {
        if (another instanceof Athlete) {
            if (this != null && another != null) {
                if (this.getSportName().compareTo(((Athlete) another).getSportName()) == 0) {
                    return this.getAthleteName().compareTo(((Athlete) another).getAthleteName());
                } else {
                    return this.getSportName().compareTo(((Athlete) another).getSportName());
                }
            }
        }
        return 0;
    }
}
