package com.olympics.olympicsandroid.model;

import com.olympics.olympicsandroid.utility.SportsUtility;

import java.util.List;

/**
 * Created by sarnab.poddar on 7/16/16.
 */
public class SportsUtilityModel
{
    Corelation corelation;

    public Corelation getCorelation() {
        return corelation;
    }

    public class Corelation {

        List<SportRelation> sportRelations;

        public List<SportRelation> getSportRelations() {
            return sportRelations;
        }

        public class SportRelation {
            private String discipline;
            private List<SubDiscipline> subDiscipline;
            private String type;

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
        }
    }
}
