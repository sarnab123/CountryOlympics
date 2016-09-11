package com.olympics.olympicsandroid.networkLayer.cache.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.olympics.olympicsandroid.model.CountryModel;
import com.olympics.olympicsandroid.model.CountryProfileEvents;
import com.olympics.olympicsandroid.model.ErrorModel;
import com.olympics.olympicsandroid.model.OlympicSchedule;
import com.olympics.olympicsandroid.model.Organization;
import com.olympics.olympicsandroid.model.presentationModel.CountryEventUnitModel;
import com.olympics.olympicsandroid.model.presentationModel.helper.CountryEventsHelper;
import com.olympics.olympicsandroid.networkLayer.CustomXMLRequest;
import com.olympics.olympicsandroid.networkLayer.OlympicRequestQueries;
import com.olympics.olympicsandroid.networkLayer.RequestPolicy;
import com.olympics.olympicsandroid.networkLayer.VolleySingleton;
import com.olympics.olympicsandroid.networkLayer.cache.database.OlympicsPrefs;
import com.olympics.olympicsandroid.networkLayer.cache.file.DataCacheHelper;
import com.olympics.olympicsandroid.networkLayer.controller.IScheduleListener;
import com.olympics.olympicsandroid.networkLayer.controller.ScheduleController;
import com.olympics.olympicsandroid.utility.DateUtils;
import com.olympics.olympicsandroid.utility.Logger;
import com.olympics.olympicsandroid.utility.UtilityMethods;

import java.util.HashMap;

/**
 * Created by sarnab.poddar on 7/25/16.
 */
public class AppCacheService extends Service {

    private OlympicSchedule totalSchedule = null;

    private int count = 0;
    private HashMap<String, String> countryAliastoID;
    private String[] countrytoCache;

    private volatile ServiceHandler mServiceHandler;
    private Looper mServiceLooper;

