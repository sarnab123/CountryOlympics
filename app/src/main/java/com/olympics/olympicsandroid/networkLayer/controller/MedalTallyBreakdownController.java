package com.olympics.olympicsandroid.networkLayer.controller;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.olympics.olympicsandroid.model.ErrorModel;
import com.olympics.olympicsandroid.model.IResponseModel;
import com.olympics.olympicsandroid.model.MedalTally;
import com.olympics.olympicsandroid.model.MedalTallyBreakdown;
import com.olympics.olympicsandroid.networkLayer.CustomXMLRequest;
import com.olympics.olympicsandroid.networkLayer.OlympicRequestQueries;
import com.olympics.olympicsandroid.networkLayer.RequestPolicy;
import com.olympics.olympicsandroid.networkLayer.cache.ICacheListener;
import com.olympics.olympicsandroid.utility.UtilityMethods;

import java.lang.ref.WeakReference;


/**
 * Created by sarnab.poddar on 7/8/16.
 */

public class MedalTallyBreakdownController {

    protected WeakReference<IUIListener> listenerWeakReference;
    protected Context mCtx;
    private String selectedCountryId;

    public MedalTallyBreakdownController(WeakReference<IUIListener> listenerWeakReference,
                                         Context mCtx) {
        this.listenerWeakReference = listenerWeakReference;
        this.mCtx = mCtx;

    }

    public synchronized void getMedalTallyData(String selectedCountryId) {
        this.selectedCountryId = selectedCountryId;
        getLatestMedalDataFromServer();
    }

    private void getLatestMedalDataFromServer() {
        if (UtilityMethods.isConnectedToInternet()) {
            // Set Request Policy
            RequestPolicy requestPolicy = new RequestPolicy();
            requestPolicy.setForceCache(true);
            requestPolicy.setMaxAge(60 * 1);
            requestPolicy.setUrlReplacement(selectedCountryId + UtilityMethods.MEDAL_TALLY_BY_ORGANIZATION);

            CustomXMLRequest<MedalTallyBreakdown> medalTallyRequest = new CustomXMLRequest<MedalTallyBreakdown>
                    (OlympicRequestQueries.MEDAL_TALLY_BY_ORGANIZATION, MedalTallyBreakdown.class,
                            createSuccessListener()
                            , createFailureListener(), requestPolicy);
            // Get Medaltally breakdown data from firebase storage
            if (medalTallyRequest != null) {
                medalTallyRequest.getDataFromFireBase();
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

    private ICacheListener createNewCacheListener() {
        return new ICacheListener() {
            @Override
            public void datafromCache(IResponseModel responseModel) {
                if (responseModel != null && (responseModel instanceof MedalTally)) {
                    MedalTally medalTallyData = (MedalTally) responseModel;
                    if (listenerWeakReference != null && listenerWeakReference.get() != null) {

                        listenerWeakReference.get().onSuccess(medalTallyData);
                    }
                }
            }
        };
    }

    protected Response.ErrorListener createFailureListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorModel errorModel = new ErrorModel();
                errorModel.setErrorCode(error.getLocalizedMessage());
                errorModel.setErrorMessage(error.getMessage());
                if (listenerWeakReference != null && listenerWeakReference.get() != null) {

                    listenerWeakReference.get().onFailure(errorModel);
                }
            }
        };
    }

    protected Response.Listener<MedalTallyBreakdown> createSuccessListener() {
        return new Response.Listener<MedalTallyBreakdown>() {
            @Override
            public void onResponse(MedalTallyBreakdown response) {
                if (listenerWeakReference != null && listenerWeakReference.get() != null) {
                    listenerWeakReference.get().onSuccess(response);
                }
            }
        };
    }
}
