package com.olympics.olympicsandroid.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by sarnab.poddar on 7/9/16.
 */
@Root(name = "discipline",strict = false)
public class OlympicDiscipline
{
        @Attribute(required = false)
        private String description;

        @Attribute(required = false)
        private String alias;

        @ElementList(required = false)
        private List<OlympicEvent> events;

        public String getAlias() {
                return alias;
        }

        public List<OlympicEvent> getEvents() {
                return events;
        }

        public String getDescription() {
                return description;
        }
}
