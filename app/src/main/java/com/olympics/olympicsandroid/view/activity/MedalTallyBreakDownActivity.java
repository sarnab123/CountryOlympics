package com.olympics.olympicsandroid.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.olympics.olympicsandroid.R;
import com.olympics.olympicsandroid.model.ErrorModel;
import com.olympics.olympicsandroid.model.IResponseModel;
import com.olympics.olympicsandroid.model.MedalTallyBreakdown;
import com.olympics.olympicsandroid.model.MedalTallyEvent;
import com.olympics.olympicsandroid.networkLayer.controller.IUIListener;
import com.olympics.olympicsandroid.networkLayer.controller.MedalTallyBreakdownController;
import com.olympics.olympicsandroid.utility.UtilityMethods;

import java.lang.ref.WeakReference;
import java.util.List;

public class MedalTallyBreakDownActivity extends AppCompatActivity implements NavigationView
        .OnNavigationItemSelectedListener, IUIListener {

    private MedalTallyListAdapter medalTallyListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medal_tally_breakdown);

        //Request for MedalTally Data through controller
        MedalTallyBreakdownController medalTallyBreakdownController = new MedalTallyBreakdownController(new
                WeakReference<IUIListener>(this), getApplication());
        medalTallyBreakdownController.getMedalTallyData(getIntent().getStringExtra(UtilityMethods
                        .EXTRA_SELECTED_COUNTRY));

                //Setup Actionbar
                setActionBar();
    }

    private void setActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(IResponseModel responseModel) {
        if(responseModel instanceof MedalTallyBreakdown)
        {
            MedalTallyBreakdown medalTallyObj = (MedalTallyBreakdown) responseModel;
            RecyclerView mRecyclerView = (RecyclerView)findViewById(R.id.medaltally_recyclerView);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,
                    false);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);

            medalTallyListAdapter = new MedalTallyListAdapter();
            medalTallyListAdapter.setMedalTallyList(medalTallyObj.getOrganization().getMedalTallyEvents());
            mRecyclerView.setAdapter(medalTallyListAdapter);

        }
    }

    @Override
    public void onFailure(ErrorModel errorModel)
    {
        if(errorModel != null  && !TextUtils.isEmpty(errorModel.getErrorMessage())) {
            Toast.makeText(this, errorModel.getErrorMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void handleLoadingIndicator(boolean showLoadingInd) {

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }


    class MedalTallyListAdapter extends RecyclerView.Adapter<MedalTallyListAdapter.ViewHolder>
    {
        List<MedalTallyEvent> medalTallyEvents;

        protected void setMedalTallyList(List<MedalTallyEvent> medalTallyEvents)
        {
            this.medalTallyEvents = medalTallyEvents;
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {
            private TextView medalName;
            private TextView athleteName;
            private TextView sportsName;

            public ViewHolder(View itemView) {
                super(itemView);

                medalName = (TextView)itemView.findViewById(R.id.id_medal_name);
                athleteName = (TextView)itemView.findViewById(R.id.id_athlete_name);
                sportsName = (TextView)itemView.findViewById(R.id.id_sports_name);
            }
        }

        @Override
        public MedalTallyListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater inflater =
                    (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View convertView = inflater.inflate(R.layout.medal_breakdown_row, parent, false);
            return new ViewHolder(convertView);
        }

        @Override
        public void onBindViewHolder(MedalTallyListAdapter.ViewHolder holder, int position) {

            MedalTallyEvent medalTallyObj = medalTallyEvents.get(position);

            if(medalTallyObj.getGold() > 0) {
                holder.medalName.setBackgroundResource(R.drawable.goldmedal);
                holder.medalName.setText("G");
            } else if (medalTallyObj.getSilver() > 0) {
                holder.medalName.setBackgroundResource(R.drawable.silvermedal);
                holder.medalName.setText("S");
            } else if (medalTallyObj.getBronze() > 0) {
                holder.medalName.setBackgroundResource(R.drawable.bronzemedal);
                holder.medalName.setText("B");
            }

            if (medalTallyObj.getCompetitor() != null) {
                holder.athleteName.setText(medalTallyObj.getCompetitor().get(0).getFirst_name() +
                        " " +
                        medalTallyObj.getCompetitor().get(0).getLast_name());
            }
            holder.sportsName.setText(medalTallyObj.getDescription() + " - " +medalTallyObj
                    .getSport()
                    .getDescription());
        }

        @Override
        public int getItemCount() {
            return medalTallyEvents != null ?medalTallyEvents.size() :0;
        }
    }
}
