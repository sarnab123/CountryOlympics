package com.olympics.olympicsandroid.utility;

import android.text.TextUtils;

import com.olympics.olympicsandroid.OlympicsApplication;
import com.olympics.olympicsandroid.model.ErrorModel;
import com.olympics.olympicsandroid.model.EventResultCompetitor;
import com.olympics.olympicsandroid.model.ExtendedResult;
import com.olympics.olympicsandroid.model.SportsUtilityModel;
import com.olympics.olympicsandroid.model.presentationModel.EventResultsViewModel;
import com.olympics.olympicsandroid.model.presentationModel.EventUnitModel;
import com.olympics.olympicsandroid.networkLayer.parse.IParseListener;
import com.olympics.olympicsandroid.networkLayer.parse.ParseTask;

/**
 * Created by sarnab.poddar on 7/14/16.
 */
public class SportsUtility {

    public static final int TYPE_NOT_SET = 0;
    public static final int TYPE_INDIVUDUAL = 1;
    public static final int TYPE_TEAM = 2;
    public static final int TYPE_TEAM_HEAD2HEAD = 3;
    public static final int TYPE_INDIVUDUAL_HEAD2HEAD = 4;
    public static final int TYPE_MIXED = 5;


    public static final String OUTCOME_WIN = "outcome_win";
    public static final String OUTCOME_LOSS = "outcome_loss";
    public static final String OUTCOME_ONGOING = "outcome_ongoing";
    public static final String OUTCOME_TIE = "outcome_tie";

    public static final String SPORT_FOOTBALL = "football";
    public static final String SPORT_TENNIS = "tennis";

    private static SportsUtility instance;

    private SportsUtilityModel sportsUtilityModel;

    public static SportsUtility getInstance() {
        if (instance == null) {
            instance = new SportsUtility();
        }

        return instance;
    }

    private SportsUtility() {
        String configString =
                UtilityMethods.loadDataFromAsset(OlympicsApplication.getAppContext(),
                        "sports_type.json");
        ParseTask parseTask = new ParseTask(SportsUtilityModel.class, configString, new IParseListener() {
            @Override
            public void onParseSuccess(Object responseModel) {
                sportsUtilityModel = (SportsUtilityModel) responseModel;
            }

            @Override
            public void onParseFailure(ErrorModel errorModel) {
            }
        }, ParseTask.JSON_DATA);
        parseTask.startParsing();
    }


    public int getTypeofSport( String discipline, String unitName) {
        if(!TextUtils.isEmpty(unitName))
        {
            unitName = unitName.toLowerCase();
        }
        if(!TextUtils.isEmpty(discipline))
        {
            discipline = discipline.toLowerCase();
        }
        else{
            return TYPE_INDIVUDUAL;
        }
        for (SportsUtilityModel.SportRelation sportRelation : sportsUtilityModel.getCorelation()) {
            if (discipline.contains(sportRelation.getDiscipline().toLowerCase())) {
                if (sportRelation.getSubDiscipline() != null && sportRelation.getSubDiscipline().size() > 0 && !TextUtils.isEmpty(unitName))
                {
                    for(SportsUtilityModel.SportRelation.SubDiscipline subDiscipline: sportRelation.getSubDiscipline())
                    {
                        if(!TextUtils.isEmpty(subDiscipline.getKeyword()) && unitName.contains(subDiscipline.getKeyword().toLowerCase()))
                        {
                            return subDiscipline.getType();
                        }
                    }

                }
                return sportRelation.getType();
            }
        }

        return TYPE_INDIVUDUAL;
    }

    public String getUnitStatus(String status)
    {
        if(status.contains("not")) {
            return EventUnitModel.UNIT_STATUS_NOT_SCHEDULED;

        } else
        if(status.contains("schedu"))
        {
            return EventUnitModel.UNIT_STATUS_SCHEDULED;
        }
        else if(status.contains("progress"))
        {
            return EventUnitModel.UNIT_STATUS_INPROGRESS;

        }
        else if(status.contains("close") || status.contains("complete"))
        {
            return EventUnitModel.UNIT_STATUS_CLOSED;

        }

        return EventUnitModel.UNIT_STATUS_NOT_SCHEDULED;

    }

