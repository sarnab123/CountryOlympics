package com.olympics.olympicsandroid.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sarnab.poddar on 7/9/16.
 */
@Root(name="event",strict = false)
public class OlympicEvent implements Serializable
{
    @Attribute
    private String id;

    @Attribute
    private String description;

    @Attribute
    private String gender;

    @Element(required = false)
    private OlympicSport sport;

    @Element(required = false)
    private OlympicDiscipline discipline;

    @ElementList(required=false)
    private List<OlympicAthlete> participants;

    @ElementList(required=false)
    private List<OlympicTeams> teams;

    @ElementList(required = false)
    private List<OlympicUnit> units;

    public String getDescription() {
        return description;
    }

    public List<OlympicAthlete> getParticipants() {
        return participants;
    }

    public List<OlympicTeams> getTeams() {
        return teams;
    }

    public List<OlympicUnit> getUnits() {
        return units;
    }

    public OlympicDiscipline getDiscipline() {
        return discipline;
    }

    public String getGender() {
        return gender;
    }

    public String getId() {
        return id;
    }

    public OlympicSport getSport() {
        return sport;
    }

}
