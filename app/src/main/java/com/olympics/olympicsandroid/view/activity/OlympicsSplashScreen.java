package com.olympics.olympicsandroid.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.olympics.olympicsandroid.R;

public class OlympicsSplashScreen extends Activity {

    private Handler mHandler;

    private Runnable myRunnable;
    private static final long SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_olympics_splash_screen);
        mHandler = new Handler();
        myRunnable = new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(OlympicsSplashScreen.this,
                        LaunchActivity.class);
                startActivity(i);
                finish();
            }
        };

    }

    @Override
    public void onBackPressed() {
        if (mHandler != null && myRunnable != null) {
            mHandler.removeCallbacks(myRunnable);
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        if (mHandler != null && myRunnable != null) {
            mHandler.removeCallbacks(myRunnable);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {

        if (mHandler != null && myRunnable != null) {
            mHandler.postDelayed(myRunnable, SPLASH_TIME_OUT);
        }
        super.onResume();
    }

    /**
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

