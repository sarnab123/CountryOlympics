package com.olympics.olympicsandroid.model.presentationModel.helper;

import android.text.TextUtils;

import com.olympics.olympicsandroid.model.CompetitorVOModel;
import com.olympics.olympicsandroid.model.CountryProfileEvents;
import com.olympics.olympicsandroid.model.OlympicAthlete;
import com.olympics.olympicsandroid.model.OlympicDiscipline;
import com.olympics.olympicsandroid.model.OlympicEvent;
import com.olympics.olympicsandroid.model.OlympicSchedule;
import com.olympics.olympicsandroid.model.OlympicSport;
import com.olympics.olympicsandroid.model.OlympicTeams;
import com.olympics.olympicsandroid.model.OlympicUnit;
import com.olympics.olympicsandroid.model.presentationModel.Athlete;
import com.olympics.olympicsandroid.model.presentationModel.CountryEventUnitModel;
import com.olympics.olympicsandroid.model.presentationModel.DateSportsModel;
import com.olympics.olympicsandroid.model.presentationModel.EventUnitModel;
import com.olympics.olympicsandroid.networkLayer.cache.database.DBCompetitorRelationHelper;
import com.olympics.olympicsandroid.networkLayer.cache.database.iFace.DBTablesDef;
import com.olympics.olympicsandroid.utility.DateUtils;
import com.olympics.olympicsandroid.utility.SportsUtility;
import com.olympics.olympicsandroid.view.activity.AthleteActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * This helper maps CountryProfileEvents and OlympicSchedule models into CountryEventUnitModel
 * <p/>
 * Created by sarnab.poddar on 7/10/16.
 */
public class CountryEventsHelper {

    private CountryProfileEvents countryProfileEvents;
    private OlympicSchedule olympicSchedule;


    public CountryEventsHelper(CountryProfileEvents countryProfileEvents, OlympicSchedule
            olympicSchedule) {
        // countryProfileEvents will have all events for a given country
        this.countryProfileEvents = countryProfileEvents;
        // olympicSchedule will hold all schedules for all countries
        this.olympicSchedule = olympicSchedule;
    }

    /*
     * The aim of this method is to create a CountryEventUnitModel = which will have the list of
     * unique athletes participating for that country.
     *  and also events #DateSportsModel sorted by date.
     */
    public CountryEventUnitModel createCountryEventUnitModel() {

        CountryEventUnitModel countryEventUnitModel = new CountryEventUnitModel();

        countryEventUnitModel.setCountryAlias(countryProfileEvents.getOrganization().getAlias());
        countryEventUnitModel.setCountryName(countryProfileEvents.getOrganization()
                .getDescription());
        countryEventUnitModel.setCountryID(countryProfileEvents.getOrganization().getId());


        // initialize mapping and then update with data
        countryEventUnitModel.initializeEmptyDateSportsMapping();
        updateDateSportsMapping(countryEventUnitModel);

        return countryEventUnitModel;
    }


