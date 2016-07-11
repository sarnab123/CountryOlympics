package com.olympics.olympicsandroid.networkLayer.controller;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.olympics.olympicsandroid.model.ErrorModel;
import com.olympics.olympicsandroid.model.IResponseModel;
import com.olympics.olympicsandroid.model.MedalTally;
import com.olympics.olympicsandroid.networkLayer.CustomXMLRequest;
import com.olympics.olympicsandroid.networkLayer.OlympicRequestQueries;
import com.olympics.olympicsandroid.networkLayer.RequestPolicy;
import com.olympics.olympicsandroid.networkLayer.VolleySingleton;
import com.olympics.olympicsandroid.networkLayer.parse.IParseListener;
import com.olympics.olympicsandroid.networkLayer.parse.ParseTask;
import com.olympics.olympicsandroid.utility.UtilityMethods;

import java.lang.ref.WeakReference;


/**
 * Created by sarnab.poddar on 7/8/16.
 */

public class MedalTallyController
{

    protected WeakReference<IUIListener> listenerWeakReference;
    protected Context mCtx;

    public MedalTallyController(WeakReference<IUIListener> listenerWeakReference, Context mCtx)
    {
        this.listenerWeakReference = listenerWeakReference;
        this.mCtx = mCtx;
    }


    public synchronized void  getMedalTallyData()
    {
        // Set Request Policy
        RequestPolicy requestPolicy = new RequestPolicy();
        requestPolicy.setForceCache(true);
        requestPolicy.setMaxAge(60 * 60 * 24);

        if(UtilityMethods.isSimulated)
        {
                String configString =
                        UtilityMethods.loadDataFromAsset(mCtx,
                                "medal_tally.xml");
                ParseTask parseTask = new ParseTask(MedalTally.class, configString, new IParseListener() {
                    @Override
                    public void onParseSuccess(Object responseModel) {
                        listenerWeakReference.get().onSuccess((IResponseModel)responseModel);
                    }

                    @Override
                    public void onParseFailure(ErrorModel errorModel) {
                        listenerWeakReference.get().onFailure(errorModel);
                    }
                },ParseTask.XML_DATA);
                parseTask.startParsing();
        }

        else {
            CustomXMLRequest<MedalTally> medalTallyRequest = new CustomXMLRequest<MedalTally>
                    (OlympicRequestQueries.MEDAL_TALLY, MedalTally.class,
                    createSuccessListener(), createFailureListener() , requestPolicy);
            VolleySingleton.getInstance(null).addToRequestQueue(medalTallyRequest);
        }
    }

    protected Response.ErrorListener createFailureListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorModel errorModel = new ErrorModel();
                errorModel.setErrorCode(error.getLocalizedMessage());
                errorModel.setErrorMessage(error.getMessage());
                listenerWeakReference.get().onFailure(errorModel);
            }
        };
    }

    protected Response.Listener<MedalTally> createSuccessListener() {
        return new Response.Listener<MedalTally>() {
            @Override
            public void onResponse(MedalTally response) {
                listenerWeakReference.get().onSuccess(response);
            }
        };
    }


}
