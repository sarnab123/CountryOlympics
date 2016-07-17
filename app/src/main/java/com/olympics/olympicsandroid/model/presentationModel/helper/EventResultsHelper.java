package com.olympics.olympicsandroid.model.presentationModel.helper;

import com.olympics.olympicsandroid.model.EventResultsModel;
import com.olympics.olympicsandroid.model.OlympicUnit;
import com.olympics.olympicsandroid.model.presentationModel.EventResultsViewModel;
import com.olympics.olympicsandroid.utility.SportsUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sarnab.poddar on 7/17/16.
 */
public class EventResultsHelper
{

    private EventResultsModel eventResultsModel;

    public EventResultsHelper(EventResultsModel eventResultsModel)
    {
        this.eventResultsModel = eventResultsModel;
    }

    public List<EventResultsViewModel> getListOfEventUnits()
    {

        List<EventResultsViewModel> eventResultsViewModels = new ArrayList<>();

        if(eventResultsModel != null && eventResultsModel.getEvent() != null && eventResultsModel.getEvent().getUnits() != null
                && eventResultsModel.getEvent().getUnits().size() > 0)
        {
            String disciplineName = eventResultsModel.getEvent().getDiscipline().getDescription();

            for(OlympicUnit olympicUnit:eventResultsModel.getEvent().getUnits())
            {
                EventResultsViewModel eventResultsViewModel = new EventResultsViewModel();

                eventResultsViewModel.setUnit_id(olympicUnit.getId());
                eventResultsViewModel.setUnit_name(olympicUnit.getName());

                eventResultsViewModel.setUnit_type(SportsUtility.getInstance().getTypeofSport(disciplineName,olympicUnit.getName()));
            }
        }

        return eventResultsViewModels;

    }


}
