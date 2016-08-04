package com.olympics.olympicsandroid.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Created by tkmagz4 on 8/3/16.
 */
public class MedalTallyBreakdownOrganization {
    @Attribute
    private String id;

    @Attribute(required = false)
    private String alias;


    @Attribute(required = false)
    private String description;

    @Attribute(required = false)
    private String gold;

    @Attribute(required = false)
    private String silver;

    @Attribute(required = false)
    private String bronze;

    @Attribute(required = false)
    private String total;

    @Attribute(required = false)
    private String updated;

        @ElementList(name = "events", required = false)
        private List<MedalTallyEvent> medalTallyEvents;

        public List<MedalTallyEvent> getMedalTallyEvents() {
            return medalTallyEvents;
        }

}
