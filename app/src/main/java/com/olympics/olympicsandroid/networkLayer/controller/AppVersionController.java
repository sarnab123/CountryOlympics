package com.olympics.olympicsandroid.networkLayer.controller;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.olympics.olympicsandroid.model.AppVersionData;
import com.olympics.olympicsandroid.model.ErrorModel;
import com.olympics.olympicsandroid.networkLayer.CustomJSONRequest;
import com.olympics.olympicsandroid.networkLayer.OlympicRequestQueries;
import com.olympics.olympicsandroid.networkLayer.RequestPolicy;
import com.olympics.olympicsandroid.networkLayer.VolleySingleton;

/**
 * Created by sarnab.poddar on 7/24/16.
 */
public class AppVersionController
{

    private IConfigListener configListener;

    public void getAppConfiguration(IConfigListener configListener)
    {
        this.configListener = configListener;
        RequestPolicy requestPolicy = new RequestPolicy();

        CustomJSONRequest<AppVersionData> appVersionRequest = new
                CustomJSONRequest<AppVersionData>(OlympicRequestQueries.APP_VERSION_DATA,
                AppVersionData.class, null, createAppVersionDataSuccessListener(),
                createAppVersionDataFailureListener(), requestPolicy);
        VolleySingleton.getInstance(null).addToRequestQueue(appVersionRequest);
    }

    private Response.ErrorListener createAppVersionDataFailureListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorModel errorModel = new ErrorModel();
                errorModel.setErrorCode(error.getMessage());
                errorModel.setErrorMessage(error.getMessage());
                if(configListener != null)
                {
                    configListener.onConfigFailure(errorModel);
                }
            }
        };
    }

    private Response.Listener<AppVersionData> createAppVersionDataSuccessListener() {
        return new Response.Listener<AppVersionData>() {
            @Override
            public void onResponse(AppVersionData response) {
                if(response != null && configListener != null) {
                    configListener.onConfigSuccess(response);
                }
                else if(configListener != null)
                {
                    configListener.onConfigFailure(null);
                }
            }
        };
    }
}
