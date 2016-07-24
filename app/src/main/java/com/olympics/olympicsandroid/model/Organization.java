package com.olympics.olympicsandroid.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sarnab.poddar on 7/9/16.
 */
@Root(strict = false)
public class Organization implements Serializable{
    @Attribute
    private String id;

    @Attribute
    private String alias;

    @Attribute
    private String description;

    @ElementList(required = false)
    private List<OlympicEvent> events;

    public Organization() {
        this.id = "840a4893-d821-436b-8c62-f5639dcdb037";
        this.alias = "IND";
        this.description = "India";
    }

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