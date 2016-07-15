package com.olympics.olympicsandroid.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Created by tkmagz4 on 7/11/16.
 */
@Root(name = "organization", strict = false)
public class MedalTallyOrganization implements Comparable<MedalTallyOrganization>{

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

    @Override
    public int compareTo(MedalTallyOrganization medalObj) {

        try {
            return Integer.parseInt(medalObj.total) - Integer.parseInt(this.total);
        } catch (Exception ex) {
            return 0;
        }
    }

    }

