package com.olympics.olympicsandroid.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by sarnab.poddar on 7/9/16.
 */
@Root(name = "unit",strict = false)
public class OlympicUnit
{
    @Attribute(required = false)
    private String id;

    @Attribute(required = false)
    private String name;

    @Attribute(required = false)
    private String phase;

    @Attribute(required = false)
    private String status;

    @Attribute(required = false)
    private String type;

    @Attribute(required = false)
    private String start_date;

    @Attribute(required = false)
    private String medal;

    @Element(required = false)
    private OlympicVenue venue;

    @ElementList(required = false)
    private List<EventResultCompetitor> results;

    public String getMedal() {
        return medal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public OlympicVenue getVenue() {
        return venue;
    }

    public void setVenue(OlympicVenue venue) {
        this.venue = venue;
    }

    public List<EventResultCompetitor> getResults() {
        return results;
    }
}