    /**
     * @return map of <Unit_start_date,
     */
    private void updateDateSportsMapping(CountryEventUnitModel countryEventUnitModel) {


        Map<String, DateSportsModel> dateSportsMapping = countryEventUnitModel.getDatesCountryMapping();

        Map<String, OlympicEvent> allEventsMap = populateEventMapFromAllEventsSchedule();

        HashSet<Athlete> athleteList = new HashSet<>();
        List<CompetitorVOModel> listOfCompetitors = new ArrayList<>();


        // Get each event that selected country is participating in
        for (OlympicEvent participatingEvent : countryProfileEvents.getOrganization().getEvents()) {
            if (participatingEvent == null) {
                continue;
            }

            OlympicEvent scheduledParticipatingEvent = allEventsMap.get(participatingEvent.getId());
            //Get all the units for the given event.
            if (scheduledParticipatingEvent == null || scheduledParticipatingEvent.getUnits() == null || scheduledParticipatingEvent
                    .getUnits().isEmpty()) {
                continue;
            }

            // indivudual participants
            List<OlympicAthlete> participantList = participatingEvent.getParticipants();

            if (participantList != null) {
                for (OlympicAthlete participant : participantList) {
                    if (participant == null) {
                        continue;
                    }
                    Athlete athlete = new Athlete();

                    athlete.setCompetitorID(participant.getId());
                    athlete.insertIntoEventSet(scheduledParticipatingEvent.getId());
                    CompetitorVOModel competitorVOModel = new CompetitorVOModel();
                    competitorVOModel.setCompetitorID(participant.getId());
                    competitorVOModel.setOrgAlias(countryEventUnitModel.getCountryAlias());

                    if (!TextUtils.isEmpty(participant.getPrint_name())) {
                        athlete.setAthleteName(participant.getPrint_name());
                    } else {
                        String firstName = TextUtils.isEmpty(participant.getFirst_name()) ? "" : participant.getFirst_name();

                        String lastName = TextUtils.isEmpty(participant.getLast_name()) ? "" : participant.getLast_name();
                        StringBuilder stringBuilder = new StringBuilder(firstName).append(" ").append(lastName);
                        athlete.setAthleteName(stringBuilder.toString());
                    }
                    if (!TextUtils.isEmpty(participant.getGender()) && participant.getGender().equalsIgnoreCase(AthleteActivity.NO_GENDER)) {
                        athlete.setAthleteGender(participatingEvent.getGender());
                    } else {
                        athlete.setAthleteGender(participant.getGender());
                    }
                    if (participatingEvent.getSport() != null) {
                        athlete.setSportName(participatingEvent.getSport().getDescription());
                        athlete.setSportsAlias(participatingEvent.getSport().getAlias());
                    }

                    if(!TextUtils.isEmpty(participatingEvent.getDescription()))
                    {
                        athlete.setDisciplineName(participatingEvent.getDescription());
                    }

                    athleteList.add(athlete);
                    competitorVOModel.setCompetitorName(athlete.getAthleteName());

                    listOfCompetitors.add(competitorVOModel);
                }
            }

            // team participants
            List<OlympicTeams> teamList = participatingEvent.getTeams();
            if (teamList != null) {
                for (OlympicTeams olympicTeams : teamList) {
                    if (olympicTeams != null && olympicTeams.getAthlete() != null && olympicTeams.getAthlete().size() > 0) {

                        CompetitorVOModel competitorVOModel = new CompetitorVOModel();
                        competitorVOModel.setCompetitorID(olympicTeams.getId());
                        competitorVOModel.setOrgAlias(countryEventUnitModel.getCountryAlias());

                        int athleteCount = 0;
                        StringBuilder competitorName = new StringBuilder();
                        for (OlympicAthlete participant : olympicTeams.getAthlete()) {
                            athleteCount++;
                            Athlete athlete = new Athlete();

                            athlete.setCompetitorID(olympicTeams.getId());
                            athlete.insertIntoEventSet(scheduledParticipatingEvent.getId());

                            if (!TextUtils.isEmpty(participant.getPrint_name())) {
                                athlete.setAthleteName(participant.getPrint_name());
                                if (competitorName.length() == 0) {
                                    competitorName.append(participant.getPrint_name());
                                } else {
                                    competitorName.append("/");
                                    competitorName.append(participant.getPrint_name());
                                }
                            } else {

                                String firstName = TextUtils.isEmpty(participant.getFirst_name()) ? "" : participant.getFirst_name();
                                String lastName = TextUtils.isEmpty(participant.getLast_name()) ? "" : participant.getLast_name();
                                if (competitorName.length() == 0) {
                                    competitorName.append(lastName);
                                } else {
                                    competitorName.append("/");
                                    competitorName.append(lastName);
                                }
                                StringBuilder stringBuilder = new StringBuilder(firstName).append
                                        (" ").append(lastName);
                                athlete.setAthleteName(stringBuilder.toString());
                            }

                            if (!TextUtils.isEmpty(participant.getGender()) && participant.getGender().equalsIgnoreCase(AthleteActivity.NO_GENDER)) {
                                athlete.setAthleteGender(participatingEvent.getGender());
                            } else {
                                athlete.setAthleteGender(participant.getGender());
                            }
                            if (participatingEvent.getSport() != null) {
                                athlete.setSportName(participatingEvent.getSport().getDescription());
                                athlete.setSportsAlias(participatingEvent.getDiscipline().getAlias());
                            }

                            if(!TextUtils.isEmpty(participatingEvent.getDescription()))
                            {
                                athlete.setDisciplineName(participatingEvent.getDescription());
                            }

                            athleteList.add(athlete);
                        }
                        if (athleteCount <= 2) {
                            competitorVOModel.setCompetitorName(competitorName.toString());
                            listOfCompetitors.add(competitorVOModel);
                        }
                    }
                }

            }




            for (OlympicUnit olympicEventUnit : scheduledParticipatingEvent.getUnits()) {
                if (olympicEventUnit == null || olympicEventUnit.getStart_date() == null || !TextUtils.isEmpty(olympicEventUnit.getPhase()) && olympicEventUnit.getPhase().equalsIgnoreCase("training")) {
                    continue;
                }

                String unitStartDate = DateUtils.getDateTimeInMillis(olympicEventUnit
                        .getStart_date());

                DateSportsModel dateSportsModel = dateSportsMapping.get(unitStartDate);

                if (dateSportsModel == null) {
                    continue;
                }


                dateSportsModel.setDateString(Long.parseLong(unitStartDate));
                dateSportsMapping.put(unitStartDate, dateSportsModel);

                //Set sports
                Map<String, DateSportsModel.SportsEventsUnits> sportsEventsUnits =
                        dateSportsModel.getAllSportsForDate();

                if (sportsEventsUnits == null) {
                    sportsEventsUnits = new HashMap<>();
                    dateSportsModel.setAllSportsForDate(sportsEventsUnits);
                }

                DateSportsModel.SportsEventsUnits sportsEventsUnit = sportsEventsUnits.get
                        (participatingEvent.getSport().getDescription());

                if (sportsEventsUnit == null) {
                    sportsEventsUnit = new DateSportsModel.SportsEventsUnits();
                }
                sportsEventsUnit.setSportsTitle(participatingEvent.getSport().getDescription());
                sportsEventsUnits.put(participatingEvent.getSport().getDescription(),
                        sportsEventsUnit);


                List<EventUnitModel> eventUnitModelList = sportsEventsUnit.getEventUnits();
                if (eventUnitModelList == null) {
                    eventUnitModelList = new ArrayList<>();
                }
                sportsEventsUnit.setEventUnits(eventUnitModelList);


                eventUnitModelList.add(populateEventUnitData(olympicEventUnit, participatingEvent));
            }
        }

        if (listOfCompetitors.size() > 0) {
            DBCompetitorRelationHelper dbCompetitorRelationHelper = new DBCompetitorRelationHelper();
            dbCompetitorRelationHelper.insertAll(DBTablesDef.T_COMPETITOR_RELATION,listOfCompetitors);
        }
        countryEventUnitModel.setDatesCountryMapping(dateSportsMapping);
        countryEventUnitModel.setAthleteList(athleteList);
    }

