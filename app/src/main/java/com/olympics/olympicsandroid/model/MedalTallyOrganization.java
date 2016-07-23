package com.olympics.olympicsandroid.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Created by tkmagz4 on 7/11/16.
 */
@Root(name = "organization", strict = false)
public class MedalTallyOrganization implements Serializable{

    @Attribute
    private String id;

    @Attribute
    private String alias;

    @Attribute
    private String description;

    @Attribute(required = false)
    private String gold = "0";

    @Attribute(required = false)
    private String silver = "0";

    @Attribute(required = false)
    private String bronze = "0";

    @Attribute(required = false)
    private String total = "0";

    @Attribute(required = false)
    private int rank;

    public String getAlias() {
        return alias;
    }

    public String getTotal() {
        return total;
    }

    public String getId() {
        return id;
    }

    public String getSilver() {
        return silver;
    }

    public String getGold() {
        return gold;
    }

    public String getBronze() {
        return bronze;
    }

    public String getCountryName() {
        return description;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }
}

