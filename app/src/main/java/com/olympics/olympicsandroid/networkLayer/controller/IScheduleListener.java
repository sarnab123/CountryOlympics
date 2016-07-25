package com.olympics.olympicsandroid.networkLayer.controller;

import com.olympics.olympicsandroid.model.ErrorModel;
import com.olympics.olympicsandroid.model.OlympicSchedule;

/**
 * Created by sarnab.poddar on 7/24/16.
 */
public interface IScheduleListener
{
    public void scheduleSuccess(OlympicSchedule olympicSchedule);
    public void scheduleError(ErrorModel errorModel);

}
