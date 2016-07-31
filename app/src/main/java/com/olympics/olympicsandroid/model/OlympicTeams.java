package com.olympics.olympicsandroid.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by sarnab.poddar on 7/9/16.
 */
@Root(name = "team",strict = false)
public class OlympicTeams
{
    @Attribute
    private String description;

    @Attribute
    private String id;

    @ElementList(inline=true, entry="athlete",required = false)
    private List<OlympicAthlete> athlete;

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public List<OlympicAthlete> getAthlete() {
        return athlete;
    }
}
