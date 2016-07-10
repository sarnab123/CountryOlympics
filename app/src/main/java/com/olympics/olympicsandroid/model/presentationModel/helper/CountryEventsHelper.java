package com.olympics.olympicsandroid.model.presentationModel.helper;

import com.olympics.olympicsandroid.model.CountryProfileEvents;
import com.olympics.olympicsandroid.model.OlympicDiscipline;
import com.olympics.olympicsandroid.model.OlympicEvent;
import com.olympics.olympicsandroid.model.OlympicSchedule;
import com.olympics.olympicsandroid.model.OlympicSport;
import com.olympics.olympicsandroid.model.presentationModel.AthleteModel;
import com.olympics.olympicsandroid.model.presentationModel.CountryEventUnitModel;
import com.olympics.olympicsandroid.model.presentationModel.DateSportsModel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by sarnab.poddar on 7/10/16.
 */
public class CountryEventsHelper
{

    private CountryProfileEvents countryProfileEvents;
    private OlympicSchedule olympicSchedule;

    public CountryEventsHelper(CountryProfileEvents countryProfileEvents,OlympicSchedule olympicSchedule)
    {
        this.countryProfileEvents = countryProfileEvents;
        this.olympicSchedule = olympicSchedule;
    }

    /*
     * The aim of this method is to create a CountryEventUnitModel = which will have the list of unique athletes participating for that country.
     *  and also events #DateSportsModel sorted by date.
     */
    public CountryEventUnitModel getCountrySpecificData()
    {

        CountryEventUnitModel countryEventUnitModel = new CountryEventUnitModel();

        countryEventUnitModel.setCountryAlias(countryProfileEvents.getOrganization().getAlias());
        countryEventUnitModel.setCountryName(countryProfileEvents.getOrganization().getDescription());
        countryEventUnitModel.setCountryID(countryProfileEvents.getOrganization().getId());


        countryEventUnitModel.setDatesCountryMapping(getDatesofCountry());

        return countryEventUnitModel;
    }


    private List<DateSportsModel> getDatesofCountry()
    {
        HashMap<String,AthleteModel> allAthletes;

        List<DateSportsModel> dateSportsModels;

        // Events from schedule API
        HashMap<String,OlympicEvent> eventFromSchedule = new HashMap<>();

        for(OlympicSport scheduledSports:olympicSchedule.getSeasonSchedule().getSports())
        {
            for(OlympicDiscipline olympicDiscipline:scheduledSports.getDisciplines())
            {
                for(OlympicEvent olympicEvent:olympicDiscipline.getEvents())
                {
                    eventFromSchedule.put(olympicEvent.getId(),olympicEvent);
                }
            }
        }

        for(OlympicEvent everyEvent:countryProfileEvents.getOrganization().getEvents())
        {
//            DateSportsModel dateSportsModel = new DateSportsModel();
//            dateSportsModel.setDateString();

            //TODO: go through events in CountryProfileEvents and look them up in eventFromSchedule.
            //TODO : If event exists, then fill up a DateSportsModel - which is unique for every date - comprising of
            //TODO: sports -> EventUnitModel
            // So the main aim should be to create EventUnitModel
            // While filing the EventUnitModel, also maintain the athletes in an unique map.


        }

        return null;
    }

}
