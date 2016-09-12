package com.olympics.olympicsandroid.networkLayer.controller;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.olympics.olympicsandroid.model.ErrorModel;
import com.olympics.olympicsandroid.model.OlympicSchedule;
import com.olympics.olympicsandroid.networkLayer.CustomXMLRequest;
import com.olympics.olympicsandroid.networkLayer.OlympicRequestQueries;
import com.olympics.olympicsandroid.networkLayer.RequestPolicy;
import com.olympics.olympicsandroid.utility.DateUtils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by sarnab.poddar on 7/24/16.
 */
public class ScheduleController {
    private static ScheduleController ourInstance = new ScheduleController();

    private boolean isScheduleAPIinProgress = false;

    private OlympicSchedule olympicSchedule;

    private BlockingQueue<IScheduleListener> scheduleListeners;

    public static ScheduleController getInstance() {
        return ourInstance;
    }

    private ScheduleController() {
        olympicSchedule = null;
        scheduleListeners = new ArrayBlockingQueue<IScheduleListener>(10);
    }

    public synchronized void clearScheduleData()
    {
        this.olympicSchedule = null;
    }

    public synchronized void getScheduleData(IScheduleListener scheduleListener) {
        if (olympicSchedule != null && scheduleListener != null) {
            scheduleListener.scheduleSuccess(olympicSchedule);
        } else if (isScheduleAPIinProgress) {
            if(scheduleListener != null) {
                scheduleListeners.offer(scheduleListener);
            }
        } else {
            if(scheduleListener != null) {
                scheduleListeners.offer(scheduleListener);
            }
            isScheduleAPIinProgress = true;
            RequestPolicy requestPolicy = new RequestPolicy();
            if (DateUtils.isCurrentDateInOlympics()) {
                requestPolicy.setForceCache(true);
                requestPolicy.setMaxAge(60 * 60 * 10);
            }
            CustomXMLRequest<OlympicSchedule> scheduleRequest = new CustomXMLRequest<OlympicSchedule>
                    (OlympicRequestQueries.COMPLETE_SCHEDULE, OlympicSchedule.class, createScheduleSuccessListener(), createScheduleFailureListener(), requestPolicy);
            // Get schedule data from firebase storage
            if (scheduleRequest != null) {
                scheduleRequest.getDataFromFireBase();
            }
        }
    }

    private Response.ErrorListener createScheduleFailureListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ErrorModel errorModel = new ErrorModel();
                errorModel.setErrorCode(errorModel.getErrorCode());
                errorModel.setErrorMessage(errorModel.getErrorMessage());

                while(scheduleListeners.size() > 0) {
                    scheduleListeners.poll().scheduleError(errorModel);
                }
                isScheduleAPIinProgress = false;
            }
        };
    }

    private Response.Listener<OlympicSchedule> createScheduleSuccessListener() {
        return new Response.Listener<OlympicSchedule>() {
            @Override
            public void onResponse(OlympicSchedule response) {
                olympicSchedule = response;
                while(scheduleListeners.size() > 0) {
                    scheduleListeners.poll().scheduleSuccess(response);
                }
                isScheduleAPIinProgress = false;
            }
        };
    }
}
