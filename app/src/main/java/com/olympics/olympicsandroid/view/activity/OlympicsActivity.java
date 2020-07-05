package com.olympics.olympicsandroid.view.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.olympics.olympicsandroid.OlympicsApplication;
import com.olympics.olympicsandroid.R;
import com.olympics.olympicsandroid.model.ErrorModel;
import com.olympics.olympicsandroid.model.IResponseModel;
import com.olympics.olympicsandroid.model.MedalTally;
import com.olympics.olympicsandroid.model.MedalTallyOrganization;
import com.olympics.olympicsandroid.model.Organization;
import com.olympics.olympicsandroid.model.presentationModel.CountryEventUnitModel;
import com.olympics.olympicsandroid.model.presentationModel.DateSportsModel;
import com.olympics.olympicsandroid.model.presentationModel.EventUnitModel;
import com.olympics.olympicsandroid.networkLayer.cache.database.OlympicsPrefs;
import com.olympics.olympicsandroid.networkLayer.controller.CountryProfileController;
import com.olympics.olympicsandroid.networkLayer.controller.IUIListener;
import com.olympics.olympicsandroid.networkLayer.controller.MedalTallyController;
import com.olympics.olympicsandroid.utility.DateUtils;
import com.olympics.olympicsandroid.utility.MedalTallyComparator;
import com.olympics.olympicsandroid.utility.SportsFilterListener;
import com.olympics.olympicsandroid.utility.UtilityMethods;
import com.olympics.olympicsandroid.view.activity.factory.ActivityFactory;
import com.olympics.olympicsandroid.view.fragment.DateEventAdapter;
import com.olympics.olympicsandroid.view.fragment.SportsFilterAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class OlympicsActivity extends AppCompatActivity implements NavigationView
        .OnNavigationItemSelectedListener, IUIListener, SportsFilterListener, View.OnClickListener {


    private DateEventAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private static int REQUEST_CODE_COUNTRY = 1;

    private MedalTally medalTallyObj;
    private ProgressDialog progress;
    private DrawerLayout drawer;
    private CountryEventUnitModel countryEventUnitModel;

    private FloatingActionButton filterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("OlympicsActivity", "Refreshed token: " + token);


//        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_ad_unit_id));


        setContentView(R.layout.activity_olympics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string
                .navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        filterButton = (FloatingActionButton) findViewById(R.id.sports_floating_btn);

        filterButton.setClickable(true);
        filterButton.setOnClickListener(this);

        setUpData();
    }

    private void setUpData() {
        //Display country information in navigation drawer
        displaySelectedCountryInfo();

        CountryProfileController scheduleController = new CountryProfileController(new
                WeakReference<IUIListener>(this), getApplication());
        scheduleController.getCountryDetails();

    }


    /**
     * This method displays country information in navigation drawer
     */
    private void displaySelectedCountryInfo() {

        //get selected country data from preference
        Organization countryInfo = OlympicsPrefs.getInstance(this).getUserSelectedCountry();

        View headerLayout = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);

        if (headerLayout != null) {

            headerLayout.findViewById(R.id
                    .CountryInfoLayout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startCountrySelectionScreen();
                }
            });

            //Setup Selected Country Name
            TextView countryNameTxt = (TextView) headerLayout.findViewById(R.id.countryTextView);
            if (countryNameTxt != null && !TextUtils.isEmpty(countryInfo.getDescription())) {
                countryNameTxt.setText(countryInfo.getDescription());
            }

            if (countryInfo != null && !TextUtils.isEmpty(countryInfo.getAlias())) {
                FirebaseMessaging.getInstance().subscribeToTopic(countryInfo.getAlias());
            }

            //Setup Selected Country Image
            ImageView countryImageView = (ImageView) headerLayout.findViewById(R.id.countryImageView);

            int rid = getResources().getIdentifier(countryInfo.getAlias().toLowerCase(), "raw", getPackageName());
            try {
                countryImageView.setImageBitmap(BitmapFactory.decodeStream(getResources().openRawResource(rid)));
            } catch (Exception ex) {
                System.out.println("Exeptipn == " + ex);
            }
        }
    }

    private void startCountrySelectionScreen() {
        Intent intent = new Intent();
        intent.putExtra("first_launch", false);
        ActivityFactory.openCountrySelectionScreenForResult(OlympicsActivity.this, intent, REQUEST_CODE_COUNTRY);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_COUNTRY) {
            if (resultCode == RESULT_OK) {
                String countryCode = data.getStringExtra("COUNTRY_CODE");

                if (!TextUtils.isEmpty(countryCode)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(countryCode);
                }

                if (drawer == null) {
                    drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                }
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }

                setUpData();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer == null) {
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_medal:
                ActivityFactory.openMedalActivity(this, null);
                break;
            case R.id.nav_athletes:
                ActivityFactory.openAthleteActivity(this, null);
                break;
            case R.id.nav_info:
                ActivityFactory.openAppInfoActivity(this);
                break;
        }

        if (drawer == null) {
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSuccess(IResponseModel responseModel) {
        if (responseModel instanceof CountryEventUnitModel) {
            if (mSectionsPagerAdapter == null) {
                mSectionsPagerAdapter = new DateEventAdapter(this, getSupportFragmentManager(),
                        (CountryEventUnitModel) responseModel);

                // Set up the ViewPager with the sections adapter.
                mViewPager = (ViewPager) findViewById(R.id.container);
                mViewPager.setAdapter(mSectionsPagerAdapter);

                TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
                tabLayout.setupWithViewPager(mViewPager);
                mViewPager.setCurrentItem(getCurrentScheduleIndex(), true);
            } else {
                if (mSectionsPagerAdapter != null)
                    mSectionsPagerAdapter.updateModel((CountryEventUnitModel) responseModel);
                mSectionsPagerAdapter.notifyDataSetChanged();
                mViewPager.invalidate();
            }

            countryEventUnitModel = (CountryEventUnitModel) responseModel;

            //Call MedalTally API through controller
            //Request for MedalTally Data through controller
            MedalTallyController medalTallyController = new MedalTallyController(new
                    WeakReference<IUIListener>(this), getApplication());
            medalTallyController.getMedalTallyData();


        } else if (responseModel instanceof MedalTally) {
            //Display Medal info
            medalTallyObj = (MedalTally) responseModel;
            displayMedalInfoForCountry();
        }
    }

    /**
     * This method is responsible for displaying selected country's medal tally.
     */
    private void displayMedalInfoForCountry() {

        if (medalTallyObj != null && medalTallyObj.getOrganization() != null && !medalTallyObj
                .getOrganization().isEmpty()) {

            List<MedalTallyOrganization> medaltallyOrgList = medalTallyObj.getOrganization();

            Collections.sort(medaltallyOrgList, new MedalTallyComparator(OlympicsPrefs.getInstance(null)
                    .getUserSelectedCountry()));

            View headerLayout = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);

            if (headerLayout != null) {
                if (!TextUtils.isEmpty(medaltallyOrgList.get(0).getGold())) {
                    ((TextView) headerLayout.findViewById(R.id.gold_medal_count)).setText(medaltallyOrgList.get(0).getGold());
                }
                if (!TextUtils.isEmpty(medaltallyOrgList.get(0).getSilver())) {
                    ((TextView) headerLayout.findViewById(R.id.silver_medal_count)).setText
                            (medaltallyOrgList.get(0).getSilver());
                }
                if (!TextUtils.isEmpty(medaltallyOrgList.get(0).getBronze())) {
                    ((TextView) headerLayout.findViewById(R.id.bronze_medal_count)).setText(medaltallyOrgList
                            .get
                                    (0).getBronze());
                }
            }
        }
    }

    @Override
    public void onFailure(ErrorModel errorModel) {
        if (errorModel != null && !TextUtils.isEmpty(errorModel.getErrorCode()) && errorModel.getErrorCode().equalsIgnoreCase(UtilityMethods.ERROR_INTERNET)) {

            try {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                dlgAlert.setMessage(getString(R.string.id_error_internet));
                dlgAlert.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        setUpData();
                    }
                });
                dlgAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                dlgAlert.setCancelable(false);
                dlgAlert.create().show();
            } catch (WindowManager.BadTokenException ex) {

            }

            Bundle params = new Bundle();
            params.putString("app_error", "event_result");
            params.putString("app_screen", "country_schedule");

            params.putString("error_reason", errorModel.getErrorMessage());
            FirebaseAnalytics.getInstance(OlympicsApplication.getAppContext()).logEvent("error", params);

        } else {
            try {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                dlgAlert.setMessage(getString(R.string.id_error_server));
                dlgAlert.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        setUpData();
                    }
                });
                dlgAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                dlgAlert.setCancelable(false);
                dlgAlert.create().show();
            } catch (WindowManager.BadTokenException ex) {

            }
            Bundle params = new Bundle();
            params.putString("app_error", "event_result");
            params.putString("app_screen", "country_schedule");

            if (errorModel != null && !TextUtils.isEmpty(errorModel.getErrorMessage())) {
                params.putString("error_reason", errorModel.getErrorMessage());
            } else {
                params.putString("error_reason", "generic_error");
            }
            FirebaseAnalytics.getInstance(OlympicsApplication.getAppContext()).logEvent("error", params);
        }

    }

    @Override
    public void handleLoadingIndicator(boolean showLoadingInd) {
        if (showLoadingInd) {
            progress = new ProgressDialog(this);
            progress.setTitle("Loading");
            progress.setMessage("Wait while loading country details...");
            progress.show();

        } else {
            if (progress != null) {
                progress.dismiss();
            }
        }
    }

    /**
     * This method determines if current date falls within Olympics schedule.
     *
     * @return index of tab if current date is within Olympics schedule, 0 otherwise
     */
    public int getCurrentScheduleIndex() {

        long eventDate = DateUtils.getOlympicEventStartDate();
        int index = 0;
        while (eventDate < DateUtils.getOlympicEventEndDate()) {
            if (DateUtils.getCurrentDate() == eventDate) {
                return index;
            }
            index++;
            eventDate += DateUtils.NUM_OF_MILISECONDS_IN_DAY;
        }
        return 0;
    }

    private List<String> getFilteredSportsList() {

        Set<String> sportsFilterList = new HashSet<>();
        int position = mSectionsPagerAdapter.getPosition();

        String dateKey = String.valueOf(DateUtils.getOlympicEventStartDate() + (
                (position) * DateUtils.NUM_OF_MILISECONDS_IN_DAY));
        DateSportsModel dateSportsModel = countryEventUnitModel.
                getDatesCountryMapping().get(dateKey);

        if (dateSportsModel != null && dateSportsModel.getAllSportsForDate() != null) {
            for (Map.Entry<String, DateSportsModel.SportsEventsUnits> entry : dateSportsModel.getAllSportsForDate().entrySet()) {
                if (entry.getValue() != null) {
                    DateSportsModel.SportsEventsUnits sportsEventsUnit = entry.getValue();
                    if (sportsEventsUnit != null && sportsEventsUnit.getEventUnits() != null) {

                        for (EventUnitModel eventUnitModel : sportsEventsUnit.getEventUnits()) {
                            if (!TextUtils.isEmpty(eventUnitModel.getParentDisciple())) {
                                sportsFilterList.add(eventUnitModel.getDisciplineAlias());
                            }
                        }
                    }
                }
            }
        }
        return new ArrayList(sportsFilterList);
    }

    private void filterSports(String sportsAlias) {

        CountryEventUnitModel filteredCountryEventModel = countryEventUnitModel;
        int position = mSectionsPagerAdapter.getPosition();

        if (!TextUtils.isEmpty(sportsAlias)) {
            filteredCountryEventModel = new CountryEventUnitModel();
            filteredCountryEventModel.setDatesCountryMapping(new HashMap<String, DateSportsModel>());

            String dateKey = String.valueOf(DateUtils.getOlympicEventStartDate() + ((position) * DateUtils.NUM_OF_MILISECONDS_IN_DAY));
            DateSportsModel dateSportsModel =
                    countryEventUnitModel
                            .getDatesCountryMapping().get(dateKey);

            if (dateSportsModel != null && dateSportsModel.getAllSportsForDate() != null) {
                DateSportsModel newDateSportsModel = new DateSportsModel();
                newDateSportsModel.setAllSportsForDate(new HashMap<String, DateSportsModel.SportsEventsUnits>());

                for (Map.Entry<String, DateSportsModel.SportsEventsUnits> entry : dateSportsModel.getAllSportsForDate().entrySet()) {
                    if (entry.getValue() != null) {

                        DateSportsModel.SportsEventsUnits sportsEventsUnit = entry.getValue();
                        if (sportsEventsUnit != null && sportsEventsUnit.getEventUnits() != null) {
                            DateSportsModel.SportsEventsUnits newSportsEventsUnit = new
                                    DateSportsModel.SportsEventsUnits();
                            newSportsEventsUnit.setEventUnits(new ArrayList<EventUnitModel>());
                            Iterator iterator = sportsEventsUnit.getEventUnits().iterator();
                            while (iterator.hasNext()) {
                                EventUnitModel eventUnitModel = (EventUnitModel) iterator.next();
                                if (sportsAlias.equalsIgnoreCase(eventUnitModel
                                        .getDisciplineAlias())) {
                                    newSportsEventsUnit.getEventUnits().add(eventUnitModel);
                                }
                            }
                            newDateSportsModel.getAllSportsForDate().put(entry.getKey(),
                                    newSportsEventsUnit);
                        }
                    }
                }
                filteredCountryEventModel.getDatesCountryMapping().put(dateKey, newDateSportsModel);

                // Populating sports data for rest of the dates
                for (Map.Entry<String, DateSportsModel> entry :  countryEventUnitModel
                        .getDatesCountryMapping().entrySet()) {

                    if (entry != null && !TextUtils.isEmpty(entry.getKey()) && !entry.getKey()
                            .equalsIgnoreCase
                            (dateKey)) {
                        filteredCountryEventModel.getDatesCountryMapping().put(entry.getKey(),
                                entry.getValue());
                    }
                }
            }
        }
        mSectionsPagerAdapter.updateModel(filteredCountryEventModel);
        mSectionsPagerAdapter.notifyDataSetChanged();
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(position, true);
    }

    @Override
    public void onSportsSelected(String filteredSport) {
        filterSports(filteredSport);
        if(filterButton != null)
        {
            filterButton.setImageDrawable(ContextCompat.getDrawable(OlympicsApplication.getAppContext(), R.drawable.filter_remove));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.sports_floating_btn:

                if (findViewById(R.id.sports_filter).getVisibility() == View.GONE) {
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.sports_filter);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(OlympicsActivity.this, LinearLayoutManager.HORIZONTAL, false);

                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setVisibility(View.VISIBLE);

                    SportsFilterAdapter sportsFilterAdapter = new SportsFilterAdapter(OlympicsActivity.this, getFilteredSportsList());
                    recyclerView.setAdapter(sportsFilterAdapter);
                } else {
                    findViewById(R.id.sports_filter).setVisibility(View.GONE);
                    filterSports(null);
                    if(filterButton != null)
                    {
                        filterButton.setImageDrawable(ContextCompat.getDrawable(OlympicsApplication.getAppContext(), R.drawable.filter));
                    }
                }
                break;
        }
    }
}
