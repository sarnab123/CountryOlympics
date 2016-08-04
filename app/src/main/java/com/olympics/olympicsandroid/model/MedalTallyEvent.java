package com.olympics.olympicsandroid.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by tkmagz4 on 8/3/16.
 */
@Root(name = "event",strict = false)
public class MedalTallyEvent {

    @Element(required = false)
    private OlympicSport sport;

    @Element(required = false)
    private OlympicDiscipline discipline;

    @ElementList(required = false, inline=true, name="competitor")
    private List<MedalTallyCompetitor> competitor;

    @Attribute(required = false)
    private String id;

    @Attribute(required = false)
    private String updated;

    @Attribute(required = false)
    private String description;

    @Attribute(required = false)
    private String gender;

    @Attribute(required = false)
    private int gold;

    @Attribute(required = false)
    private int silver;

    @Attribute(required = false)
    private int bronze;

    @Attribute(required = false)
    private String odf_id;

    public String getDescription() {
        return description;
    }

    public int getGold() {
        return gold;
    }

    public int getSilver() {
        return silver;
    }

    public int getBronze() {
        return bronze;
    }

     public List<MedalTallyCompetitor> getCompetitor() {
       return competitor;
    }

    public OlympicSport getSport() {
        return sport;
    }

    public OlympicDiscipline getDiscipline() {
        return discipline;
    }

}
