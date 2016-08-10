package com.olympics.olympicsandroid.model.presentationModel;

import android.text.TextUtils;

import com.olympics.olympicsandroid.model.IResponseModel;
import com.olympics.olympicsandroid.networkLayer.cache.database.OlympicsPrefs;

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


    // miantian the event id for notfications
    private String eventID;

    // this is to check whether we need to show extended result
    private boolean isDetailsSport;

    private String start_date;

    public boolean isDetailsSport() {
        return isDetailsSport;
    }

    public void setIsDetailsSport(boolean isDetailsSport) {
        this.isDetailsSport = isDetailsSport;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

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
        if(OlympicsPrefs.getInstance(null).getTimeReset() && !TextUtils.isEmpty(start_date))
        {
            if(start_date.contains("+00:00"))
            {
                start_date = start_date.replace("+00:00","-03:00");
            }
        }
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

    private CompetitorHeadtoHeadViewModel competitorH2HViewModel;

    public CompetitorHeadtoHeadViewModel getCompetitorH2HViewModel() {
        return competitorH2HViewModel;
    }

    public void setCompetitorH2HViewModel(CompetitorHeadtoHeadViewModel competitorH2HViewModel) {
        this.competitorH2HViewModel = competitorH2HViewModel;
    }

    public class CompetitorViewModel
    {
        private String countryAlias;

        private String competitorID;
        private String competitorName;

        private String rank;

        // This is to update the score header , this can be : points/sets/time
        private String unit_scoring_type;

        // win/loss/draw
        private String outcome;

        // if status = closed , then the result from competitor.
        // else if status = in progress, then the result from score object.
        private String result;

        public String getUnit_scoring_type() {
            return unit_scoring_type;
        }

        public void setUnit_scoring_type(String unit_scoring_type) {
            this.unit_scoring_type = unit_scoring_type;
        }

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

    public class CompetitorHeadtoHeadViewModel
    {

        private String unitID;

        private String countryAlias;

        private String competitorID;
        private String competitorName;

        private String rank;

        // win/loss/draw
        private String outcome;

        // if status = closed , then the result from competitor.
        // else if status = in progress, then the result from score object.
        private String result;

        // This is to update the score header , this can be : points/sets/time
        private String unit_scoring_type;

        private String opp_countryAlias;

        private String opp_competitorID;
        private String opp_competitorName;

        private String opp_rank;

        private String notes;

        // win/loss/draw
        private String opp_outcome;

        // if status = closed , then the result from competitor.
        // else if status = in progress, then the result from score object.
        private String opp_result;

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public String getUnitID() {
            return unitID;
        }

        public String getOpp_competitorID() {
            return opp_competitorID;
        }

        public String getOpp_competitorName() {
            return opp_competitorName;
        }

        public String getOpp_countryAlias() {
            return opp_countryAlias;
        }

        public String getOpp_outcome() {
            return opp_outcome;
        }

        public String getOpp_rank() {
            return opp_rank;
        }

        public String getOpp_result() {
            return opp_result;
        }

        public String getUnit_scoring_type() {
            return unit_scoring_type;
        }

        public void setUnit_scoring_type(String unit_scoring_type) {
            this.unit_scoring_type = unit_scoring_type;
        }

        public void setOpp_competitorID(String opp_competitorID) {
            this.opp_competitorID = opp_competitorID;
        }

        public void setOpp_competitorName(String opp_competitorName) {
            this.opp_competitorName = opp_competitorName;
        }

        public void setOpp_countryAlias(String opp_countryAlias) {
            this.opp_countryAlias = opp_countryAlias;
        }

        public void setOpp_outcome(String opp_outcome) {
            this.opp_outcome = opp_outcome;
        }

        public void setOpp_rank(String opp_rank) {
            this.opp_rank = opp_rank;
        }

        public void setOpp_result(String opp_result) {
            this.opp_result = opp_result;
        }

        public void setUnitID(String unitID) {
            this.unitID = unitID;
        }

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
