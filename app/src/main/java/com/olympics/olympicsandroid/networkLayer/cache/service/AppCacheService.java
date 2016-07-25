package com.olympics.olympicsandroid.networkLayer.cache.service;

import android.app.IntentService;
import android.content.Intent;

import com.olympics.olympicsandroid.networkLayer.controller.ScheduleController;

/**
 * Created by sarnab.poddar on 7/25/16.
 */
public class AppCacheService extends IntentService
{

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AppCacheService(String name) {
        super(name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        ScheduleController.getInstance().getScheduleData(null);
    }
}
