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

    @Attribute
    private String gender;

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrint_name() {
        return print_name;
    }

    public void setPrint_name(String print_name) {
        this.print_name = print_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
