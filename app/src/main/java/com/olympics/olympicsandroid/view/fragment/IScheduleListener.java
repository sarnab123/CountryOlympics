package com.olympics.olympicsandroid.view.fragment;

import com.olympics.olympicsandroid.model.presentationModel.EventResultsViewModel;

/**
 * Created by sarnab.poddar on 7/22/16.
 */
public interface IScheduleListener
{
    public void handleItemClick(EventResultsViewModel itemClicked);
}
