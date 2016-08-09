package com.olympics.olympicsandroid.model.presentationModel;

import java.io.Serializable;

/**
 * Created by tkmagz4 on 8/8/16.
 */
public class EventReminder implements Serializable{

    private String eventId;
    private String unitId;
    private String unitName;
    private String unitStartDate;
    private String disciplineName;

    public EventReminder(String eventId, String unitName, String unitStartDate, String unitId,
                         String desciplineName) {
        this.eventId = eventId;
        this.unitName = unitName;
        this.unitStartDate = unitStartDate;
        this.unitId = unitId;
        this.disciplineName = desciplineName;
    }

    public EventReminder () {

    }
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitStartDate() {
        return unitStartDate;
    }

    public void setUnitStartDate(String unitStartDate) {
        this.unitStartDate = unitStartDate;
    }

    public String getDisciplineName() {
        return disciplineName;
    }

    public void setDisciplineName(String disciplineName) {
        this.disciplineName = disciplineName;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }
}
