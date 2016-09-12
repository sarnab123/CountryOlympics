package com.olympics.olympicsandroid.networkLayer.controller;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.olympics.olympicsandroid.model.CountryProfileEvents;
import com.olympics.olympicsandroid.model.ErrorModel;
import com.olympics.olympicsandroid.model.IResponseModel;
import com.olympics.olympicsandroid.model.OlympicSchedule;
import com.olympics.olympicsandroid.model.presentationModel.CountryEventUnitModel;
import com.olympics.olympicsandroid.model.presentationModel.helper.CountryEventsHelper;
import com.olympics.olympicsandroid.networkLayer.CustomXMLRequest;
import com.olympics.olympicsandroid.networkLayer.OlympicRequestQueries;
import com.olympics.olympicsandroid.networkLayer.RequestPolicy;
import com.olympics.olympicsandroid.networkLayer.cache.ICacheListener;
import com.olympics.olympicsandroid.networkLayer.cache.database.OlympicsPrefs;
import com.olympics.olympicsandroid.networkLayer.cache.file.DataCacheHelper;
import com.olympics.olympicsandroid.networkLayer.parse.IParseListener;
import com.olympics.olympicsandroid.networkLayer.parse.ParseTask;
import com.olympics.olympicsandroid.utility.DateUtils;
import com.olympics.olympicsandroid.utility.UtilityMethods;

import java.lang.ref.WeakReference;

/**
 * Created by sarnab.poddar on 7/9/16.
 */
public class CountryProfileController {


    protected WeakReference<IUIListener> listenerWeakReference;
    protected Context mCtx;

    // maintaining a copy since processing on this data needs to happen
    // after schedule call comes back.
    private CountryProfileEvents countryProfileModel;

    public CountryProfileController(WeakReference<IUIListener> listenerWeakReference, Context mCtx) {
        this.listenerWeakReference = listenerWeakReference;
        this.mCtx = mCtx;
    }

    public void getCountryDetails() {
        listenerWeakReference.get().handleLoadingIndicator(true);
        DataCacheHelper.getInstance().getDataModel(DataCacheHelper.CACHE_COUNTRY_MODEL,
                OlympicsPrefs.getInstance(null).getUserSelectedCountry().getAlias(), createNewCacheListener(), false);

    }

    private ICacheListener createNewCacheListener() {
        return new ICacheListener() {
            @Override
            public void datafromCache(IResponseModel responseModel) {
                if (responseModel == null || !(responseModel instanceof CountryEventUnitModel)) {
                    getDataFromServerAndCache();
                } else {
                    if (listenerWeakReference != null && listenerWeakReference.get() != null) {
                        listenerWeakReference.get().handleLoadingIndicator(false);

                        CountryEventUnitModel countryEventData = (CountryEventUnitModel) responseModel;
                        listenerWeakReference.get().onSuccess(countryEventData);
                    }
                }
            }
        };
    }

    private void getDataFromServerAndCache() {
        if (UtilityMethods.isConnectedToInternet()) {
            // Set Request Policy
            RequestPolicy requestPolicy = new RequestPolicy();
            if (DateUtils.isCurrentDateInOlympics()) {
                requestPolicy.setForceCache(true);
                requestPolicy.setMaxAge(60 * 60 * 10);
            }
            requestPolicy.setUrlReplacement(OlympicsPrefs.getInstance(null)
                    .getUserSelectedCountry().getId() + UtilityMethods.COUNTRY_CONFIG);

            if (UtilityMethods.isSimulated) {
                String configString =
                        UtilityMethods.loadDataFromAsset(mCtx,
                                "country_profile.xml");
                ParseTask<CountryProfileEvents> parseTask = new ParseTask<CountryProfileEvents>(CountryProfileEvents.class, configString, new IParseListener() {
                    @Override
                    public void onParseSuccess(Object responseModel) {
                        //TODO: Create common code for simulated/server response
                        if (responseModel != null && responseModel instanceof CountryProfileEvents) {
                            countryProfileModel = (CountryProfileEvents) responseModel;
                            getCompleteSchedule();
                        } else {
                            listenerWeakReference.get().onFailure(null);
                        }

                    }

                    @Override
                    public void onParseFailure(ErrorModel errorModel) {
                        listenerWeakReference.get().onFailure(errorModel);
                    }
                }, ParseTask.XML_DATA);
                parseTask.startParsing();
            } else {
                CustomXMLRequest<CountryProfileEvents> countryRequest = new CustomXMLRequest<CountryProfileEvents>(OlympicRequestQueries.COUNTRY_CONFIG, CountryProfileEvents.class, createCountryProfileSuccessListener(), createCountryProfileFailureListener(), requestPolicy);
                //Get Contry Profile data from firebase storage
                if (countryRequest != null) {
                    countryRequest.getDataFromFireBase();
                }
            }
        } else {
            ErrorModel errorModel = new ErrorModel();
            errorModel.setErrorCode(UtilityMethods.ERROR_INTERNET);
            errorModel.setErrorMessage(UtilityMethods.ERROR_INTERNET);
            listenerWeakReference.get().onFailure(errorModel);
        }
    }

