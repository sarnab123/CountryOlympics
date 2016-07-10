package com.olympics.olympicsandroid.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

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

    @Element(required = false)
    private OlympicVenue venue;
}
