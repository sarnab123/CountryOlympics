package com.olympics.olympicsandroid.view.activity.eventActivities;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.olympics.olympicsandroid.OlympicsApplication;
import com.olympics.olympicsandroid.R;
import com.olympics.olympicsandroid.model.presentationModel.EventReminder;
import com.olympics.olympicsandroid.model.presentationModel.EventResultsViewModel;
import com.olympics.olympicsandroid.model.presentationModel.EventUnitModel;
import com.olympics.olympicsandroid.networkLayer.cache.database.DBCompetitorRelationHelper;
import com.olympics.olympicsandroid.utility.DateUtils;
import com.olympics.olympicsandroid.utility.LocalNotifications;
import com.olympics.olympicsandroid.utility.UtilityMethods;
import com.olympics.olympicsandroid.view.fragment.IScheduleListener;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by sarnab.poddar on 7/16/16.
 */
public class EventListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final static int TYPE_UNIT_HEADER = 1;
    public final static int TYPE_IND_COMPETITOR = 2;
    public final static int TYPE_IND_HEAD2HEAD_COMPET = 3;
    public final static int TYPE_TEAM_COMPETITOR = 4;
    public final static int TYPE_TEAM_HEAD2HEAD_COMPET = 5;

    // Allows to remember the last item shown on screen
    private int lastPosition = -1;
    private Activity activity;

    List<EventListAdapter.Result> resultsModels;
    public final IScheduleListener itemClickListener;

    public EventListAdapter(Activity activity, IScheduleListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        this.activity = activity;
    }

    public void updateData(List<EventListAdapter.Result> resultsModels) {
        this.resultsModels = resultsModels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {


            case TYPE_UNIT_HEADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .event_details_header, parent, false);

                ListUnitHeaderHolder viewHolder = new ListUnitHeaderHolder(view);

                return viewHolder;

            case TYPE_IND_COMPETITOR:

                view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .event_cardview_ind_rounds, parent, false);


                ListIndividualCardViewHolder listIndividualCard = new
                        ListIndividualCardViewHolder(view);
                return listIndividualCard;

            case TYPE_TEAM_COMPETITOR:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .event_cardview_team_rounds, parent, false);


                ListTeamCardViewHolder listTeamCardViewHolderCard = new ListTeamCardViewHolder
                        (view);
                return listTeamCardViewHolderCard;

            case TYPE_IND_HEAD2HEAD_COMPET:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .event_cardview_ind_head, parent, false);


                ListIndH2HCardViewHolder listIndH2HCardViewHolder = new ListIndH2HCardViewHolder
                        (view);
                return listIndH2HCardViewHolder;

            case TYPE_TEAM_HEAD2HEAD_COMPET:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .event_cardview_team_head, parent, false);


                ListTeamH2HCardViewHolder listTeamH2HCardViewHolder = new
                        ListTeamH2HCardViewHolder(view);
                return listTeamH2HCardViewHolder;
        }

        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        switch (resultsModels.get(position).type) {
            case TYPE_IND_COMPETITOR:

                ListIndividualCardViewHolder cardViewHolder = (ListIndividualCardViewHolder) holder;
                String competitorName = resultsModels.get(position).competitorModel
                        .getCompetitorName();
                if (TextUtils.isEmpty(competitorName)) {
                    DBCompetitorRelationHelper dbCompetitorRelationHelper = new
                            DBCompetitorRelationHelper();
                    if (dbCompetitorRelationHelper.getCompetitorByID(resultsModels.get(position)
                            .competitorModel.getCompetitorID()) != null) {
                        competitorName = dbCompetitorRelationHelper.getCompetitorByID
                                (resultsModels.get(position).competitorModel.getCompetitorID())
                                .getCompetitorName();
                    }
                }
                cardViewHolder.id_athlete_name.setText(competitorName + "(" + resultsModels.get
                        (position).competitorModel.getCountryAlias() + ")");
                cardViewHolder.id_score.setText(resultsModels.get(position).competitorModel
                        .getResult());
                cardViewHolder.id_rank.setText(resultsModels.get(position).competitorModel
                        .getRank());
                cardViewHolder.id_score_header.setText(resultsModels.get(position)
                        .competitorModel.getUnit_scoring_type());
                setAnimation(cardViewHolder.id_indRound_cardview, position);
                break;
            case TYPE_TEAM_COMPETITOR:

                ListTeamCardViewHolder teamCardViewHolder = (ListTeamCardViewHolder) holder;

                if (TextUtils.isEmpty(resultsModels.get(position).competitorModel
                        .getCompetitorName())) {
                    teamCardViewHolder.id_country_alias.setText(resultsModels.get(position)
                            .competitorModel.getCountryAlias());

                } else {
                    teamCardViewHolder.id_country_alias.setText(resultsModels.get(position)
                            .competitorModel.getCompetitorName());
                }
                int rid = OlympicsApplication.getAppContext().getResources().getIdentifier
                        (resultsModels.get(position).competitorModel.getCountryAlias()
                                .toLowerCase(), "raw", OlympicsApplication.getAppContext()
                                .getPackageName());
                try {
                    teamCardViewHolder.id_country_image.setImageBitmap(BitmapFactory.decodeStream
                            (OlympicsApplication.getAppContext().getResources().openRawResource
                                    (rid)));
                } catch (Exception ex) {
                    teamCardViewHolder.id_country_image.setVisibility(View.GONE);
                    System.out.println("Exeptipn == " + ex);
                }
                teamCardViewHolder.id_score.setText(resultsModels.get(position).competitorModel
                        .getResult());
                teamCardViewHolder.id_rank.setText(resultsModels.get(position).competitorModel
                        .getRank());
                teamCardViewHolder.id_score_header.setText(resultsModels.get(position)
                        .competitorModel.getUnit_scoring_type());
                setAnimation(teamCardViewHolder.id_teamRound_cardview, position);
                break;
            case TYPE_IND_HEAD2HEAD_COMPET:

                ListIndH2HCardViewHolder indh2hHolder = (ListIndH2HCardViewHolder) holder;
                competitorName = resultsModels.get(position).competitorheadModel
                        .getCompetitorName();
                if (competitorName == null || competitorName != null && competitorName.trim()
                        .length() == 0) {
                    DBCompetitorRelationHelper dbCompetitorRelationHelper = new
                            DBCompetitorRelationHelper();
                    competitorName = dbCompetitorRelationHelper.getCompetitorByID(resultsModels
                            .get(position).competitorheadModel.getCompetitorID())
                            .getCompetitorName();
                }
                indh2hHolder.id_athlete_name.setText(competitorName + "(" + resultsModels.get
                        (position).competitorheadModel.getCountryAlias() + ")");
                indh2hHolder.id_athlete_name_1.setText(resultsModels.get(position)
                        .competitorheadModel.getOpp_competitorName() + "(" + resultsModels.get
                        (position).competitorheadModel.getOpp_countryAlias() + ")");
                indh2hHolder.id_outcome.setText(resultsModels.get(position).competitorheadModel
                        .getOutcome());
                indh2hHolder.id_outcome_1.setText(resultsModels.get(position).competitorheadModel
                        .getOpp_outcome());
                indh2hHolder.id_score.setText(resultsModels.get(position).competitorheadModel
                        .getResult());
                indh2hHolder.id_score_1.setText(resultsModels.get(position).competitorheadModel
                        .getOpp_result());
                indh2hHolder.id_score_header.setText(resultsModels.get(position)
                        .competitorheadModel.getUnit_scoring_type());
                setAnimation(indh2hHolder.id_indh2h_cardview, position);
                break;
            case TYPE_TEAM_HEAD2HEAD_COMPET:

                ListTeamH2HCardViewHolder teamh2hviewHolder = (ListTeamH2HCardViewHolder) holder;
                setAnimation(teamh2hviewHolder.id_teamh2h_cardview, position);
                if (TextUtils.isEmpty(resultsModels.get(position).competitorheadModel
                        .getCompetitorName())) {
                    teamh2hviewHolder.id_country1_alias.setText(resultsModels.get(position)
                            .competitorheadModel.getCountryAlias());
                    teamh2hviewHolder.id_country2_alias.setText(resultsModels.get(position)
                            .competitorheadModel.getOpp_countryAlias());
                } else {
                    teamh2hviewHolder.id_country1_alias.setText(resultsModels.get(position)
                            .competitorheadModel.getCompetitorName());
                    teamh2hviewHolder.id_country2_alias.setText(resultsModels.get(position)
                            .competitorheadModel.getOpp_competitorName());
                }

                if (!TextUtils.isEmpty(resultsModels.get(position).competitorheadModel
                        .getCountryAlias())) {
                    int rid_1 = OlympicsApplication.getAppContext().getResources().getIdentifier
                            (resultsModels.get(position).competitorheadModel.getCountryAlias()
                                    .toLowerCase(), "raw", OlympicsApplication.getAppContext()
                                    .getPackageName());
                    try {
                        teamh2hviewHolder.id_country1_image.setImageBitmap(BitmapFactory
                                .decodeStream(OlympicsApplication.getAppContext().getResources()
                                        .openRawResource(rid_1)));
                    } catch (Exception ex) {
                        teamh2hviewHolder.id_country1_image.setVisibility(View.GONE);
                        System.out.println("Exeptipn == " + ex);
                    }
                }

                if (!TextUtils.isEmpty(resultsModels.get(position).competitorheadModel
                        .getOpp_countryAlias())) {
                    int rid_2 = OlympicsApplication.getAppContext().getResources().getIdentifier
                            (resultsModels.get(position).competitorheadModel.getOpp_countryAlias
                                    ().toLowerCase(), "raw", OlympicsApplication.getAppContext()
                                    .getPackageName());
                    try {
                        teamh2hviewHolder.id_country2_image.setImageBitmap(BitmapFactory
                                .decodeStream(OlympicsApplication.getAppContext().getResources()
                                        .openRawResource(rid_2)));
                    } catch (Exception ex) {
                        teamh2hviewHolder.id_country2_image.setVisibility(View.GONE);
                        System.out.println("Exeptipn == " + ex);
                    }
                }
                teamh2hviewHolder.id_outcome.setText(resultsModels.get(position)
                        .competitorheadModel.getOutcome());
                teamh2hviewHolder.id_outcome_1.setText(resultsModels.get(position)
                        .competitorheadModel.getOpp_outcome());

                if(!TextUtils.isEmpty(resultsModels.get(position)
                        .competitorheadModel.getNotes()))
                {
                    teamh2hviewHolder.id_match_notes.setVisibility(View.VISIBLE);
                    teamh2hviewHolder.id_match_notes.setText(resultsModels.get(position)
                            .competitorheadModel.getNotes());

                }
                else{
                    teamh2hviewHolder.id_match_notes.setVisibility(View.GONE);
                }

                teamh2hviewHolder.id_score.setText(resultsModels.get(position)
                        .competitorheadModel.getResult());
                teamh2hviewHolder.id_score_1.setText(resultsModels.get(position)
                        .competitorheadModel.getOpp_result());
                teamh2hviewHolder.id_score_header.setText(resultsModels.get(position)
                        .competitorheadModel.getUnit_scoring_type());

                break;
            case TYPE_UNIT_HEADER:

                final ListUnitHeaderHolder unitHeaderHolder = (ListUnitHeaderHolder) holder;
                byte spbyte[] = new byte[0];
                try {

                    spbyte = resultsModels.get(position).sportsTitle.getUnit_name().getBytes
                            ("ISO-8859-1");
                    String str = new String(spbyte);
                    unitHeaderHolder.unitName.setText(str);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if (resultsModels.get(position).sportsTitle.getUnit_medal_type() !=
                        EventUnitModel.UNIT_MEDAL_NONE) {
                    unitHeaderHolder.medalImage.setVisibility(View.VISIBLE);
                    if (resultsModels.get(position).sportsTitle.getUnit_medal_type() ==
                            EventUnitModel.UNIT_MEDAL_GOLD) {
                        unitHeaderHolder.medalImage.setImageDrawable(ResourcesCompat.getDrawable
                                (OlympicsApplication.getAppContext().getResources(), R.drawable
                                        .gold_event, null));
                    } else if (resultsModels.get(position).sportsTitle.getUnit_medal_type() ==
                            EventUnitModel.UNIT_MEDAL_BRONZE) {
                        unitHeaderHolder.medalImage.setImageDrawable(ResourcesCompat.getDrawable
                                (OlympicsApplication.getAppContext().getResources(), R.drawable
                                        .bronzemedal, null));
                    }
                } else {
                    unitHeaderHolder.medalImage.setVisibility(View.GONE);
                }

                if (resultsModels.get(position).sportsTitle.getUnit_status() == EventUnitModel
                        .UNIT_STATUS_SCHEDULED) {
                    unitHeaderHolder.scheduleImage.setImageDrawable(ResourcesCompat.getDrawable
                            (OlympicsApplication.getAppContext().getResources(), R.drawable
                                    .schedule_small, null));
                    unitHeaderHolder.scheduleImage.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(resultsModels.get(position).sportsTitle.getStart_date
                            ())) {

                        final EventResultsViewModel eventResultsViewModel = resultsModels.get
                                (position).sportsTitle;
                        unitHeaderHolder.schduledTime.setVisibility(View.VISIBLE);
                        unitHeaderHolder.schduledTime.setText(DateUtils.getUnitDateWithTime
                                (eventResultsViewModel.getStart_date()));
                        unitHeaderHolder.scheduleImage.setOnClickListener(new View
                                .OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new LocalNotifications().createLocalNotification
                                        (OlympicsApplication.getAppContext(), new EventReminder
                                                (eventResultsViewModel.getEventID(),
                                                        eventResultsViewModel.getUnit_name(),
                                                        eventResultsViewModel.getStart_date(),
                                                        eventResultsViewModel.getUnit_id(), activity
                                                        .getIntent().getStringExtra
                                                                (UtilityMethods
                                                                        .EXTRA_DESCIPLINE_NAME)));
                            }
                        });
                    } else {
                        unitHeaderHolder.schduledTime.setVisibility(View.GONE);
                    }


                } else if (resultsModels.get(position).sportsTitle.getUnit_status() ==
                        EventUnitModel.UNIT_STATUS_INPROGRESS) {
                    unitHeaderHolder.scheduleImage.setImageDrawable(ResourcesCompat.getDrawable
                            (OlympicsApplication.getAppContext().getResources(), R.drawable
                                    .live_icon, null));
                    unitHeaderHolder.scheduleImage.setVisibility(View.VISIBLE);
                    unitHeaderHolder.schduledTime.setVisibility(View.GONE);
                    unitHeaderHolder.scheduleImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(OlympicsApplication.getAppContext(), "This is live " +
                                    "event, Pull to Refresh!", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    unitHeaderHolder.scheduleImage.setVisibility(View.GONE);
                    unitHeaderHolder.schduledTime.setVisibility(View.GONE);
                }
                break;
        }
    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(OlympicsApplication.getAppContext
                    (), android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return resultsModels.get(position).type;
    }

    @Override
    public int getItemCount() {
        if (resultsModels == null) {
            return 0;
        }
        return resultsModels.size();
    }

    private class ListUnitHeaderHolder extends RecyclerView.ViewHolder {

        private TextView unitName;
        private ImageView medalImage;
        private ImageView scheduleImage;
        private TextView schduledTime;

        public ListUnitHeaderHolder(View itemView) {
            super(itemView);
            unitName = (TextView) itemView.findViewById(R.id.id_unit_title);
            medalImage = (ImageView) itemView.findViewById(R.id.id_medal_image);
            scheduleImage = (ImageView) itemView.findViewById(R.id.id_schedule_image);
            schduledTime = (TextView) itemView.findViewById(R.id.id_unit_scheduled_time);
        }
    }

    private class ListIndividualCardViewHolder extends RecyclerView.ViewHolder {
        private TextView id_athlete_name;
        private TextView id_score;
        private TextView id_rank;
        private TextView id_score_header;
        private CardView id_indRound_cardview;

        public ListIndividualCardViewHolder(View itemView) {
            super(itemView);
            id_rank = (TextView) itemView.findViewById(R.id.id_rank);
            id_athlete_name = (TextView) itemView.findViewById(R.id.id_athlete_name);
            id_score = (TextView) itemView.findViewById(R.id.id_score);
            id_score_header = (TextView) itemView.findViewById(R.id.id_score_header);
            id_indRound_cardview = (CardView) itemView.findViewById(R.id.id_indRound_cardview);
        }
    }

    private class ListTeamCardViewHolder extends RecyclerView.ViewHolder {
        private TextView id_country_alias;
        private ImageView id_country_image;
        private TextView id_score;
        private TextView id_rank;
        private TextView id_score_header;
        private CardView id_teamRound_cardview;


        public ListTeamCardViewHolder(View itemView) {
            super(itemView);
            id_rank = (TextView) itemView.findViewById(R.id.id_rank);
            id_country_image = (ImageView) itemView.findViewById(R.id.id_country_image);
            id_country_alias = (TextView) itemView.findViewById(R.id.id_country_alias);
            id_score = (TextView) itemView.findViewById(R.id.id_score);
            id_score_header = (TextView) itemView.findViewById(R.id.id_score_header);
            id_teamRound_cardview = (CardView) itemView.findViewById(R.id.id_teamRound_cardview);
        }
    }


    private class ListTeamH2HCardViewHolder extends RecyclerView.ViewHolder {
        private TextView id_country1_alias;
        private ImageView id_country1_image;
        private TextView id_score;
        private TextView id_score_header;
        private TextView id_outcome;

        private CardView id_teamh2h_cardview;

        private TextView id_match_notes;

        private TextView id_country2_alias;
        private ImageView id_country2_image;
        private TextView id_score_1;
        private TextView id_outcome_1;

        public ListTeamH2HCardViewHolder(View itemView) {
            super(itemView);
            id_country1_image = (ImageView) itemView.findViewById(R.id.id_country1_image);
            id_country1_alias = (TextView) itemView.findViewById(R.id.id_country1_alias);
            id_score = (TextView) itemView.findViewById(R.id.id_score);
            id_score_header = (TextView) itemView.findViewById(R.id.id_score_header);
            id_outcome = (TextView) itemView.findViewById(R.id.id_outcome);

            id_match_notes = (TextView) itemView.findViewById(R.id.id_match_notes);

            id_country2_image = (ImageView) itemView.findViewById(R.id.id_country2_image);
            id_country2_alias = (TextView) itemView.findViewById(R.id.id_country2_alias);
            id_score_1 = (TextView) itemView.findViewById(R.id.id_score_1);
            id_outcome_1 = (TextView) itemView.findViewById(R.id.id_outcome_1);
            id_teamh2h_cardview = (CardView) itemView.findViewById(R.id.id_teamh2h_cardview);
        }
    }

    private class ListIndH2HCardViewHolder extends RecyclerView.ViewHolder {
        private TextView id_athlete_name;
        private TextView id_score;
        private TextView id_outcome;
        private TextView id_score_header;

        private TextView id_athlete_name_1;
        private TextView id_score_1;
        private TextView id_outcome_1;

        private CardView id_indh2h_cardview;

        public ListIndH2HCardViewHolder(View itemView) {
            super(itemView);
            id_outcome = (TextView) itemView.findViewById(R.id.id_outcome);
            id_athlete_name = (TextView) itemView.findViewById(R.id.id_athlete_name);
            id_score = (TextView) itemView.findViewById(R.id.id_score);
            id_score_header = (TextView) itemView.findViewById(R.id.id_score_header);

            id_outcome_1 = (TextView) itemView.findViewById(R.id.id_outcome_1);
            id_athlete_name_1 = (TextView) itemView.findViewById(R.id.id_athlete_name_1);
            id_score_1 = (TextView) itemView.findViewById(R.id.id_score_1);

            id_indh2h_cardview = (CardView) itemView.findViewById(R.id.id_indh2h_cardview);
        }
    }

    public static class Result {
        private EventResultsViewModel.CompetitorViewModel competitorModel;
        public int type;
        private EventResultsViewModel.CompetitorHeadtoHeadViewModel competitorheadModel;
        private EventResultsViewModel sportsTitle;

        public Result() {
        }

        public Result(int type, EventResultsViewModel.CompetitorHeadtoHeadViewModel
                competitorheadModel) {
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

//    private void createCalendarInvite(long startTime,long endTime, String eventTitle)
//    {
//        Calendar cal = Calendar.getInstance();
//        Intent intent = new Intent(Intent.ACTION_EDIT);
//        intent.setType("vnd.android.cursor.item/event");
//        intent.putExtra("beginTime", startTime);
//        intent.putExtra("endTime", endTime);
//        intent.putExtra("title", eventTitle);
//        startActivity(intent);
//    }

}
