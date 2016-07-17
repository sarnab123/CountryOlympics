package com.olympics.olympicsandroid.networkLayer.controller;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.olympics.olympicsandroid.model.EventResultsModel;
import com.olympics.olympicsandroid.networkLayer.CustomXMLRequest;
import com.olympics.olympicsandroid.networkLayer.OlympicRequestQueries;
import com.olympics.olympicsandroid.networkLayer.RequestPolicy;
import com.olympics.olympicsandroid.networkLayer.VolleySingleton;

import java.lang.ref.WeakReference;

/**
 * Created by sarnab.poddar on 7/16/16.
 */
public class EventResultsController
{
    protected WeakReference<IUIListener> listenerWeakReference;
    protected Context mCtx;

    public EventResultsController(WeakReference<IUIListener> listenerWeakReference, Context mCtx)
    {
        this.listenerWeakReference = listenerWeakReference;
        this.mCtx = mCtx;
    }

    public void getEventResults(String eventID)
    {
        // Set Request Policy
        RequestPolicy requestPolicy = new RequestPolicy();
        requestPolicy.setUrlReplacement(eventID);

        CustomXMLRequest<EventResultsModel> countryRequest =
                new CustomXMLRequest<EventResultsModel>(OlympicRequestQueries.EVENT_RESULTS,EventResultsModel.class,createEventSuccessListener(),createEventFailureListener(),requestPolicy);
        VolleySingleton.getInstance(null).addToRequestQueue(countryRequest);
    }

    private Response.ErrorListener createEventFailureListener()
    {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error == "+error);
            }
        };
    }

    private Response.Listener<EventResultsModel> createEventSuccessListener() {
        return new Response.Listener<EventResultsModel>() {
            @Override
            public void onResponse(EventResultsModel response) {

            }
        };
    }
}
