package com.olympics.olympicsandroid.view.activity.eventActivities;

import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.olympics.olympicsandroid.OlympicsApplication;
import com.olympics.olympicsandroid.R;
import com.olympics.olympicsandroid.model.presentationModel.EventResultsViewModel;
import com.olympics.olympicsandroid.model.presentationModel.EventUnitModel;
import com.olympics.olympicsandroid.view.fragment.IItemClickListener;

import java.util.List;

/**
 * Created by sarnab.poddar on 7/16/16.
 */
public class EventListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    public final static int TYPE_UNIT_HEADER = 1;
    public final static int TYPE_IND_COMPETITOR = 2;
    public final static int TYPE_IND_HEAD2HEAD_COMPET = 3;
    public final static int TYPE_TEAM_COMPETITOR = 4;
    public final static int TYPE_TEAM_HEAD2HEAD_COMPET = 5;

    List<EventListAdapter.Result> resultsModels;
    IItemClickListener itemClickListener;

    public EventListAdapter(IItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }

    public void updateData(List<EventListAdapter.Result> resultsModels)
    {
        this.resultsModels = resultsModels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = null;
        switch(viewType)
        {

            case TYPE_UNIT_HEADER:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_unit_header, parent, false);

                ListUnitHeaderHolder viewHolder = new ListUnitHeaderHolder(view);

                return viewHolder;

            case TYPE_IND_COMPETITOR:

                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.event_cardview_ind_rounds, parent, false);


                ListIndividualCardViewHolder listIndividualCard = new ListIndividualCardViewHolder(view);
                return listIndividualCard;

            case TYPE_TEAM_COMPETITOR:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.event_cardview_ind_rounds, parent, false);


                ListTeamCardViewHolder listTeamCardViewHolderCard = new ListTeamCardViewHolder(view);
                return listTeamCardViewHolderCard;

            case TYPE_IND_HEAD2HEAD_COMPET:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.event_cardview_ind_rounds, parent, false);


                ListIndH2HCardViewHolder listIndH2HCardViewHolder = new ListIndH2HCardViewHolder(view);
                return listIndH2HCardViewHolder;

            case TYPE_TEAM_HEAD2HEAD_COMPET:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.event_cardview_ind_rounds, parent, false);


                ListTeamH2HCardViewHolder listTeamH2HCardViewHolder = new ListTeamH2HCardViewHolder(view);
                return listTeamH2HCardViewHolder;
        }

        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        switch (resultsModels.get(position).type)
        {
            case TYPE_IND_COMPETITOR:

                ListIndividualCardViewHolder cardViewHolder = (ListIndividualCardViewHolder) holder;
                cardViewHolder.id_athlete_name.setText(resultsModels.get(position).competitorModel.getCompetitorName());
                cardViewHolder.id_score.setText(resultsModels.get(position).competitorModel.getResult());
                cardViewHolder.id_rank.setText(resultsModels.get(position).competitorModel.getRank());
                cardViewHolder.id_score_header.setText(resultsModels.get(position).competitorModel.getUnit_scoring_type());
                break;
            case TYPE_TEAM_COMPETITOR:
                break;
            case TYPE_IND_HEAD2HEAD_COMPET:
                break;
            case TYPE_TEAM_HEAD2HEAD_COMPET:
                break;
            case TYPE_UNIT_HEADER:

                ListUnitHeaderHolder unitHeaderHolder = (ListUnitHeaderHolder) holder;
                unitHeaderHolder.unitName.setText(resultsModels.get(position).sportsTitle.getUnit_name());
                if(resultsModels.get(position).sportsTitle.getUnit_medal_type() != EventUnitModel.UNIT_MEDAL_NONE)
                {
                    unitHeaderHolder.medalImage.setVisibility(View.VISIBLE);
                    unitHeaderHolder.medalImage.setImageDrawable(ResourcesCompat.getDrawable(OlympicsApplication.getAppContext().getResources(), R.drawable.goldmedal, null));
                }
                else{
                    unitHeaderHolder.medalImage.setVisibility(View.GONE);
                }
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        return resultsModels.get(position).type;
    }

    @Override
    public int getItemCount() {
        if(resultsModels == null)
        {
            return 0;
        }
        return resultsModels.size();
    }

    private class ListUnitHeaderHolder extends RecyclerView.ViewHolder
    {

        private TextView unitName;
        private ImageView medalImage;


        public ListUnitHeaderHolder(View itemView) {
            super(itemView);
            unitName = (TextView) itemView.findViewById(R.id.id_unit_title);
            medalImage = (ImageView) itemView.findViewById(R.id.id_medal_image);
        }
    }

    private class ListIndividualCardViewHolder extends RecyclerView.ViewHolder
    {
        private TextView id_athlete_name;
        private TextView id_score;
        private TextView id_rank;
        private TextView id_score_header;

        public ListIndividualCardViewHolder(View itemView) {
            super(itemView);
            id_rank =(TextView) itemView.findViewById(R.id.id_rank);
            id_athlete_name =(TextView) itemView.findViewById(R.id.id_athlete_name);
            id_score =(TextView) itemView.findViewById(R.id.id_score);
            id_score_header = (TextView) itemView.findViewById(R.id.id_score_header);
        }
    }

    private class ListTeamCardViewHolder extends RecyclerView.ViewHolder
    {
        private TextView id_country_alias;
        private ImageView id_country_image;
        private TextView id_number_rounds;
        private TextView id_score;
        private TextView id_rank;
        private TextView id_score_header;

        public ListTeamCardViewHolder(View itemView) {
            super(itemView);
            id_rank =(TextView) itemView.findViewById(R.id.id_rank);
            id_country_image = (ImageView) itemView.findViewById(R.id.id_country_image);
            id_country_alias =(TextView) itemView.findViewById(R.id.id_country_alias);
            id_number_rounds =(TextView) itemView.findViewById(R.id.id_number_rounds);
            id_score =(TextView) itemView.findViewById(R.id.id_score);
            id_score_header = (TextView) itemView.findViewById(R.id.id_score_header);
        }
    }


    private class ListTeamH2HCardViewHolder extends RecyclerView.ViewHolder
    {
        private TextView id_country1_alias;
        private ImageView id_country1_image;
        private TextView id_score;
        private TextView id_score_header;
        private TextView id_outcome;

        private TextView id_country2_alias;
        private ImageView id_country2_image;
        private TextView id_score_1;
        private TextView id_outcome_1;

        public ListTeamH2HCardViewHolder(View itemView) {
            super(itemView);
            id_country1_image = (ImageView) itemView.findViewById(R.id.id_country1_image);
            id_country1_alias =(TextView) itemView.findViewById(R.id.id_country1_alias);
            id_score =(TextView) itemView.findViewById(R.id.id_score);
            id_score_header = (TextView) itemView.findViewById(R.id.id_score_header);
            id_outcome =  (TextView) itemView.findViewById(R.id.id_outcome);

            id_country2_image = (ImageView) itemView.findViewById(R.id.id_country2_image);
            id_country2_alias =(TextView) itemView.findViewById(R.id.id_country2_alias);
            id_score_1 =(TextView) itemView.findViewById(R.id.id_score_1);
            id_outcome_1 =  (TextView) itemView.findViewById(R.id.id_outcome_1);
        }
    }

    private class ListIndH2HCardViewHolder extends RecyclerView.ViewHolder
    {
        private TextView id_athlete_name;
        private TextView id_score;
        private TextView id_outcome;
        private TextView id_score_header;

        private TextView id_athlete_name_1;
        private TextView id_score_1;
        private TextView id_outcome_1;

        public ListIndH2HCardViewHolder(View itemView) {
            super(itemView);
            id_outcome =(TextView) itemView.findViewById(R.id.id_outcome);
            id_athlete_name =(TextView) itemView.findViewById(R.id.id_athlete_name);
            id_score =(TextView) itemView.findViewById(R.id.id_score);
            id_score_header = (TextView) itemView.findViewById(R.id.id_score_header);

            id_outcome_1 =(TextView) itemView.findViewById(R.id.id_outcome);
            id_athlete_name_1 =(TextView) itemView.findViewById(R.id.id_athlete_name_1);
            id_score_1 =(TextView) itemView.findViewById(R.id.id_score_1);
        }
    }

    public static class Result {
        private EventResultsViewModel.CompetitorViewModel competitorModel;
        public int type;
        private EventResultsViewModel.CompetitorHeadtoHeadViewModel competitorheadModel;
        private EventResultsViewModel sportsTitle;

        public Result() {
        }

        public Result(int type, EventResultsViewModel.CompetitorHeadtoHeadViewModel competitorheadModel) {
            this.type = type;
            this.competitorheadModel = competitorheadModel;
        }

        public Result(int type, EventResultsViewModel sportsTitle) {
            this.type = type;
            this.sportsTitle = sportsTitle;
        }

        public Result(int type, EventResultsViewModel.CompetitorViewModel competitorModel) {
            this.type = type;
            this.competitorModel = competitorModel;
        }
    }

}
