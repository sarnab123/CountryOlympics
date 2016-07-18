package com.olympics.olympicsandroid.model.presentationModel;

import com.olympics.olympicsandroid.model.IResponseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sarnab.poddar on 7/17/16.
 */
public class UnitResultsViewModel implements IResponseModel
{

    List<EventResultsViewModel> eventResultsViewModels = new ArrayList<>();

    private int eventType;

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public List<EventResultsViewModel> getEventResultsViewModels() {
        return eventResultsViewModels;
    }

    public void setEventResultsViewModels(List<EventResultsViewModel> eventResultsViewModels) {
        this.eventResultsViewModels = eventResultsViewModels;
    }
}
