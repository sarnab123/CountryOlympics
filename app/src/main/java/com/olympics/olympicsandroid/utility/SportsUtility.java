package com.olympics.olympicsandroid.utility;

import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.olympics.olympicsandroid.OlympicsApplication;
import com.olympics.olympicsandroid.model.ErrorModel;
import com.olympics.olympicsandroid.model.EventResultCompetitor;
import com.olympics.olympicsandroid.model.ExtendedResult;
import com.olympics.olympicsandroid.model.ScoreModel;
import com.olympics.olympicsandroid.model.SportsUtilityModel;
import com.olympics.olympicsandroid.model.presentationModel.EventResultsViewModel;
import com.olympics.olympicsandroid.model.presentationModel.EventUnitModel;
import com.olympics.olympicsandroid.networkLayer.CustomJSONRequest;
import com.olympics.olympicsandroid.networkLayer.OlympicRequestQueries;
import com.olympics.olympicsandroid.networkLayer.RequestPolicy;
import com.olympics.olympicsandroid.networkLayer.VolleySingleton;
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


    public static final String OUTCOME_WIN = "WIN";
    public static final String OUTCOME_LOSS = "LOSS";
    public static final String OUTCOME_ONGOING = "LIVE";
    public static final String OUTCOME_TIE = "TIE";
    public static final String OUTCOME_SCHEDULED = "Scheduled";

    public static final String SPORT_FOOTBALL = "football";
    public static final String SPORT_TENNIS = "tennis";

    private static SportsUtility instance;

    private static String NOTES_COMPETITOR__DIVIDER = ":";
    private static String NOTES_DIVIDER = ";";

    private SportsUtilityModel sportsUtilityModel;

    public static SportsUtility getInstance() {
        if (instance == null) {
            instance = new SportsUtility();
        }

        return instance;
    }

    private SportsUtility() {

        // Set Request Policy

        RequestPolicy requestPolicy = new RequestPolicy();
        requestPolicy.setForceCache(true);
        requestPolicy.setMaxAge(60 * 60 * 60 * 30);

        CustomJSONRequest<SportsUtilityModel> sportsTypeRequest = new
                CustomJSONRequest<SportsUtilityModel>(OlympicRequestQueries.SPORTS_TYPE,
                SportsUtilityModel.class, null, createSportsTypeSuccessListener(),
                createSportsTypeFailureListener(), requestPolicy);
        VolleySingleton.getInstance(null).addToRequestQueue(sportsTypeRequest);

    }

    protected Response.ErrorListener createSportsTypeFailureListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorModel errorModel = new ErrorModel();
                errorModel.setErrorCode(error.getLocalizedMessage());
                errorModel.setErrorMessage(error.getMessage());
                getStaticSportsData();
            }
        };
    }

    protected Response.Listener<SportsUtilityModel> createSportsTypeSuccessListener() {
        return new Response.Listener<SportsUtilityModel>() {
            @Override
            public void onResponse(SportsUtilityModel responseModel) {
                if (responseModel != null && responseModel instanceof SportsUtilityModel) {
                    sportsUtilityModel = (SportsUtilityModel) responseModel;
                } else {
                    getStaticSportsData();
                }
            }
        };
    }

    private void getStaticSportsData() {

        String configString = UtilityMethods.loadDataFromAsset(OlympicsApplication
                .getAppContext(), "sports_type.json");
        ParseTask parseTask = new ParseTask(SportsUtilityModel.class, configString, new
                IParseListener() {
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


    public int getTypeofSport(String discipline, String unitName) {
        if (!TextUtils.isEmpty(unitName)) {
            unitName = unitName.toLowerCase();
        }
        if (!TextUtils.isEmpty(discipline)) {
            discipline = discipline.toLowerCase();
        } else {
            return TYPE_INDIVUDUAL;
        }
        if (sportsUtilityModel != null) {
            for (SportsUtilityModel.SportRelation sportRelation : sportsUtilityModel.getCorelation()) {
                if (discipline.contains(sportRelation.getDiscipline().toLowerCase())) {
                    if (sportRelation.getSubDiscipline() != null && sportRelation.getSubDiscipline()
                            .size() > 0 && !TextUtils.isEmpty(unitName)) {
                        for (SportsUtilityModel.SportRelation.SubDiscipline subDiscipline :
                                sportRelation.getSubDiscipline()) {
                            if (!TextUtils.isEmpty(subDiscipline.getKeyword()) && unitName.contains
                                    (subDiscipline.getKeyword().toLowerCase())) {
                                return subDiscipline.getType();
                            }
                        }

                    }
                    return sportRelation.getType();
                }
            }
        }

        return TYPE_INDIVUDUAL;
    }

    public boolean getIsDetailedSport(String discipline, String unitName) {
        if (!TextUtils.isEmpty(unitName)) {
            unitName = unitName.toLowerCase();
        }
        if (!TextUtils.isEmpty(discipline)) {
            discipline = discipline.toLowerCase();
        } else {
            return false;
        }

        if (sportsUtilityModel != null) {
            for (SportsUtilityModel.SportRelation sportRelation : sportsUtilityModel.getCorelation()) {
                if (discipline.contains(sportRelation.getDiscipline().toLowerCase())) {
                    return sportRelation.getDetail_score();
                }
            }
        }
        return false;

    }

    public String getUnitStatus(String status) {
        if (status.contains("not")) {
            return EventUnitModel.UNIT_STATUS_NOT_SCHEDULED;

        } else if (status.contains("schedu")) {
            return EventUnitModel.UNIT_STATUS_SCHEDULED;
        } else if (status.contains("progress")) {
            return EventUnitModel.UNIT_STATUS_INPROGRESS;

        } else if (status.contains("close") || status.contains("complete")) {
            return EventUnitModel.UNIT_STATUS_CLOSED;

        }

        return EventUnitModel.UNIT_STATUS_NOT_SCHEDULED;

    }

    public byte getMedalType(String medal) {

        if (TextUtils.isEmpty(medal)) {
            return EventUnitModel.UNIT_MEDAL_NONE;
        } else if (medal.equalsIgnoreCase("gold")) {
            return EventUnitModel.UNIT_MEDAL_GOLD;
        } else if (medal.equalsIgnoreCase("bronze")) {
            return EventUnitModel.UNIT_MEDAL_BRONZE;
        }

        return EventUnitModel.UNIT_MEDAL_NONE;
    }

    public String getCompetitorName(EventResultCompetitor competitor, EventResultsViewModel
            eventResultsViewModel) {

        if (eventResultsViewModel != null && competitor != null) {
            if (eventResultsViewModel.getUnit_type() == TYPE_INDIVUDUAL || eventResultsViewModel
                    .getUnit_type() == TYPE_INDIVUDUAL_HEAD2HEAD) {
                if (!TextUtils.isEmpty(competitor.getPrint_name())) {
                    return competitor.getPrint_name();
                } else {
                    String firstName = TextUtils.isEmpty(competitor.getFirst_name()) ? "" : competitor.getFirst_name();
                    String lastName = TextUtils.isEmpty(competitor.getLast_name()) ? "" : competitor.getLast_name();
                    StringBuilder stringBuilder = new StringBuilder(firstName).append
                            (" ").append(lastName);
                    return stringBuilder.toString();
                }
            } else if (eventResultsViewModel.getUnit_type() == TYPE_TEAM_HEAD2HEAD ||
                    eventResultsViewModel.getUnit_type() == TYPE_INDIVUDUAL_HEAD2HEAD) {
                return competitor.getDescription();
            }
        }
        return null;
    }

    public String getOutcomeData(EventResultCompetitor competitor, EventResultsViewModel
            eventResultsViewModel) {
        if (eventResultsViewModel != null && competitor != null) {
            if (eventResultsViewModel.getUnit_status().equalsIgnoreCase(EventUnitModel
                    .UNIT_STATUS_CLOSED)) {
                if (!TextUtils.isEmpty(competitor.getOutcome())) {
                    if (competitor.getOutcome().equalsIgnoreCase("loss") || competitor.getOutcome
                            ().equalsIgnoreCase("defeat")) {
                        return OUTCOME_LOSS;
                    } else if (competitor.getOutcome().equalsIgnoreCase("win") || competitor
                            .getOutcome().equalsIgnoreCase("victory")) {
                        return OUTCOME_WIN;
                    } else if (competitor.getOutcome().equalsIgnoreCase("tie")) {
                        return OUTCOME_TIE;
                    }
                }

            } else if (eventResultsViewModel.getUnit_status().equalsIgnoreCase(EventUnitModel
                    .UNIT_STATUS_INPROGRESS)) {
                return OUTCOME_ONGOING;
            }
        }
        return OUTCOME_SCHEDULED;
    }

    public String getResult(String disciplineName, EventResultsViewModel eventResultsViewModel,
                            EventResultCompetitor competitor) {

        String unitStatus = eventResultsViewModel.getUnit_status();

        if (unitStatus != null && unitStatus.equalsIgnoreCase(EventUnitModel.UNIT_STATUS_CLOSED)) {
            return competitor.getResult();
        } else if (unitStatus != null && unitStatus.equalsIgnoreCase(EventUnitModel
                .UNIT_STATUS_INPROGRESS)) {
            if (competitor.getResult() != null) {
                return competitor.getResult();
            }

            if (competitor.getExtendedResults() != null) {
                for (ExtendedResult result : competitor.getExtendedResults()) {
                    if (result.getCode() != null && result.getCode().equalsIgnoreCase
                            ("SET_PT_COUNT")) {
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
        if (sportsUtilityModel != null) {
            for (SportsUtilityModel.SportRelation sportRelation : sportsUtilityModel.getCorelation()) {
                if (disciplineName.contains(sportRelation.getDiscipline().toLowerCase())) {
                    {
                        if (sportRelation.getPointTypes() != null) {
                            for (SportsUtilityModel.SportRelation.PointType pointType : sportRelation
                                    .getPointTypes()) {
                                if (!TextUtils.isEmpty(eventResultsViewModel.getUnit_name()) &&
                                        eventResultsViewModel.getUnit_name().toLowerCase().contains
                                                (pointType.getType().toLowerCase())) {
                                    return pointType.getValue();
                                }
                            }
                        }

                        return sportRelation.getScore_type();
                    }

                }

            }
        }

        return "points";
    }

    public StringBuilder getNotes(EventResultCompetitor competitor, EventResultsViewModel eventResultsViewModel, StringBuilder notes) {

        StringBuilder tempNotes = new StringBuilder();

        if (!TextUtils.isEmpty(notes)) {
            tempNotes.append(notes);
        }

        if (eventResultsViewModel != null && !TextUtils.isEmpty(eventResultsViewModel.getUnit_status()) &&
                (eventResultsViewModel.getUnit_status().equalsIgnoreCase(EventUnitModel.UNIT_STATUS_INPROGRESS) ||
                        eventResultsViewModel.getUnit_status().equalsIgnoreCase(EventUnitModel.UNIT_STATUS_CLOSED))) {
            if (!TextUtils.isEmpty(tempNotes.toString())) {
                tempNotes.append(NOTES_COMPETITOR__DIVIDER);
            }

            if (competitor != null && competitor.getScoring() != null && competitor.getScoring().size() > 0) {
                for (ScoreModel score : competitor.getScoring()) {
                    tempNotes.append(score.getScore());
                    tempNotes.append(NOTES_DIVIDER);
                }
            }

        }
        return tempNotes;
    }

    /*
     * Utility method to convert notes
     */
    public static String getDetailedNotes(String notes) {
        StringBuilder tempNotes = new StringBuilder("Scores are ");
        if (!TextUtils.isEmpty(notes)) {
            String[] compScores = notes.split(NOTES_COMPETITOR__DIVIDER, 2);

            if (compScores != null && compScores.length >= 2) {
                String firstComp = compScores[0];
                String secondComp = compScores[1];

                if (!TextUtils.isEmpty(firstComp) && !TextUtils.isEmpty(secondComp)) {
                    String[] firstScores = firstComp.split(NOTES_DIVIDER);
                    String[] secondScores = secondComp.split(NOTES_DIVIDER);

                    if (firstScores != null && secondScores != null && firstScores.length > 0 && secondScores.length == firstScores.length) {
                        for (int i = 0; i < firstScores.length; i++) {
                            tempNotes.append(" " + firstScores[i]);
                            tempNotes.append(NOTES_COMPETITOR__DIVIDER);
                            tempNotes.append(secondScores[i]);
                            tempNotes.append(" ,");
                        }

                        tempNotes = tempNotes.deleteCharAt(tempNotes.length() - 1);
                    }
                }
            }

        }
        return tempNotes.toString();
    }
}
