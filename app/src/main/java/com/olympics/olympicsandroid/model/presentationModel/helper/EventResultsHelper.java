package com.olympics.olympicsandroid.model.presentationModel.helper;

import android.text.TextUtils;

import com.olympics.olympicsandroid.model.EventResultCompetitor;
import com.olympics.olympicsandroid.model.EventResultsModel;
import com.olympics.olympicsandroid.model.OlympicUnit;
import com.olympics.olympicsandroid.model.presentationModel.EventResultsViewModel;
import com.olympics.olympicsandroid.model.presentationModel.UnitResultsViewModel;
import com.olympics.olympicsandroid.networkLayer.cache.database.OlympicsPrefs;
import com.olympics.olympicsandroid.utility.SportsUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sarnab.poddar on 7/17/16.
 */
public class EventResultsHelper {

    private EventResultsModel eventResultsModel;

    public EventResultsHelper(EventResultsModel eventResultsModel) {
        this.eventResultsModel = eventResultsModel;
    }

    /*
     * This is the helper method to fill the UI model.
     *
     * 1) Go through all units of the event results response.
     * 2) For each unit , find whether it is IND/HEADTOHEAD (TEAM AND IND)
     * 3) If Ind/Team then add those competitors who are from the selected country into the List of Competitors within the unit
     * 4) If head2head, then identify the 2 opponents, and if one of them is from the selected country, then set the competitorhead2head object,
     *
     * This seemed very convuluted to me :(
     */
    public UnitResultsViewModel getListOfEventUnits() {

        UnitResultsViewModel unitResultsViewModel = new UnitResultsViewModel();

        List<EventResultsViewModel> eventResultsViewModelList = new ArrayList<>();

        if (eventResultsModel != null && eventResultsModel.getEvent() != null && eventResultsModel.getEvent().getUnits() != null
                && eventResultsModel.getEvent().getUnits().size() > 0) {
            String disciplineName = eventResultsModel.getEvent().getDiscipline().getDescription();
            String sportName = eventResultsModel.getEvent().getSport().getDescription();

            for (OlympicUnit olympicUnit : eventResultsModel.getEvent().getUnits()) {
                EventResultsViewModel eventResultsViewModel = new EventResultsViewModel();

                eventResultsViewModel.setUnit_id(olympicUnit.getId());
                eventResultsViewModel.setUnit_name(olympicUnit.getName());
                eventResultsViewModel.setEventID(eventResultsModel.getEvent().getId());

                eventResultsViewModel.setUnit_type(SportsUtility.getInstance().getTypeofSport(disciplineName, olympicUnit.getName()));
                if (unitResultsViewModel.getEventType() != SportsUtility.TYPE_NOT_SET && unitResultsViewModel.getEventType() != eventResultsViewModel.getUnit_type()) {
                    unitResultsViewModel.setEventType(SportsUtility.TYPE_MIXED);
                } else {
                    unitResultsViewModel.setEventType(eventResultsViewModel.getUnit_type());
                }
                eventResultsViewModel.setUnit_status(SportsUtility.getInstance().getUnitStatus(olympicUnit.getStatus()));

                eventResultsViewModel.setIsDetailsSport(SportsUtility.getInstance().getIsDetailedSport(disciplineName, olympicUnit.getName()));

                eventResultsViewModel.setUnit_medal_type(SportsUtility.getInstance().getMedalType(olympicUnit.getMedal()));

                eventResultsViewModel.setStart_date(olympicUnit.getStart_date());

                eventResultsViewModel.setUnit_scoring_type(SportsUtility.getInstance().getPointType(disciplineName, eventResultsViewModel));

                if (olympicUnit.getResults() != null) {
                    List<EventResultsViewModel.CompetitorViewModel> listOfCompetitors = new ArrayList<>();
                    List<EventResultsViewModel.CompetitorHeadtoHeadViewModel> listOfH2HCompetitors = new ArrayList<>();
                    EventResultsViewModel.CompetitorHeadtoHeadViewModel tempCompetitors = null;

                    if (eventResultsViewModel.getUnit_type() == SportsUtility.TYPE_INDIVUDUAL_HEAD2HEAD || eventResultsViewModel.getUnit_type() == SportsUtility.TYPE_TEAM_HEAD2HEAD) {
                        tempCompetitors = eventResultsViewModel.new CompetitorHeadtoHeadViewModel();
                        tempCompetitors.setUnitID(eventResultsViewModel.getUnit_id());
                    }

                    StringBuilder notes = new StringBuilder();

                    for (EventResultCompetitor competitor : olympicUnit.getResults()) {
                        if (tempCompetitors == null) {
                            EventResultsViewModel.CompetitorViewModel competitorViewModel = eventResultsViewModel.new CompetitorViewModel();
                            if (!TextUtils.isEmpty(competitor.getGender())) {
                                eventResultsViewModel.setUnit_gender(competitor.getGender());
                            }

                            competitorViewModel.setCompetitorName(SportsUtility.getInstance().getCompetitorName(competitor, eventResultsViewModel));

                            competitorViewModel.setCompetitorID(competitor.getId());

                            competitorViewModel.setCountryAlias(competitor.getOrganization());

                            if (competitorViewModel.getCountryAlias().equalsIgnoreCase(OlympicsPrefs.getInstance(null).getUserSelectedCountry().getAlias())) {
                                eventResultsViewModel.setIsSelectedCountry(true);
                            }

                            competitorViewModel.setOutcome(SportsUtility.getInstance().getOutcomeData(competitor, eventResultsViewModel));

                            competitorViewModel.setResult(SportsUtility.getInstance().getResult(disciplineName, eventResultsViewModel, competitor));
                            competitorViewModel.setRank(competitor.getRank());
                            competitorViewModel.setUnit_scoring_type(eventResultsViewModel.getUnit_scoring_type());

                            if (competitorViewModel.getCountryAlias().equalsIgnoreCase(OlympicsPrefs.getInstance(null).getUserSelectedCountry().getAlias())) {
                                listOfCompetitors.add(competitorViewModel);
                            }
                        } else {
                            if (competitor.getOrganization().equalsIgnoreCase
                                    (OlympicsPrefs.getInstance(null).getUserSelectedCountry().getAlias())) {
                                eventResultsViewModel.setIsSelectedCountry(true);
                            }
                            if (tempCompetitors.getCompetitorID() == null) {
                                if (!TextUtils.isEmpty(competitor.getGender())) {
                                    eventResultsViewModel.setUnit_gender(competitor.getGender());
                                }

                                tempCompetitors.setCompetitorName(SportsUtility.getInstance().getCompetitorName(competitor, eventResultsViewModel));

                                tempCompetitors.setCompetitorID(competitor.getId());

                                tempCompetitors.setCountryAlias(competitor.getOrganization());

                                if (tempCompetitors.getCountryAlias().equalsIgnoreCase(OlympicsPrefs.getInstance(null).getUserSelectedCountry().getAlias())) {
                                    eventResultsViewModel.setIsSelectedCountry(true);
                                }

                                tempCompetitors.setOutcome(SportsUtility.getInstance().getOutcomeData(competitor, eventResultsViewModel));

                                if(eventResultsViewModel.isDetailsSport()) {
                                    notes = SportsUtility.getInstance().getNotes(competitor, eventResultsViewModel, notes);
                                }
                                tempCompetitors.setResult(SportsUtility.getInstance().getResult(disciplineName, eventResultsViewModel, competitor));
                                tempCompetitors.setRank(competitor.getRank());

                                tempCompetitors.setUnit_scoring_type(eventResultsViewModel.getUnit_scoring_type());

                            } else {
                                if (!TextUtils.isEmpty(competitor.getGender())) {
                                    eventResultsViewModel.setUnit_gender(competitor.getGender());
                                }

                                tempCompetitors.setUnit_scoring_type(eventResultsViewModel.getUnit_scoring_type());

                                tempCompetitors.setOpp_competitorName(SportsUtility.getInstance().getCompetitorName(competitor, eventResultsViewModel));

                                tempCompetitors.setOpp_competitorID(competitor.getId());

                                tempCompetitors.setOpp_countryAlias(competitor.getOrganization());

                                if(eventResultsViewModel.isDetailsSport()) {
                                    notes = SportsUtility.getInstance().getNotes(competitor, eventResultsViewModel, notes);
                                }
                                if (tempCompetitors.getCountryAlias().equalsIgnoreCase(OlympicsPrefs.getInstance(null).getUserSelectedCountry().getAlias())) {
                                    eventResultsViewModel.setIsSelectedCountry(true);
                                }

                                tempCompetitors.setOpp_outcome(SportsUtility.getInstance().getOutcomeData(competitor, eventResultsViewModel));

                                tempCompetitors.setOpp_result(SportsUtility.getInstance().getResult(disciplineName, eventResultsViewModel, competitor));
                                tempCompetitors.setOpp_rank(competitor.getRank());
                            }
                        }


                    }

                    if(eventResultsViewModel != null && notes != null && eventResultsViewModel.isDetailsSport() && !TextUtils.isEmpty(notes.toString()))
                    {
                        String finalNotes = SportsUtility.getDetailedNotes(notes.toString());
                        if(!TextUtils.isEmpty(finalNotes)) {
                            tempCompetitors.setNotes(finalNotes);
                        }
                    }

                    if (eventResultsViewModel.isSelectedCountry() && tempCompetitors != null) {
                        eventResultsViewModel.setCompetitorH2HViewModel(tempCompetitors);
                    }

                    eventResultsViewModel.setCompetitorViewModelList(listOfCompetitors);

                }

                if (eventResultsViewModel.isSelectedCountry()) {
                    eventResultsViewModelList.add(eventResultsViewModel);
                }
            }
        }


        unitResultsViewModel.setEventResultsViewModels(eventResultsViewModelList);
        return unitResultsViewModel;

    }


}
