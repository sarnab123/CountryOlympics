package com.olympics.olympicsandroid.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tkmagz4 on 7/10/16.
 */

@Root(name="olympics")
public class MedalTally implements IResponseModel, Serializable{

    @Attribute
    private String generated;

    @Attribute
    private String id;

    @Attribute
    private String alias;

    @ElementList(name = "organizations")
    private List<MedalTallyOrganization> organizations;

    public List<MedalTallyOrganization> getOrganization() {
        return organizations;
    }
}