    /**
     * Populates map of <EventID, Event> from schedule API.
     * This will hold all events for all countries
     *
     * @return
     */
    private Map<String, OlympicEvent> populateEventMapFromAllEventsSchedule() {

        Map<String, OlympicEvent> eventFromSchedule = new HashMap<>();

        // Populate all the events from Schedule list
        for (OlympicSport scheduledSports : olympicSchedule.getSeasonSchedule().getSports()) {
            for (OlympicDiscipline olympicDiscipline : scheduledSports.getDisciplines()) {
                for (OlympicEvent olympicEvent : olympicDiscipline.getEvents()) {
                    eventFromSchedule.put(olympicEvent.getId(), olympicEvent);
                }
            }
        }
        return eventFromSchedule;
    }

    private EventUnitModel populateEventUnitData(OlympicUnit olympicUnit, OlympicEvent
            olympicEvent) {

        EventUnitModel eventUnitModel = new EventUnitModel();
        eventUnitModel.setEventID(olympicEvent.getId());
        eventUnitModel.setSportAlias(olympicEvent.getSport().getAlias());
        eventUnitModel.setDisciplineAlias(olympicEvent.getDiscipline().getAlias());

        eventUnitModel.setUnitName(olympicUnit.getName());
        eventUnitModel.setEventStartTime(olympicUnit.getStart_date
                ());
        eventUnitModel.setEventGender(olympicEvent.getGender());
        eventUnitModel.setEventName(olympicEvent.getDescription());
        if (olympicEvent.getTeams() != null && olympicEvent.getTeams().size() > 0) {
            eventUnitModel.setEventType(EventUnitModel.TEAM_EVENT);
        } else {
            eventUnitModel.setEventType(EventUnitModel.INDIVIDUAL_EVENT);
        }

        if (olympicEvent.getDiscipline() != null) {
            eventUnitModel.setParentDisciple(olympicEvent.getDiscipline().getDescription());
        }

        if (!TextUtils.isEmpty(olympicUnit.getStatus())) {
            eventUnitModel.setUnitStatus(SportsUtility.getInstance().getUnitStatus(olympicUnit.getStatus()));
        }

        eventUnitModel.setUnitVenue(olympicUnit.getVenue().getName());
        eventUnitModel.setUnitID(olympicUnit.getId());
        eventUnitModel.setUnitMedalType(SportsUtility.getInstance().getMedalType(olympicUnit.getMedal()));

        return eventUnitModel;
    }

}
