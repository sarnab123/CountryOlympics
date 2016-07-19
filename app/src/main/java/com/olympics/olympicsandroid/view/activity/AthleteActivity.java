package com.olympics.olympicsandroid.view.activity;

import android.app.Activity;
import android.graphics.BitmapFactory;
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
import java.util.List;

public class AthleteActivity extends AppCompatActivity implements NavigationView
.OnNavigationItemSelectedListener, IUIListener {

    public static final String MALE = "male";
    private static final String MEN_SPORTS_STR = " - Men";
    private static final String WOMEN_SPORTS_STR = " - Women";
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
    public void handleLoadingIndicator(boolean showLoadingInd) {

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    class AthleteListAdapter extends RecyclerView.Adapter<AthleteListAdapter.ViewHolder>
    {
        List<Athlete> athleteList;

        protected void setAthleteList(List<Athlete> athleteList)
        {
            this.athleteList = athleteList;
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {
            private ImageView sportsIconView;
            private TextView athleteNameView;
            private TextView eventView;

            public ViewHolder(View itemView) {
                super(itemView);

                sportsIconView = (ImageView)itemView.findViewById(R.id.id_sports_icon);
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
            Athlete athleteObj = athleteList.get(position);
            if (athleteObj != null) {
                holder.athleteNameView.setText(athleteObj.getAthleteName());

                String athleteDetail = "";
                if (!TextUtils.isEmpty(athleteObj.getSportName())) {

                    StringBuilder stringBuilder = new StringBuilder(athleteObj.getSportName());
                    athleteDetail = athleteObj.getAthleteGender().equalsIgnoreCase
                            (MALE) ? stringBuilder.append(MEN_SPORTS_STR).toString() : stringBuilder
                            .append(WOMEN_SPORTS_STR).toString();
                }
                holder.eventView.setText(athleteDetail);

                int rid = getResources()
                        .getIdentifier(athleteObj.getSportsAlias().toLowerCase(), "raw", getPackageName());
                try {
                    holder.sportsIconView.setImageBitmap(BitmapFactory.decodeStream(getResources
                            ().openRawResource(rid)));
                } catch (Exception ex)
                {
                    System.out.println("Exeptipn == "+ex);
                }
            }
        }

        @Override
        public int getItemCount() {
            return athleteList != null ?athleteList.size() :0;
        }

    }
}
