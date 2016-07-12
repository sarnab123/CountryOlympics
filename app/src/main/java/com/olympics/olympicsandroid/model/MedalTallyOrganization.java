package com.olympics.olympicsandroid.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Created by tkmagz4 on 7/11/16.
 */
@Root(name = "organization", strict = false)
public class MedalTallyOrganization {

    @Attribute
    private String id;

    @Attribute
    private String description;

    @Attribute
    private String alias;

    @Attribute(required = false)
    private String gold;

    @Attribute(required = false)
    private String silver;

    @Attribute(required = false)
    private String bronze;

    @Attribute(required = false)
    private String total;

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
    }

