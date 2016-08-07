package com.olympics.olympicsandroid.model;

/**
 * Created by tkmagz4 on 7/19/16.
 */
public class AppVersionData implements IResponseModel {

    private float Android;
    private String message;
    private float lastWorkingVersion;
    private String baseURL;
    private String apiKey;
    private String cacheConfigDate;

    private String isAdsEnabled;
    private String hardCheckDate;

    private String cacheCountryChecksum;
    private String onDemandCountryAlias;

    public String getIsAdsEnabled() {
        return isAdsEnabled;
    }

    public void setIsAdsEnabled(String isAdsEnabled) {
        this.isAdsEnabled = isAdsEnabled;
    }

    public String getHardCheckDate() {
        return hardCheckDate;
    }

    public void setHardCheckDate(String hardCheckDate) {
        this.hardCheckDate = hardCheckDate;
    }

    public String getOnDemandCountryAlias() {
        return onDemandCountryAlias;
    }

    public void setCacheCountryChecksum(String cacheCountryChecksum) {
        this.cacheCountryChecksum = cacheCountryChecksum;
    }

    public String getCacheCountryChecksum() {
        return cacheCountryChecksum;
    }

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

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getCacheConfigDate() {
        return cacheConfigDate;
    }

    public void setCacheConfigDate(String cacheConfigDate) {
        this.cacheConfigDate = cacheConfigDate;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }
}