    private void getCompleteSchedule() {
        // Set Request Policy


        if (UtilityMethods.isSimulated) {
            String configString =
                    UtilityMethods.loadDataFromAsset(mCtx,
                            "schedule.xml");
            ParseTask<OlympicSchedule> parseTask = new ParseTask<OlympicSchedule>(OlympicSchedule.class, configString, new IParseListener() {
                @Override
                public void onParseSuccess(Object responseModel) {
                    if (responseModel != null && responseModel instanceof OlympicSchedule) {
                        createCountryEventMapping((OlympicSchedule) responseModel);
                    } else {
                        listenerWeakReference.get().onFailure(null);
                    }
                }

                @Override
                public void onParseFailure(ErrorModel errorModel) {
                    listenerWeakReference.get().onFailure(errorModel);
                }
            }, ParseTask.XML_DATA);
            parseTask.startParsing();
        } else {
            ScheduleController.getInstance().getScheduleData(createScheduleSuccessListener());
        }
    }


    /*
     * Now that we have data for country-profile and schedule ,
     * 1) we need to filter those events where country is participating,
     * 2) generate the list of athletes representing the country.
     * 3) maintain a mapping of date to disciple-event-units
     * 4) Create UI model of the data
     * 5) Cache all the above data
     */
    private void createCountryEventMapping(OlympicSchedule olympicScheduleModel) {
        if (listenerWeakReference != null && listenerWeakReference.get() != null) {
            CountryEventUnitModel countryEventData = new CountryEventsHelper(countryProfileModel,
                    olympicScheduleModel)
                    .createCountryEventUnitModel();
            DataCacheHelper.getInstance().saveDataModel(DataCacheHelper.CACHE_COUNTRY_MODEL, countryEventData);

            listenerWeakReference.get().handleLoadingIndicator(false);

            listenerWeakReference.get().onSuccess(countryEventData);

        }
    }

    protected Response.ErrorListener createCountryProfileFailureListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (listenerWeakReference != null && listenerWeakReference.get() != null) {
                    listenerWeakReference.get().handleLoadingIndicator(false);

                    ErrorModel errorModel = new ErrorModel();
                    errorModel.setErrorCode(error.getLocalizedMessage());
                    errorModel.setErrorMessage(error.getMessage());
                    listenerWeakReference.get().onFailure(errorModel);
                }
            }
        };
    }

    protected Response.Listener<CountryProfileEvents> createCountryProfileSuccessListener() {
        return new Response.Listener<CountryProfileEvents>() {
            @Override
            public void onResponse(CountryProfileEvents responseModel) {
                if (listenerWeakReference != null && listenerWeakReference.get() != null) {
                    if (responseModel != null && responseModel instanceof CountryProfileEvents) {
                        countryProfileModel = (CountryProfileEvents) responseModel;
                        getCompleteSchedule();
                    } else {
                        listenerWeakReference.get().onFailure(null);
                    }
                }
            }
        };
    }

    protected IScheduleListener createScheduleSuccessListener() {
        return new IScheduleListener() {
            @Override
            public void scheduleSuccess(OlympicSchedule olympicSchedule) {
                if (listenerWeakReference != null && listenerWeakReference.get() != null) {
                    listenerWeakReference.get().handleLoadingIndicator(false);

                    if (olympicSchedule != null) {
                        createCountryEventMapping(olympicSchedule);
                    } else {

                        listenerWeakReference.get().onFailure(null);
                    }
                }
            }

            @Override
            public void scheduleError(ErrorModel errorModel) {
                if (listenerWeakReference != null && listenerWeakReference.get() != null) {
                    listenerWeakReference.get().handleLoadingIndicator(false);
                    listenerWeakReference.get().onFailure(errorModel);
                }
            }
        };
    }
}
