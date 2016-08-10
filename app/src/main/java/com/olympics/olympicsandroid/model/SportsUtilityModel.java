package com.olympics.olympicsandroid.model;

import com.google.gson.annotations.Expose;
import com.olympics.olympicsandroid.utility.SportsUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sarnab.poddar on 7/16/16.
 */
public class SportsUtilityModel implements IResponseModel
{
    @Expose
    ArrayList<SportRelation> corelation;

    public ArrayList<SportRelation> getCorelation() {
        return corelation;
    }


        public class SportRelation {
            private String discipline;
            private List<SubDiscipline> subDiscipline;
            private List<PointType> scoring_type;
            private String type;
            private String detail_score;
            private String score_type;

            public boolean getDetail_score() {
                return Boolean.parseBoolean(detail_score);
            }

            public String getScore_type() {
                return score_type;
            }

            public List<PointType> getPointTypes() {
                return scoring_type;
            }

            public List<SubDiscipline> getSubDiscipline() {
                return subDiscipline;
            }

            public String getDiscipline() {
                return discipline;
            }

            public int getType()
            {
                if(type == null || type.equalsIgnoreCase("ind"))
                {
                    return SportsUtility.TYPE_INDIVUDUAL;
                }
                else if(type.equalsIgnoreCase("team"))
                {
                    return SportsUtility.TYPE_TEAM;
                }
                else if(type.equalsIgnoreCase("team_head2head"))
                {
                    return SportsUtility.TYPE_TEAM_HEAD2HEAD;
                }
                else if(type.equalsIgnoreCase("ind_head2head"))
                {
                    return SportsUtility.TYPE_INDIVUDUAL_HEAD2HEAD;
                }

                return SportsUtility.TYPE_INDIVUDUAL;
            }

            public class SubDiscipline
            {
                private String keyword;
                private String type;

                public int getType() {
                    if(type == null || type.equalsIgnoreCase("ind"))
                    {
                        return SportsUtility.TYPE_INDIVUDUAL;
                    }
                    else if(type.equalsIgnoreCase("team"))
                    {
                        return SportsUtility.TYPE_TEAM;
                    }
                    else if(type.equalsIgnoreCase("team_head2head"))
                    {
                        return SportsUtility.TYPE_TEAM_HEAD2HEAD;
                    }
                    else if(type.equalsIgnoreCase("ind_head2head"))
                    {
                        return SportsUtility.TYPE_INDIVUDUAL_HEAD2HEAD;
                    }

                    return SportsUtility.TYPE_INDIVUDUAL;
                }

                public String getKeyword() {
                    return keyword;
                }
            }

            public class PointType {
                private String type;
                private String value;

                public String getType() {
                    return type;
                }

                public String getValue() {
                    return value;
                }
            }
        }
}
