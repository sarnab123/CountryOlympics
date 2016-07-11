package com.olympics.olympicsandroid.model.presentationModel;

import com.olympics.olympicsandroid.model.IResponseModel;
import com.olympics.olympicsandroid.utility.DateUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sarnab.poddar on 7/10/16.
 */
public class CountryEventUnitModel implements IResponseModel, Serializable {

    private String countryName;

    private String countryAlias;

    private String countryID;

    private List<Athlete> athleteList;

    private Map<String, DateSportsModel> datesCountryMapping;

    public List<Athlete> getAthleteList() {
        return athleteList;
    }

    public Map<String, DateSportsModel> getDatesCountryMapping() {
        return datesCountryMapping;
    }

    public String getCountryAlias() {
        return countryAlias;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setAthleteList(List<Athlete> athleteList) {
        this.athleteList = athleteList;
    }

    public void setCountryAlias(String countryAlias) {
        this.countryAlias = countryAlias;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryID() {
        return countryID;
    }

    public void setCountryID(String countryID) {
        this.countryID = countryID;
    }

    public void setDatesCountryMapping(Map<String, DateSportsModel> datesCountryMapping) {
        this.datesCountryMapping = datesCountryMapping;
    }

    public void initializeEmptyDateSportsMapping() {

        Map<String, DateSportsModel> dateSportsModelMap = new HashMap<>();
        long eventDate = DateUtils.OLYMPIC_EVENT_START_DATE;

        while (eventDate < DateUtils.OLYMPIC_EVENT_END_DATE) {
            DateSportsModel dateSportsModel = new DateSportsModel();
            dateSportsModelMap.put(String.valueOf(eventDate), dateSportsModel);
            eventDate += DateUtils.NUM_OF_MILISECONDS_IN_DAY;
        }

        setDatesCountryMapping(dateSportsModelMap);
    }

}
