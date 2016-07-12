package com.olympics.olympicsandroid.view.activity;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.olympics.olympicsandroid.R;

public class OlympicsbaseActivity extends AppCompatActivity implements NavigationView
        .OnNavigationItemSelectedListener {


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_events) {

        } else if (id == R.id.nav_medal) {
            this.startActivity(new Intent(this, MedalTallyActivity
                    .class));
        } else if (id == R.id.nav_athletes) {
            this.startActivity(new Intent(this, AthleteActivity
                    .class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
