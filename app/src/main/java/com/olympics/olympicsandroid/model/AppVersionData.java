package com.olympics.olympicsandroid.model;

/**
 * Created by tkmagz4 on 7/19/16.
 */
public class AppVersionData implements IResponseModel {

    private float Android;
    private String message;
    private float lastWorkingVersion;

    public float getAndroid() {
        return Android;
    }

    public void setAndroid(float android) {
        Android = android;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public float getLastWorkingVersion() {
        return lastWorkingVersion;
    }

    public void setLastWorkingVersion(float lastWorkingVersion) {
        this.lastWorkingVersion = lastWorkingVersion;
    }
}
