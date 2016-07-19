package com.olympics.olympicsandroid.view.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.olympics.olympicsandroid.R;
import com.olympics.olympicsandroid.model.ErrorModel;
import com.olympics.olympicsandroid.model.IResponseModel;
import com.olympics.olympicsandroid.model.MedalTally;
import com.olympics.olympicsandroid.model.MedalTallyOrganization;
import com.olympics.olympicsandroid.model.Organization;
import com.olympics.olympicsandroid.model.presentationModel.CountryEventUnitModel;
import com.olympics.olympicsandroid.networkLayer.cache.database.OlympicsPrefs;
import com.olympics.olympicsandroid.networkLayer.controller.CountryScheduleController;
import com.olympics.olympicsandroid.networkLayer.controller.IUIListener;
import com.olympics.olympicsandroid.networkLayer.controller.MedalTallyController;
import com.olympics.olympicsandroid.utility.MedalTallyComparator;
import com.olympics.olympicsandroid.view.activity.factory.ActivityFactory;
import com.olympics.olympicsandroid.view.fragment.DateEventAdapter;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

public class OlympicsActivity extends AppCompatActivity implements NavigationView
        .OnNavigationItemSelectedListener, IUIListener {


    private DateEventAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olympics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string
                .navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Display country information in navigation drawer
        displaySelectedCountryInfo();

        CountryScheduleController scheduleController = new CountryScheduleController(new
                WeakReference<IUIListener>(this), getApplication());
        scheduleController.getCountryDetails();

        //Call MedalTally API through controller
        //Request for MedalTally Data through controller
        MedalTallyController medalTallyController = new MedalTallyController(new
                WeakReference<IUIListener>(this), getApplication());
        medalTallyController.getMedalTallyData();

        View headerLayout = ((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0);

    }




    /**
     * This method displays country information in navigation drawer
     */
    private void displaySelectedCountryInfo() {

        //get selected country data from preference
        Organization countryInfo = OlympicsPrefs.getInstance(this).getUserSelectedCountry();

        View headerLayout = ((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0);

        if (headerLayout != null) {
            headerLayout.findViewById(R.id.CountryView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(OlympicsActivity.this,CountrySelectionActivity.class));
                }
            });

            //Setup Selected Country Name
            TextView countryNameTxt = (TextView) headerLayout.findViewById(R.id.countryTextView);
            if (countryNameTxt != null && !TextUtils.isEmpty(countryInfo.getDescription())) {
                countryNameTxt.setText(countryInfo.getDescription());
            }

            //Setup Selected Country Image
            ImageView countryImageView = (ImageView) headerLayout.findViewById(R.id.countryImageView);

            int rid = getResources().getIdentifier(countryInfo.getAlias().toLowerCase(), "raw", getPackageName());
            try {
                countryImageView.setImageBitmap(BitmapFactory.decodeStream(getResources().openRawResource(rid)));
                countryImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("first_launch",false);
                        ActivityFactory.openCountrySelectionScreen(OlympicsActivity.this,intent);
                    }
                });
            } catch (Exception ex) {
                System.out.println("Exeptipn == " + ex);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_olympics_drawer, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_events) {

        } else if (id == R.id.nav_medal) {
            ActivityFactory.openMedalActivity(this, null);
        } else if (id == R.id.nav_athletes) {
            ActivityFactory.openAthleteActivity(this,null);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSuccess(IResponseModel responseModel) {
        if (responseModel instanceof CountryEventUnitModel) {
            mSectionsPagerAdapter = new DateEventAdapter(getSupportFragmentManager(),
                    (CountryEventUnitModel) responseModel);

            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);
        } else if (responseModel instanceof MedalTally)
        {
            //Display Medal info
            displayMedalInfoForCountry((MedalTally) responseModel);
        }
    }

    /**
     * This method is responsible for displaying selected country's medal tally.
     * @param medalTallyObj
     */
    private void displayMedalInfoForCountry(MedalTally medalTallyObj) {

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

    }

    @Override
    public void handleLoadingIndicator(boolean showLoadingInd) {

    }
}
