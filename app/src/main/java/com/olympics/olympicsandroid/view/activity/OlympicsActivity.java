package com.olympics.olympicsandroid.view.activity;

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
import com.olympics.olympicsandroid.model.Organization;
import com.olympics.olympicsandroid.model.presentationModel.CountryEventUnitModel;
import com.olympics.olympicsandroid.networkLayer.controller.CountryScheduleController;
import com.olympics.olympicsandroid.networkLayer.controller.IUIListener;
import com.olympics.olympicsandroid.networkLayer.database.OlympicsPrefs;
import com.olympics.olympicsandroid.view.fragment.DateEventAdapter;

import java.lang.ref.WeakReference;

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
    }

    /**
     * This method displays country information in navigation drawer
     */
    private void displaySelectedCountryInfo() {

        //get selected country data from preference
        Organization countryInfo = OlympicsPrefs.getInstance(this).getUserSelectedCountry();

        View headerLayout = ((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0);

        if (headerLayout != null) {
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
        }
    }

    @Override
    public void onFailure(ErrorModel errorModel) {

    }
}
