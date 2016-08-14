package com.olympics.olympicsandroid.view.fragment;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.olympics.olympicsandroid.R;
import com.olympics.olympicsandroid.utility.SportsFilterListener;

import java.util.List;

/**
 * Created by tkmagz4 on 8/12/16.
 */
public class SportsFilterAdapter extends RecyclerView.Adapter<SportsFilterAdapter.ViewHolder> {

    private List<String> sportsForTheDayList;
    private Activity activity;

    public SportsFilterAdapter(Activity activity, List<String>
            sportsForTheDayList) {
        this.activity = activity;
        this.sportsForTheDayList = sportsForTheDayList;
    }

    @Override
    public SportsFilterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater =
                (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.sports_filter, parent, false);

        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(SportsFilterAdapter.ViewHolder holder, int position) {

        final String filteredSportsName = sportsForTheDayList.get(position);
        //Setup Selected Country Image
        int rid = activity.getResources().getIdentifier(filteredSportsName.toLowerCase(), "raw", activity
                .getPackageName());
        try {
            holder.sportsIconView.setImageBitmap(BitmapFactory.decodeStream(activity.getResources
                    ().openRawResource(rid)));
            holder.sportsIconView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((SportsFilterListener)activity).onSportsSelected(filteredSportsName);
                }
            });
        } catch (Exception ex) {
            System.out.println("Exeptipn == " + ex);
        }
    }

    @Override
    public int getItemCount() {
        return sportsForTheDayList != null ?sportsForTheDayList.size() :0;
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView sportsIconView;

        public ViewHolder(View itemView) {
            super(itemView);

            sportsIconView = (ImageView) itemView.findViewById(R.id.sports_icon);
        }
    }
}
