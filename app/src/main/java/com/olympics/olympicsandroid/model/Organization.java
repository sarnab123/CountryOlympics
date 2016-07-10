package com.olympics.olympicsandroid.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by sarnab.poddar on 7/9/16.
 */
@Root(strict = false)
public class Organization {
    @Attribute
    private String id;

    @Attribute
    private String alias;

    @Attribute
    private String description;

    @ElementList
    private List<OlympicEvent> events;

    public String getAlias() {
        return alias;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public List<OlympicEvent> getEvents() {
        return events;
    }
}