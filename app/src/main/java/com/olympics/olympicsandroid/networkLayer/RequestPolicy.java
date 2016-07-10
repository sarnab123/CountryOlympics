package com.olympics.olympicsandroid.networkLayer;

/**
 * Created by sarnab.poddar on 7/9/16.
 */
public class RequestPolicy
{

    // Even if server does not push max-age for Cache Control, do you still want to cache?
    private boolean forceCache;

    //If @forceCache is true, then what is the maxage in seconds
    private long maxAge;

    //If URL needs to be updated to replace substring {xxx}
    private String urlReplacement;


    public boolean isForceCache() {
        return forceCache;
    }

    public long getMaxAge() {
        return maxAge;
    }

    public void setForceCache(boolean forceCache) {
        this.forceCache = forceCache;
    }

    public void setMaxAge(long maxAge) {
        this.maxAge = maxAge;
    }

    public String getUrlReplacement() {
        return urlReplacement;
    }

    public void setUrlReplacement(String urlReplacement) {
        this.urlReplacement = urlReplacement;
    }
}
