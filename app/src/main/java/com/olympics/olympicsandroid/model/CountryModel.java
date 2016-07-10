package com.olympics.olympicsandroid.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by sarnab.poddar on 7/7/16.
 */

@Root(name="olympics")
public class CountryModel implements IResponseModel
{
    @Attribute
    private String generated;

    @Attribute
    private String id;
    @Attribute
    private String alias;


    @ElementList
    private List<Organization> organizations;


    public List<Organization> getOrganization() {
            return organizations;
        }

}
