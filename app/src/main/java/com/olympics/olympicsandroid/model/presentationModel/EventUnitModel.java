package com.olympics.olympicsandroid.model.presentationModel;

import java.io.Serializable;

/**
 * Created by sarnab.poddar on 7/10/16.
 */
public class EventUnitModel implements Serializable
{

    public static final byte INDIVIDUAL_EVENT = 0x00;
    public static final byte TEAM_EVENT = 0x01;

    public static final String UNIT_STATUS_SCHEDULED = "scheduled";
    public static final String UNIT_STATUS_INPROGRESS = "inprogress";
    public static final String UNIT_STATUS_NOT_SCHEDULED = "not_scheduled";
    public static final String UNIT_STATUS_CLOSED = "closed";

    public static final byte UNIT_MEDAL_GOLD = 0x00;
    public static final byte UNIT_MEDAL_BRONZE = 0x01;
    public static final byte UNIT_MEDAL_NONE = 0x02;

    private String eventID;
    private String unitName;
    private String eventGender;
    private String eventName;
    private byte eventType;
    private String sportAlias;
    private String disciplineAlias;
    private String unitVenue;
    private String unitStatus;
    private String eventStartTime;
    private byte unitMedalType;
    private String unitID;

    private String parentDisciple;

    public String getDisciplineAlias() {
        return disciplineAlias;
    }

    public void setDisciplineAlias(String disciplineAlias) {
        this.disciplineAlias = disciplineAlias;
    }

    public String getSportAlias() {
        return sportAlias;
    }

    public void setSportAlias(String sportAlias) {
        this.sportAlias = sportAlias;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getUnitID() {
        return unitID;
    }

    public void setUnitID(String unitID)
    {
        this.unitID = unitID;
    }

    public void setParentDisciple(String parentDisciple) {
        this.parentDisciple = parentDisciple;
    }

    public String getParentDisciple() {
        return parentDisciple;
    }

    public String getUnitStatus() {
        return unitStatus;
    }

    public void setUnitStatus(String unitStatus) {
        this.unitStatus = unitStatus;
    }

    public byte getEventType() {
        return eventType;
    }

    public byte getUnitMedalType()
    {
        return unitMedalType;
    }

    public String getEventID() {
        return eventID;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public String getEventGender() {
        return eventGender;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getUnitVenue() {
        return unitVenue;
    }

    public void setEventGender(String eventGender) {
        this.eventGender = eventGender;
    }

    public void setEventType(byte eventType) {
        this.eventType = eventType;
    }

    public void setUnitMedalType(byte unitMedalType) {
        this.unitMedalType = unitMedalType;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public void setUnitVenue(String unitVenue) {
        this.unitVenue = unitVenue;
    }

    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

}
