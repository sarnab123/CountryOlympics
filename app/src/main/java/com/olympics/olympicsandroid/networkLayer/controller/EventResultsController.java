package com.olympics.olympicsandroid.networkLayer.controller;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.olympics.olympicsandroid.model.ErrorModel;
import com.olympics.olympicsandroid.model.EventResultsModel;
import com.olympics.olympicsandroid.model.presentationModel.UnitResultsViewModel;
import com.olympics.olympicsandroid.model.presentationModel.helper.EventResultsHelper;
import com.olympics.olympicsandroid.networkLayer.CustomXMLRequest;
import com.olympics.olympicsandroid.networkLayer.OlympicRequestQueries;
import com.olympics.olympicsandroid.networkLayer.RequestPolicy;
import com.olympics.olympicsandroid.utility.UtilityMethods;

import java.lang.ref.WeakReference;

/**
 * Created by sarnab.poddar on 7/16/16.
 */
public class EventResultsController {
    protected WeakReference<IUIListener> listenerWeakReference;
    protected Context mCtx;

    public EventResultsController(WeakReference<IUIListener> listenerWeakReference, Context mCtx) {
        this.listenerWeakReference = listenerWeakReference;
        this.mCtx = mCtx;
    }

    public void getEventResults(String eventID) {
        if (UtilityMethods.isConnectedToInternet()) {
            // Set Request Policy
            RequestPolicy requestPolicy = new RequestPolicy();
            requestPolicy.setUrlReplacement(eventID + UtilityMethods.EVENT_RESULTS);
            if (listenerWeakReference != null && listenerWeakReference.get() != null) {

                listenerWeakReference.get().handleLoadingIndicator(true);
            }
            CustomXMLRequest<EventResultsModel> eventresultRequest =
                    new CustomXMLRequest<EventResultsModel>(OlympicRequestQueries.EVENT_RESULTS, EventResultsModel.class, createEventSuccessListener(), createEventFailureListener(), requestPolicy);
            //Get Event Result data from firebase storage
            if (eventresultRequest != null) {
                eventresultRequest.getDataFromFireBase();
            }
        } else {
            ErrorModel errorModel = new ErrorModel();
            errorModel.setErrorCode(UtilityMethods.ERROR_INTERNET);
            errorModel.setErrorMessage(UtilityMethods.ERROR_INTERNET);
            if (listenerWeakReference != null && listenerWeakReference.get() != null) {

                listenerWeakReference.get().onFailure(errorModel);
            }
        }
    }

    private Response.ErrorListener createEventFailureListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (listenerWeakReference != null && listenerWeakReference.get() != null) {
                    listenerWeakReference.get().handleLoadingIndicator(false);
                    ErrorModel errorModel = new ErrorModel();
                    errorModel.setErrorCode(error.getLocalizedMessage());
                    errorModel.setErrorMessage(error.getMessage());
                    listenerWeakReference.get().onFailure(errorModel);
                    System.out.println("error == " + error);
                }
            }
        };
    }

    private Response.Listener<EventResultsModel> createEventSuccessListener() {
        return new Response.Listener<EventResultsModel>() {
            @Override
            public void onResponse(EventResultsModel response) {
                EventResultsHelper eventResultsHelper = new EventResultsHelper(response);

                UnitResultsViewModel unitResultsViewModel = eventResultsHelper.getListOfEventUnits();
                if (listenerWeakReference != null && listenerWeakReference.get() != null) {

                    listenerWeakReference.get().handleLoadingIndicator(false);
                    listenerWeakReference.get().onSuccess(unitResultsViewModel);
                }
            }
        };
    }

}
