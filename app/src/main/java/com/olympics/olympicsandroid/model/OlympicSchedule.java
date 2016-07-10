package com.olympics.olympicsandroid.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by sarnab.poddar on 7/9/16.
 */
@Root(name = "olympics", strict = false)
public class OlympicSchedule implements IResponseModel
{
    @Element(name = "season-schedule")
    private SeasonSchedule seasonSchedule;

    public SeasonSchedule getSeasonSchedule() {
        return seasonSchedule;
    }
}
