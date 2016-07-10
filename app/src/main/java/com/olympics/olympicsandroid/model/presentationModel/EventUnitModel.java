package com.olympics.olympicsandroid.model.presentationModel;

/**
 * Created by sarnab.poddar on 7/10/16.
 */
public class EventUnitModel
{

    private static final byte INDIVIDUAL_EVENT = 0x00;
    private static final byte TEAM_EVENT = 0x01;

    private static final byte UNIT_STATUS_SCHEDULED = 0x00;
    private static final byte UNIT_STATUS_INPROGRESS = 0x01;
    private static final byte UNIT_STATUS_NOT_SCHEDULED = 0x02;
    private static final byte UNIT_STATUS_CLOSED = 0x03;

    private static final byte UNIT_MEDAL_GOLD = 0x00;
    private static final byte UNIT_MEDAL_BRONZE = 0x01;

    private String eventID;
    private String unitName;
    private String eventGender;
    private byte eventType;
    private String unitVenue;
    private byte unitStatus;
    private long eventStartTime;
    private byte unitMedalType;

    public byte getUnitStatus() {
        return unitStatus;
    }

    public void setUnitStatus(byte unitStatus) {
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

    public long getEventStartTime() {
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

    public void setEventStartTime(long eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

}