    private final int MESSAGE_SCHEDULE = 0;
    private final int COUNTRY_LIST = 1;
    private final int COUNTRY_IDS = 2;
    private final int COUNTRY_PROFILE = 3;
    private final int COUNTRY_UI_MODEL = 4;

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        HandlerThread thread = new HandlerThread("AppCacheService");
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = MESSAGE_SCHEDULE;
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg)
        {
            switch(msg.arg1)
            {
                case MESSAGE_SCHEDULE:
                    ScheduleController.getInstance().getScheduleData(createScheduleListener());
                    break;
                case COUNTRY_LIST:
                    getCountryList();
                break;

                case COUNTRY_IDS:
                    updateCountryIDs((CountryModel)msg.obj);
                    break;

                case COUNTRY_PROFILE:
                    makeCountryProfileCall();
                    break;

                case COUNTRY_UI_MODEL:

                    savePresentationModelCountry((CountryProfileEvents)msg.obj);
                    break;
            }
        }
    }

    private IScheduleListener createScheduleListener() {
        return new IScheduleListener() {
            @Override
            public void scheduleSuccess(OlympicSchedule olympicSchedule) {
                Logger.logs("AppCacheService", "Schedule success thread id == " + android.os.Process.getThreadPriority(android.os.Process.myTid()));
                boolean isOnUiThread = Thread.currentThread() == Looper.getMainLooper().getThread();
                Logger.logs("AppCacheService", "is main thread == " + isOnUiThread);
                totalSchedule = olympicSchedule;

                // For each start request, send a message to start a job and deliver the
                // start ID so we know which request we're stopping when we finish the job
                Message msg = mServiceHandler.obtainMessage();
                msg.arg1 = COUNTRY_LIST;
                mServiceHandler.sendMessage(msg);

            }

            @Override
            public void scheduleError(ErrorModel errorModel) {

            }
        };
    }

    private void getCountryList() {

        RequestPolicy requestPolicy = new RequestPolicy();
        if (DateUtils.isCurrentDateInOlympics()) {
            requestPolicy.setForceCache(true);
            requestPolicy.setMaxAge(60 * 60 * 10);
        }
        boolean isOnUiThread = Thread.currentThread() == Looper.getMainLooper().getThread();

        Logger.logs("AppCacheService", "get country list thread id == " + android.os.Process.getThreadPriority(android.os.Process.myTid()));
        Logger.logs("AppCacheService", "get country list is main thread == " + isOnUiThread);

        CustomXMLRequest<CountryModel> countryRequest = new CustomXMLRequest<CountryModel>(OlympicRequestQueries.COUNTRY_LIST, CountryModel.class,
                createCountryListSuccessListener(), createCountryListFailureListener(), requestPolicy);
        VolleySingleton.getInstance(null).addToRequestQueue(countryRequest);
    }

    private Response.ErrorListener createCountryListFailureListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                OlympicsPrefs.getInstance(null).setCacheChecksum("");
                stopSelf();
            }
        };
    }

    private Response.Listener<CountryModel> createCountryListSuccessListener() {
        return new Response.Listener<CountryModel>() {
            @Override
            public void onResponse(CountryModel response) {

                Logger.logs("AppCacheService", "country response thread id == " + android.os.Process.getThreadPriority(android.os.Process.myTid()));

                boolean isOnUiThread = Thread.currentThread() == Looper.getMainLooper().getThread();
                Logger.logs("AppCacheService", "country response is main thread == " + isOnUiThread);
                // For each start request, send a message to start a job and deliver the
                // start ID so we know which request we're stopping when we finish the job
                Message msg = mServiceHandler.obtainMessage();
                msg.arg1 = COUNTRY_IDS;
                msg.obj = response;
                mServiceHandler.sendMessage(msg);

            }
        };
    }

    private void updateCountryIDs(CountryModel response)
    {

        boolean isOnUiThread = Thread.currentThread() == Looper.getMainLooper().getThread();
        Logger.logs("AppCacheService", "updateCountryIDs is main thread == " + isOnUiThread);

        if (response != null && response.getOrganization() != null && response.getOrganization().size() > 0) {
            DataCacheHelper.getInstance().saveDataModel(DataCacheHelper.CACHE_COUNTRYSELECTION_MODEL, response);

            String cacheCountry = OlympicsPrefs.getInstance(null).getCacheCountry();
            if (!TextUtils.isEmpty(cacheCountry)) {
                countrytoCache = cacheCountry.split(";");
                countryAliastoID = new HashMap<>();
                for (Organization organization : response.getOrganization()) {
                    countryAliastoID.put(organization.getAlias(), organization.getId());
                }

                count = countrytoCache.length;

            }
        }
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = COUNTRY_PROFILE;
        mServiceHandler.sendMessage(msg);

    }

    private synchronized void makeCountryProfileCall() {
        Logger.logs("AppCacheService", "Count ======= == " + count);

        if (count > 0 && !TextUtils.isEmpty(countryAliastoID.get(countrytoCache[--count]))) {
            boolean isOnUiThread = Thread.currentThread() == Looper.getMainLooper().getThread();
            Logger.logs("AppCacheService", "makeCountryProfileCall is main thread == " + isOnUiThread);

            RequestPolicy requestPolicy = new RequestPolicy();
            if (DateUtils.isCurrentDateInOlympics()) {
                requestPolicy.setForceCache(true);
                requestPolicy.setMaxAge(60 * 60 * 10);
            }

            requestPolicy.setUrlReplacement(countryAliastoID.get(countrytoCache[count] +
                    UtilityMethods.COUNTRY_CONFIG));
            CustomXMLRequest<CountryProfileEvents> countryRequest = new CustomXMLRequest<CountryProfileEvents>(OlympicRequestQueries.COUNTRY_CONFIG, CountryProfileEvents.class, createCountryProfileSuccessListener(), createCountryProfileFailureListener(), requestPolicy);
            VolleySingleton.getInstance(null).addToRequestQueue(countryRequest);
        }
        else{
            stopSelf();
        }

    }

    private Response.ErrorListener createCountryProfileFailureListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                OlympicsPrefs.getInstance(null).setCacheChecksum("");
                stopSelf();
            }
        };
    }

    private Response.Listener<CountryProfileEvents> createCountryProfileSuccessListener() {
        return new Response.Listener<CountryProfileEvents>() {
            @Override
            public void onResponse(CountryProfileEvents countryProfileEvents) {
                boolean isOnUiThread = Thread.currentThread() == Looper.getMainLooper().getThread();
                Logger.logs("AppCacheService", "country response success is main thread == " + isOnUiThread);

                Message msg = mServiceHandler.obtainMessage();
                msg.arg1 = COUNTRY_UI_MODEL;
                msg.obj = countryProfileEvents;
                mServiceHandler.sendMessage(msg);

            }
        };
    }

    private void savePresentationModelCountry(CountryProfileEvents countryProfileEvents)
    {

        boolean isOnUiThread = Thread.currentThread() == Looper.getMainLooper().getThread();
        Logger.logs("AppCacheService", "savePresentationModelCountry is main thread == " + isOnUiThread);

        CountryEventUnitModel countryEventData = new CountryEventsHelper(countryProfileEvents,
                totalSchedule)
                .createCountryEventUnitModel();
        DataCacheHelper.getInstance().saveDataModel(DataCacheHelper.CACHE_COUNTRY_MODEL, countryEventData);

        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = COUNTRY_PROFILE;
        mServiceHandler.sendMessage(msg);
    }


}
