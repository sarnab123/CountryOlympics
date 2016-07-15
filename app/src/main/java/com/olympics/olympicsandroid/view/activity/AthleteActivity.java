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
import android.widget.ImageView;
import android.widget.TextView;

import com.olympics.olympicsandroid.R;
import com.olympics.olympicsandroid.model.ErrorModel;
import com.olympics.olympicsandroid.model.IResponseModel;
import com.olympics.olympicsandroid.model.presentationModel.Athlete;
import com.olympics.olympicsandroid.model.presentationModel.CountryEventUnitModel;
import com.olympics.olympicsandroid.networkLayer.controller.CountryScheduleController;
import com.olympics.olympicsandroid.networkLayer.controller.IUIListener;

import java.lang.ref.WeakReference;
import java.util.LinkedHashSet;

public class AthleteActivity extends AppCompatActivity implements NavigationView
.OnNavigationItemSelectedListener, IUIListener {

    public static final String MALE = "male";
    private AthleteListAdapter athleteListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athlete);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.athlete_recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        athleteListAdapter = new AthleteListAdapter();
        mRecyclerView.setAdapter(athleteListAdapter);

        //Request for Athlete Data through Controller
        CountryScheduleController scheduleController = new CountryScheduleController(new
                WeakReference<IUIListener>(this), getApplication());
        scheduleController.getCountryDetails();

    }

    @Override
    public void onSuccess(IResponseModel responseModel) {

        if (responseModel instanceof CountryEventUnitModel) {

            CountryEventUnitModel countryEventUnitModel = (CountryEventUnitModel) responseModel;
            athleteListAdapter.setAthleteList(countryEventUnitModel.getAthleteList());
            athleteListAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onFailure(ErrorModel errorModel) {

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    class AthleteListAdapter extends RecyclerView.Adapter<AthleteListAdapter.ViewHolder>
    {
        LinkedHashSet<Athlete> athleteList;

        protected void setAthleteList(LinkedHashSet<Athlete> athleteList)
        {
            this.athleteList = athleteList;
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {
            private ImageView genderIconView;
            private TextView athleteNameView;
            private TextView eventView;

            public ViewHolder(View itemView) {
                super(itemView);

                genderIconView = (ImageView)itemView.findViewById(R.id.id_gender_icon);
                athleteNameView = (TextView)itemView.findViewById(R.id.id_athlete_name);
                eventView = (TextView)itemView.findViewById(R.id.id_participating_event);
            }
        }

        @Override
        public AthleteListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater inflater =
                    (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View convertView = inflater.inflate(R.layout.athlete_list_row, parent, false);
            return new ViewHolder(convertView);
        }

        @Override
        public void onBindViewHolder(AthleteListAdapter.ViewHolder holder, int position)
        {
            Athlete athleteObj = (Athlete)athleteList.toArray()[position];
            if (athleteObj != null) {
                holder.athleteNameView.setText(athleteObj.getAthleteName());
                holder.eventView.setText(athleteObj.getSportName());

                if (!TextUtils.isEmpty(athleteObj.getAthleteGender()) && athleteObj
                        .getAthleteGender().equalsIgnoreCase(MALE)) {
                    holder.genderIconView.setImageResource(R.drawable.athlete_male);
                } else {
                    holder.genderIconView.setImageResource(R.drawable.athlete_female);
                }
            }
        }

        @Override
        public int getItemCount() {
            return athleteList != null ?athleteList.size() :0;
        }
    }
}
