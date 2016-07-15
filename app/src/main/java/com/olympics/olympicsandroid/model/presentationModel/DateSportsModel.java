package com.olympics.olympicsandroid.model.presentationModel;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by sarnab.poddar on 7/10/16.
 */
public class DateSportsModel implements Serializable
{

    private long dateString;

    private Map<String, SportsEventsUnits> allSportsForDate;

    public Map<String, SportsEventsUnits> getAllSportsForDate() {
        return allSportsForDate;
    }

    public Collection<SportsEventsUnits> getSportsEventsUnits(){
        return allSportsForDate.values();
    }

    public long getDateString() {
        return dateString;
    }

    public void setAllSportsForDate(Map<String, SportsEventsUnits> allSportsForDate) {
        this.allSportsForDate = allSportsForDate;
    }

    public void setDateString(long dateString) {
        this.dateString = dateString;
    }

    public  static class SportsEventsUnits implements Serializable
    {
        private String sportsTitle;

        private List<EventUnitModel> eventUnits;

        public List<EventUnitModel> getEventUnits() {
            return eventUnits;
        }

        public String getSportsTitle() {
            return sportsTitle;
        }

        public void setEventUnits(List<EventUnitModel> eventUnits) {
            this.eventUnits = eventUnits;
        }

        public void setSportsTitle(String sportsTitle) {
            this.sportsTitle = sportsTitle;
        }
    }

}