    public byte getMedalType(String medal)
    {

        if(TextUtils.isEmpty(medal))
        {
            return EventUnitModel.UNIT_MEDAL_NONE;
        }
        else if (medal.equalsIgnoreCase("gold"))
        {
            return EventUnitModel.UNIT_MEDAL_GOLD;
        }
        else if (medal.equalsIgnoreCase("bronze"))
        {
            return EventUnitModel.UNIT_MEDAL_BRONZE;
        }

        return EventUnitModel.UNIT_MEDAL_NONE;
    }

    public String getCompetitorName(EventResultCompetitor competitor, EventResultsViewModel eventResultsViewModel) {

        if(eventResultsViewModel != null && competitor != null)
        {
            if(eventResultsViewModel.getUnit_type() == TYPE_INDIVUDUAL || eventResultsViewModel.getUnit_type() == TYPE_INDIVUDUAL_HEAD2HEAD)
            {
                return competitor.getFirst_name() + " " + competitor.getLast_name();
            }
            else if (eventResultsViewModel.getUnit_type() == TYPE_TEAM_HEAD2HEAD || eventResultsViewModel.getUnit_type() == TYPE_INDIVUDUAL_HEAD2HEAD)
            {
                return competitor.getOrganization();
            }
        }
        return null;
    }

    public String getOutcomeData(EventResultCompetitor competitor,EventResultsViewModel eventResultsViewModel)
    {
        if(eventResultsViewModel != null && competitor != null)
        {
            if(eventResultsViewModel.getUnit_status().equalsIgnoreCase(EventUnitModel.UNIT_STATUS_CLOSED))
            {
                if(!TextUtils.isEmpty(competitor.getOutcome()))
                {
                    if(competitor.getOutcome().equalsIgnoreCase("loss") || competitor.getOutcome().equalsIgnoreCase("defeat"))
                    {
                        return OUTCOME_LOSS;
                    }
                    else if(competitor.getOutcome().equalsIgnoreCase("win") || competitor.getOutcome().equalsIgnoreCase("victory"))
                    {
                        return OUTCOME_WIN;
                    }
                    else if(competitor.getOutcome().equalsIgnoreCase("tie"))
                    {
                        return OUTCOME_TIE;
                    }
                }

            }
            else if (eventResultsViewModel.getUnit_status().equalsIgnoreCase(EventUnitModel.UNIT_STATUS_INPROGRESS))
            {
                return OUTCOME_ONGOING;
            }
        }
        return OUTCOME_ONGOING;
    }

    public String getResult(String disciplineName, EventResultsViewModel eventResultsViewModel, EventResultCompetitor competitor)
    {

        String unitStatus = eventResultsViewModel.getUnit_status();

        if(unitStatus != null && unitStatus.equalsIgnoreCase(EventUnitModel.UNIT_STATUS_CLOSED))
        {
            return competitor.getResult();
        }
        else if(unitStatus != null && unitStatus.equalsIgnoreCase(EventUnitModel.UNIT_STATUS_INPROGRESS))
        {
            if (competitor.getResult() != null)
            {
                return competitor.getResult();
            }

            if(competitor.getExtendedResults() != null) {
                for (ExtendedResult result : competitor.getExtendedResults()) {
                    if (result.getCode() != null && result.getCode().equalsIgnoreCase("SET_PT_COUNT")) {
                        return result.getValue();
                    }

                    if (result.getCode() != null && result.getCode().equalsIgnoreCase("PTS")) {
                        return result.getValue();
                    }
                }
            }


        }

        return "-";
    }

    public String getPointType(String disciplineName, EventResultsViewModel eventResultsViewModel) {
        if (!TextUtils.isEmpty(disciplineName)) {
            disciplineName = disciplineName.toLowerCase();
        } else {
            return "points";
        }
        for (SportsUtilityModel.SportRelation sportRelation : sportsUtilityModel.getCorelation()) {
            if (disciplineName.contains(sportRelation.getDiscipline().toLowerCase())) {
                {
                    if (sportRelation.getPointTypes() != null) {
                        for (SportsUtilityModel.SportRelation.PointType pointType : sportRelation.getPointTypes()) {
                            if (eventResultsViewModel.getUnit_name().contains(pointType.getType())) {
                                return pointType.getValue();
                            }
                        }
                    }

                    return sportRelation.getScore_type();
                }

            }

        }

        return "points";
    }
}
