package com.olympics.olympicsandroid.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Created by sarnab.poddar on 7/9/16.
 */
@Root(name = "athlete",strict = false)
public class OlympicAthlete
{
    @Attribute
    private String id;

    @Attribute
    private String print_name;

    @Attribute
    private String birth_date;



}
