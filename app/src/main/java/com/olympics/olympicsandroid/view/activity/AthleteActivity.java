package com.olympics.olympicsandroid.view.activity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.olympics.olympicsandroid.R;
import com.olympics.olympicsandroid.model.ErrorModel;
import com.olympics.olympicsandroid.model.IResponseModel;
import com.olympics.olympicsandroid.model.presentationModel.Athlete;
import com.olympics.olympicsandroid.model.presentationModel.CountryEventUnitModel;
import com.olympics.olympicsandroid.networkLayer.controller.CountryProfileController;
import com.olympics.olympicsandroid.networkLayer.controller.IUIListener;
import com.olympics.olympicsandroid.view.activity.factory.ActivityFactory;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class AthleteActivity extends AppCompatActivity implements NavigationView
.OnNavigationItemSelectedListener, IUIListener, SearchView.OnQueryTextListener{

    public static final String MALE = "male";
    public static final String NO_GENDER = "no gender";

    private static final String MEN_SPORTS_STR = " - Men";
    private static final String WOMEN_SPORTS_STR = " - Women";
    private AthleteListAdapter athleteListAdapter;
    private List<Athlete> athleteList;

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
        CountryProfileController scheduleController = new CountryProfileController(new
                WeakReference<IUIListener>(this), getApplication());
        scheduleController.getCountryDetails();

        //Setup Actionbar
        setActionBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.athlete_selection, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint(getString(R.string.athlete_search_hint));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        return true;
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
            athleteList = getListfromSet(countryEventUnitModel.getAthleteList());
            athleteListAdapter.setAthleteList(athleteList);
            athleteListAdapter.notifyDataSetChanged();

        }
    }

    private List<Athlete> getListfromSet(HashSet<Athlete> athleteSet) {
        List<Athlete> athleteList = new ArrayList<>();

        Iterator<Athlete> it = athleteSet.iterator();
        while(it.hasNext()){
            athleteList.add(it.next());
        }

        return athleteList;
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        List<Athlete> filteredAthleteList = new ArrayList<>();

        if (TextUtils.isEmpty(newText) && athleteListAdapter != null)
        {
            athleteListAdapter.setAthleteList(athleteList);
            athleteListAdapter.notifyDataSetChanged();
            return true;
        }

        if(athleteList != null && athleteList.size() > 0) {
            for (Athlete athlete : athleteList) {
                if (!TextUtils.isEmpty(newText) && athlete != null && (!TextUtils.isEmpty(athlete
                        .getAthleteName()) && athlete
                        .getAthleteName().toLowerCase().contains(newText.toLowerCase())) || !TextUtils.isEmpty(newText) && athlete != null && (!TextUtils.isEmpty(athlete
                        .getSportName()) && athlete
                        .getSportName().toLowerCase().contains(newText.toLowerCase()))) {
                    filteredAthleteList.add(athlete);
                }
            }
            athleteListAdapter.setAthleteList(filteredAthleteList);
            athleteListAdapter.notifyDataSetChanged();
            return true;
        }
        return false;
    }

    private void onAthleteClickReceiver(Athlete athlete)
    {
        try {
            ActivityFactory.openEventActivity(this, (String) athlete.getParticipatingEventID().toArray()[0],athlete.getSportName());
        }catch (Exception ex)
        {

        }
    }

    class AthleteListAdapter extends RecyclerView.Adapter<AthleteListAdapter.ViewHolder>
    {
        List<Athlete> athleteList;


        protected void setAthleteList(List<Athlete> athleteList)
        {
            Collections.sort(athleteList);
            this.athleteList = athleteList;
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {
            private View athlete_layout;
            private ImageView sportsIconView;
            private TextView athleteNameView;
            private TextView eventView;

            public ViewHolder(View itemView) {
                super(itemView);
                athlete_layout = itemView.findViewById(R.id.id_athlete_layout);
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
            final Athlete athleteObj = athleteList.get(position);
            if (athleteObj != null) {
                holder.athleteNameView.setText(athleteObj.getAthleteName());

                String athleteDetail = "";
                if (!TextUtils.isEmpty(athleteObj.getSportName())) {

                    StringBuilder stringBuilder = new StringBuilder(athleteObj.getSportName());
                    athleteDetail = athleteObj.getAthleteGender().equalsIgnoreCase
                            (MALE) ? stringBuilder.append(MEN_SPORTS_STR).toString() : stringBuilder
                            .append(WOMEN_SPORTS_STR).toString();
                }

                if (!TextUtils.isEmpty(athleteObj.getDisciplineName())) {

                    if(TextUtils.isEmpty(athleteDetail))
                    {
                        athleteDetail = athleteObj.getDisciplineName();
                    }
                    else{
                        athleteDetail = athleteDetail +"- "+athleteObj.getDisciplineName();
                    }
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

                holder.athlete_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onAthleteClickReceiver(athleteObj);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return athleteList != null ?athleteList.size() :0;
        }

    }


}
