package com.olympics.olympicsandroid.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by sarnab.poddar on 7/9/16.
 */
@Root(name = "sport",strict = false)
public class OlympicSport
{
    @Attribute
    private String description;

    @Attribute
    private String alias;

    @ElementList(required = false)
    private List<OlympicDiscipline> disciplines;

    public List<OlympicDiscipline> getDisciplines() {
        return disciplines;
    }

    public String getDescription() {
        return description;
    }

    public String getAlias() {
        return alias;
    }
}
