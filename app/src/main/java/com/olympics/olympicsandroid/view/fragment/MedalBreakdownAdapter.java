package com.olympics.olympicsandroid.view.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.olympics.olympicsandroid.R;
import com.olympics.olympicsandroid.model.presentationModel.MedalTallyByOrgModel;

import java.util.List;

/**
 * Created by tkmagz4 on 8/4/16.
 */

public class MedalBreakdownAdapter extends RecyclerView.Adapter<MedalBreakdownAdapter.ViewHolder> {

    public static final int TYPE_PLAYER_NAME = 1;
    public static final int TYPE_MEDAL_DATA = 2;
    private List<MedalBreakdownData> medalBreakdownData;

    public void setMedalBreakdown(List<MedalBreakdownData> result) {
        this.medalBreakdownData = result;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView medalName;
        private TextView athleteName;
        private TextView sportsName;

        public ViewHolder(View itemView, int type) {
            super(itemView);

            if (type == TYPE_PLAYER_NAME) {
                athleteName = (TextView) itemView.findViewById(R.id.id_athlete_name);
            } else if (type == TYPE_MEDAL_DATA) {
                medalName = (TextView) itemView.findViewById(R.id.id_medal_name);
                sportsName = (TextView) itemView.findViewById(R.id.id_sports_name);
            }
        }
    }

    @Override
    public MedalBreakdownAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        ViewHolder holder = null;
        switch (viewType) {
            case TYPE_PLAYER_NAME:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .player_name_view, parent, false);
                break;
            case TYPE_MEDAL_DATA:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .medal_breakdown_row, parent, false);
                break;
        }
        holder = new ViewHolder(view, viewType);
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return medalBreakdownData.get(position).type;
    }



    @Override public void onBindViewHolder (MedalBreakdownAdapter.ViewHolder holder,
                                            int position){

        switch (medalBreakdownData.get(position).type) {
            case TYPE_PLAYER_NAME:
                holder.athleteName.setText(medalBreakdownData.get(position).playername);
                break;
            case TYPE_MEDAL_DATA:

                MedalTallyByOrgModel medalTallyObj = medalBreakdownData.get(position).medalData;

                if (medalTallyObj.getGoldCount() > 0) {
                    holder.medalName.setBackgroundResource(R.drawable.goldmedal);
                    holder.medalName.setText("G");
                } else if (medalTallyObj.getSilverCount() > 0) {
                    holder.medalName.setBackgroundResource(R.drawable.silvermedal);
                    holder.medalName.setText("S");
                } else if (medalTallyObj.getBronzeCount() > 0) {
                    holder.medalName.setBackgroundResource(R.drawable.bronzemedal);
                    holder.medalName.setText("B");
                }
                holder.sportsName.setText(medalTallyObj.getSportsName());
                break;

        }
    }

    @Override public int getItemCount () {
        if (medalBreakdownData != null) {
            return medalBreakdownData.size();
        }

        return 0;
    }

    public static class MedalBreakdownData {
        public String playername;
        public int type;
        public MedalTallyByOrgModel medalData;

        public MedalBreakdownData() {
        }

        public MedalBreakdownData(int type, String playerName) {
            this.type = type;
            this.playername = playerName;
        }

        public MedalBreakdownData(int type, MedalTallyByOrgModel medalData) {
            this.type = type;
            this.medalData = medalData;
        }
    }
}