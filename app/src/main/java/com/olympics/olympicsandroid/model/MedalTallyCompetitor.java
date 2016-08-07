package com.olympics.olympicsandroid.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Created by tkmagz4 on 8/3/16.
 */
@Root(name = "competitor",strict = false)
public class MedalTallyCompetitor {

    @Attribute(required = false)
    private String id;

    @Attribute(required = false)
    private String last_name;

    @Attribute(required = false)
    private String first_name;

    @Attribute(required = false)
    private String type;

    @Attribute(required = false)
    private String gender;

    @Attribute(required = false)
    private String status;

    @Attribute(required = false)
    private String odf_id;

    @Attribute(required = false)
    private String medal;

    @Attribute(required = false)
    private String description;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getMedal() {
        return medal;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
