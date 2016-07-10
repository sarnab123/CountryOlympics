package com.olympics.olympicsandroid.model.presentationModel;

import java.util.List;

/**
 * Created by sarnab.poddar on 7/10/16.
 */
public class CountryEventUnitModel {

    private String countryName;

    private String countryAlias;

    private String countryID;

    private List<AthleteModel> athleteModelList;

    private List<DateSportsModel> datesCountryMapping;

    public List<AthleteModel> getAthleteModelList() {
        return athleteModelList;
    }

    public List<DateSportsModel> getDatesCountryMapping() {
        return datesCountryMapping;
    }

    public String getCountryAlias() {
        return countryAlias;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setAthleteModelList(List<AthleteModel> athleteModelList) {
        this.athleteModelList = athleteModelList;
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

    public void setDatesCountryMapping(List<DateSportsModel> datesCountryMapping) {
        this.datesCountryMapping = datesCountryMapping;
    }

}
