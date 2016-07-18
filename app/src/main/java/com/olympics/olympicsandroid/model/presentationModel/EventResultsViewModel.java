package com.olympics.olympicsandroid.model.presentationModel;

import com.olympics.olympicsandroid.model.IResponseModel;

import java.util.List;

/**
 * Created by sarnab.poddar on 7/17/16.
 */
public class EventResultsViewModel implements IResponseModel
{

    // Name of the Unit, for ex: Men's Classification 5th-6th Place
    private String unit_name;

    // Id of the Unit , in order to group games from the same unit together
    private String unit_id;

    // What is the type of sport it is - type can be of the type : TYPE_INDIVUDUAL,
    // TYPE_TEAM,TYPE_TEAM_HEAD2HEAD,TYPE_INDIVUDUAL_HEAD2HEAD
    private int unit_type;

    // Gender : Men/Women/Mixed
    private String unit_gender;

    // Whether Gold/Bronze medal match
    private byte unit_medal_type;

    // Status of the unit : UNIT_STATUS_SCHEDULED,UNIT_STATUS_INPROGRESS,
    // UNIT_STATUS_NOT_SCHEDULED,UNIT_STATUS_CLOSED
    private String unit_status;

    // This is to update the score header , this can be : points/sets/time
    private String unit_scoring_type;


    // Maintain the fact whether the unit is for the country user has selected
    private boolean isSelectedCountry;

    private String start_date;

    public boolean isSelectedCountry() {
        return isSelectedCountry;
    }

    public void setIsSelectedCountry(boolean isSelectedCountry) {
        this.isSelectedCountry = isSelectedCountry;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public int getUnit_type() {
        return unit_type;
    }

    public List<CompetitorViewModel> getCompetitorViewModelList() {
        return competitorViewModelList;
    }

    public String getUnit_gender() {
        return unit_gender;
    }

    public String getUnit_id() {
        return unit_id;
    }

    public byte getUnit_medal_type() {
        return unit_medal_type;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public String getUnit_scoring_type() {
        return unit_scoring_type;
    }

    public String getUnit_status() {
        return unit_status;
    }

    public void setCompetitorViewModelList(List<CompetitorViewModel> competitorViewModelList) {
        this.competitorViewModelList = competitorViewModelList;
    }

    public void setUnit_gender(String unit_gender) {
        this.unit_gender = unit_gender;
    }

    public void setUnit_id(String unit_id) {
        this.unit_id = unit_id;
    }

    public void setUnit_medal_type(byte unit_medal_type) {
        this.unit_medal_type = unit_medal_type;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    public void setUnit_scoring_type(String unit_scoring_type) {
        this.unit_scoring_type = unit_scoring_type;
    }

    public void setUnit_status(String unit_status) {
        this.unit_status = unit_status;
    }

    public void setUnit_type(int unit_type) {
        this.unit_type = unit_type;
    }


    private List<CompetitorViewModel> competitorViewModelList;

    public class CompetitorViewModel
    {
        private String countryAlias;

        private String competitorID;
        private String competitorName;

        private String rank;

        // win/loss/draw
        private String outcome;

        // if status = closed , then the result from competitor.
        // else if status = in progress, then the result from score object.
        private String result;

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getCountryAlias() {
            return countryAlias;
        }

        public String getCompetitorID() {
            return competitorID;
        }

        public String getCompetitorName() {
            return competitorName;
        }

        public String getOutcome() {
            return outcome;
        }

        public String getResult() {
            return result;
        }

        public void setCompetitorID(String competitorID) {
            this.competitorID = competitorID;
        }

        public void setCompetitorName(String competitorName) {
            this.competitorName = competitorName;
        }

        public void setCountryAlias(String countryAlias) {
            this.countryAlias = countryAlias;
        }

        public void setOutcome(String outcome) {
            this.outcome = outcome;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }

}
