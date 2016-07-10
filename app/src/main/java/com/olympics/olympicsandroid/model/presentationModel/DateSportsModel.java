package com.olympics.olympicsandroid.model.presentationModel;

import java.util.List;

/**
 * Created by sarnab.poddar on 7/10/16.
 */
public class DateSportsModel
{

    private String dateString;

    private List<SportsEventsUnits> allSportsForDate;

    public List<SportsEventsUnits> getAllSportsForDate() {
        return allSportsForDate;
    }

    public String getDateString() {
        return dateString;
    }

    public void setAllSportsForDate(List<SportsEventsUnits> allSportsForDate) {
        this.allSportsForDate = allSportsForDate;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public class SportsEventsUnits
    {
        private String sportsTitle;

        private List<EventUnitModel> eventsModelList;

        public List<EventUnitModel> getEventsModelList() {
            return eventsModelList;
        }

        public String getSportsTitle() {
            return sportsTitle;
        }

        public void setEventsModelList(List<EventUnitModel> eventsModelList) {
            this.eventsModelList = eventsModelList;
        }

        public void setSportsTitle(String sportsTitle) {
            this.sportsTitle = sportsTitle;
        }
    }

}
