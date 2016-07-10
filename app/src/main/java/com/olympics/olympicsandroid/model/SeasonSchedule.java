package com.olympics.olympicsandroid.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by sarnab.poddar on 7/9/16.
 */
@Root(name = "season-schedule",strict = false)
public class SeasonSchedule
{
    @ElementList
    private List<OlympicSport> sports;

    public List<OlympicSport> getSports() {
        return sports;
    }
}
