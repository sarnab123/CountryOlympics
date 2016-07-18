package com.olympics.olympicsandroid.model.presentationModel.helper;

import android.text.TextUtils;

import com.olympics.olympicsandroid.model.EventResultCompetitor;
import com.olympics.olympicsandroid.model.EventResultsModel;
import com.olympics.olympicsandroid.model.OlympicUnit;
import com.olympics.olympicsandroid.model.presentationModel.EventResultsViewModel;
import com.olympics.olympicsandroid.model.presentationModel.UnitResultsViewModel;
import com.olympics.olympicsandroid.networkLayer.cache.database.OlympicsPrefs;
import com.olympics.olympicsandroid.utility.DateUtils;
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

    public UnitResultsViewModel getListOfEventUnits()
    {

        UnitResultsViewModel unitResultsViewModel = new UnitResultsViewModel();

        List<EventResultsViewModel> eventResultsViewModelList = new ArrayList<>();

        if(eventResultsModel != null && eventResultsModel.getEvent() != null && eventResultsModel.getEvent().getUnits() != null
                && eventResultsModel.getEvent().getUnits().size() > 0)
        {
            String disciplineName = eventResultsModel.getEvent().getDiscipline().getDescription();
            String sportName = eventResultsModel.getEvent().getSport().getDescription();

            for(OlympicUnit olympicUnit:eventResultsModel.getEvent().getUnits())
            {
                EventResultsViewModel eventResultsViewModel = new EventResultsViewModel();

                eventResultsViewModel.setUnit_id(olympicUnit.getId());
                eventResultsViewModel.setUnit_name(olympicUnit.getName());

                eventResultsViewModel.setUnit_type(SportsUtility.getInstance().getTypeofSport(disciplineName, olympicUnit.getName()));

                eventResultsViewModel.setUnit_status(SportsUtility.getInstance().getUnitStatus(olympicUnit.getStatus()));

                eventResultsViewModel.setUnit_medal_type(SportsUtility.getInstance().getMedalType(olympicUnit.getMedal()));

                eventResultsViewModel.setStart_date(DateUtils.getDateTimeInMillis(olympicUnit.getStart_date()));

                if(olympicUnit.getResults() != null)
                {
                    List<EventResultsViewModel.CompetitorViewModel> listOfCompetitors = new ArrayList<>();
                    for(EventResultCompetitor competitor:olympicUnit.getResults())
                    {

                        EventResultsViewModel.CompetitorViewModel competitorViewModel = eventResultsViewModel.new CompetitorViewModel();
                        if(!TextUtils.isEmpty(competitor.getGender()))
                        {
                            eventResultsViewModel.setUnit_gender(competitor.getGender());
                        }

                        competitorViewModel.setCompetitorName(SportsUtility.getInstance().getCompetitorName(competitor, eventResultsViewModel));

                        competitorViewModel.setCompetitorID(competitor.getId());

                        competitorViewModel.setCountryAlias(competitor.getOrganization());

                        if(competitorViewModel.getCountryAlias().equalsIgnoreCase(OlympicsPrefs.getInstance(null).getUserSelectedCountry().getAlias()))
                        {
                            eventResultsViewModel.setIsSelectedCountry(true);
                        }

                        competitorViewModel.setOutcome(SportsUtility.getInstance().getOutcomeData(competitor, eventResultsViewModel));

                        competitorViewModel.setResult(SportsUtility.getInstance().getResult(disciplineName, eventResultsViewModel, competitor));
                        competitorViewModel.setRank(competitor.getRank());

                        listOfCompetitors.add(competitorViewModel);

                    }

                    eventResultsViewModel.setCompetitorViewModelList(listOfCompetitors);
                    eventResultsViewModel.setUnit_scoring_type(SportsUtility.getInstance().getPointType(disciplineName, eventResultsViewModel));
                }

                eventResultsViewModelList.add(eventResultsViewModel);
            }
        }

        unitResultsViewModel.setEventResultsViewModels(eventResultsViewModelList);
        return unitResultsViewModel;

    }


    public UnitResultsViewModel cacheAndFilter(UnitResultsViewModel listOfEventUnits) {

        List<EventResultsViewModel> allResultModels = listOfEventUnits.getEventResultsViewModels();

        List<EventResultsViewModel> filteredResultModels = new ArrayList<>();

        for(EventResultsViewModel eventResultsViewModel: allResultModels)
        {
            if(eventResultsViewModel.isSelectedCountry())
            {
                    filteredResultModels.add(eventResultsViewModel);
            }
        }

        UnitResultsViewModel unitResultsViewModel = new UnitResultsViewModel();
        unitResultsViewModel.setEventResultsViewModels(filteredResultModels);

        return unitResultsViewModel ;
    }
}
