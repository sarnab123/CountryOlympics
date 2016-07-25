package com.olympics.olympicsandroid.networkLayer.cache.service;

import android.app.IntentService;
import android.content.Intent;
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

import java.util.HashMap;

/**
 * Created by sarnab.poddar on 7/25/16.
 */
public class AppCacheService extends IntentService
{

    private OlympicSchedule totalSchedule = null;

    private int count = 0;
    private HashMap<String, String> countryAliastoID;
    private String[] countrytoCache;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AppCacheService(String name) {
        super(name);
    }

    public AppCacheService()
    {
        super("AppCacheService");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        ScheduleController.getInstance().getScheduleData(createScheduleListener());
    }

    private IScheduleListener createScheduleListener() {
        return new IScheduleListener() {
            @Override
            public void scheduleSuccess(OlympicSchedule olympicSchedule) {
                totalSchedule = olympicSchedule;
                getCountryList();
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

        CustomXMLRequest<CountryModel> countryRequest = new CustomXMLRequest<CountryModel>(OlympicRequestQueries.COUNTRY_LIST, CountryModel.class,
                createCountryListSuccessListener(), createCountryListFailureListener(), requestPolicy);
        VolleySingleton.getInstance(null).addToRequestQueue(countryRequest);
    }

    private Response.ErrorListener createCountryListFailureListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };
    }

    private Response.Listener<CountryModel> createCountryListSuccessListener() {
        return new Response.Listener<CountryModel>() {
            @Override
            public void onResponse(CountryModel response) {
                if(response != null && response.getOrganization() != null && response.getOrganization().size() > 0) {
                    DataCacheHelper.getInstance().saveDataModel(DataCacheHelper.CACHE_COUNTRYSELECTION_MODEL, response);

                    String cacheCountry = OlympicsPrefs.getInstance(null).getCacheCountry();
                    if(!TextUtils.isEmpty(cacheCountry))
                    {
                        countrytoCache = cacheCountry.split(";");
                        countryAliastoID = new HashMap<>();
                        for(Organization organization:response.getOrganization())
                        {
                           countryAliastoID.put(organization.getAlias(),organization.getId());
                        }

                        count = countrytoCache.length;

                    }
                }
            }
        };
    }

    private void makeCountryProfileCall()
    {
        if(count >= 0 && !TextUtils.isEmpty(countryAliastoID.get(countrytoCache[--count])))
        {
            RequestPolicy requestPolicy = new RequestPolicy();
            if (DateUtils.isCurrentDateInOlympics()) {
                requestPolicy.setForceCache(true);
                requestPolicy.setMaxAge(60 * 60 * 10);
            }

            requestPolicy.setUrlReplacement(countryAliastoID.get(countrytoCache[count]));
            CustomXMLRequest<CountryProfileEvents> countryRequest = new CustomXMLRequest<CountryProfileEvents>(OlympicRequestQueries.COUNTRY_CONFIG, CountryProfileEvents.class, createCountryProfileSuccessListener(), createCountryProfileFailureListener(), requestPolicy);
            VolleySingleton.getInstance(null).addToRequestQueue(countryRequest);
        }

    }

    private Response.ErrorListener createCountryProfileFailureListener() {
        return null;
    }

    private Response.Listener<CountryProfileEvents> createCountryProfileSuccessListener() {
        return new Response.Listener<CountryProfileEvents>() {
            @Override
            public void onResponse(CountryProfileEvents countryProfileEvents) {
                CountryEventUnitModel countryEventData = new CountryEventsHelper(countryProfileEvents,
                        totalSchedule)
                        .createCountryEventUnitModel();
                DataCacheHelper.getInstance().saveDataModel(DataCacheHelper.CACHE_COUNTRY_MODEL, countryEventData);
                makeCountryProfileCall();
            }
        };
    }


}
