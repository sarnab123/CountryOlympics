package com.olympics.olympicsandroid.view.activity.eventActivities;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.olympics.olympicsandroid.R;
import com.olympics.olympicsandroid.model.presentationModel.EventResultsViewModel;
import com.olympics.olympicsandroid.utility.SportsUtility;
import com.olympics.olympicsandroid.view.fragment.IItemClickListener;

import java.util.List;

/**
 * Created by sarnab.poddar on 7/16/16.
 */
public class EventListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    List<EventResultsViewModel> resultsModels;
    IItemClickListener itemClickListener;

    public EventListAdapter(IItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }

    public void updateData(List<EventResultsViewModel> resultsModels)
    {
        this.resultsModels = resultsModels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = null;
        switch(viewType)
        {
            case SportsUtility.TYPE_INDIVUDUAL:

                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.event_cardview_ind_rounds, parent, false);


                ListIndividualCardViewHolder listIndividualCard = new ListIndividualCardViewHolder(view);
                return listIndividualCard;

            case SportsUtility.TYPE_TEAM:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.event_cardview_ind_rounds, parent, false);


                ListTeamCardViewHolder listTeamCardViewHolderCard = new ListTeamCardViewHolder(view);
                return listTeamCardViewHolderCard;

            case SportsUtility.TYPE_INDIVUDUAL_HEAD2HEAD:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.event_cardview_ind_rounds, parent, false);


                ListIndH2HCardViewHolder listIndH2HCardViewHolder = new ListIndH2HCardViewHolder(view);
                return listIndH2HCardViewHolder;

            case SportsUtility.TYPE_TEAM_HEAD2HEAD:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.event_cardview_ind_rounds, parent, false);


                ListTeamH2HCardViewHolder listTeamH2HCardViewHolder = new ListTeamH2HCardViewHolder(view);
                return listTeamH2HCardViewHolder;
        }

        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    private class ListIndividualCardViewHolder extends RecyclerView.ViewHolder
    {
        private TextView id_athlete_name;
        private TextView id_number_rounds;
        private TextView id_score;
        private TextView id_rank;
        private TextView id_score_header;

        public ListIndividualCardViewHolder(View itemView) {
            super(itemView);
            id_rank =(TextView) itemView.findViewById(R.id.id_rank);
            id_athlete_name =(TextView) itemView.findViewById(R.id.id_athlete_name);
            id_number_rounds =(TextView) itemView.findViewById(R.id.id_number_rounds);
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

}
