package com.olympics.olympicsandroid.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Created by sarnab.poddar on 7/16/16.
 */
@Root(name = "score")
public class ScoreModel {

    @Attribute(required = false)
    private String code;

    @Attribute(required = false)
    private String description;

    @Attribute(required = false)
    private String number;

    @Attribute(required = false)
    private String score;

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getNumber() {
        return number;
    }

    public String getScore() {
        return score;
    }

}
