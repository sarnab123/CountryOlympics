package com.olympics.olympicsandroid.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Created by tkmagz4 on 7/10/16.
 */

@Root(name="olympics")
public class MedalTallyBreakdown implements IResponseModel, Serializable{

    @Attribute
    private String generated;

    @Attribute
    private String id;

    @Attribute
    private String alias;

    @Element(required = false)
    private MedalTallyBreakdownOrganization organization;

    public MedalTallyBreakdownOrganization getOrganization() {
        return organization;
    }
}
