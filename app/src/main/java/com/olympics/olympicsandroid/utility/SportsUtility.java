package com.olympics.olympicsandroid.utility;

import com.olympics.olympicsandroid.OlympicsApplication;
import com.olympics.olympicsandroid.model.ErrorModel;
import com.olympics.olympicsandroid.model.SportsUtilityModel;
import com.olympics.olympicsandroid.networkLayer.parse.IParseListener;
import com.olympics.olympicsandroid.networkLayer.parse.ParseTask;

/**
 * Created by sarnab.poddar on 7/14/16.
 */
public class SportsUtility {

    public static final int TYPE_INDIVUDUAL = 1;
    public static final int TYPE_TEAM = 2;
    public static final int TYPE_TEAM_HEAD2HEAD = 3;
    public static final int TYPE_INDIVUDUAL_HEAD2HEAD = 4;

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


    public int getTypeofSport(final String discipline, final String unitName) {
        for (SportsUtilityModel.Corelation.SportRelation sportRelation : sportsUtilityModel.getCorelation().getSportRelations()) {
            if(discipline.equalsIgnoreCase(sportRelation.getDiscipline())) {
                if (sportRelation.getSubDiscipline() != null && sportRelation.getSubDiscipline().size() > 0)
                {
                    for(SportsUtilityModel.Corelation.SportRelation.SubDiscipline subDiscipline: sportRelation.getSubDiscipline())
                    {
                        if(unitName.contains(subDiscipline.getKeyword()))
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

}
