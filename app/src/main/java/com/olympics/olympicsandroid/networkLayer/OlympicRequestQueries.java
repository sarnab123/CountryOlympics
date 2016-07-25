package com.olympics.olympicsandroid.networkLayer;

import android.text.TextUtils;

import com.android.volley.Request;
import com.olympics.olympicsandroid.networkLayer.cache.database.OlympicsPrefs;

/**
 * Created by sarnab.poddar on 7/9/16.
 */
public enum OlympicRequestQueries
{

    COUNTRY_LIST(Request.Method.GET,null,"/organization/list.xml", true , true),
    COUNTRY_CONFIG(Request.Method.GET,null,"/organization/2016/{countryID}/profile.xml", true , true),
    COMPLETE_SCHEDULE(Request.Method.GET,null,"/2016/schedule.xml", true , true),
    MEDAL_TALLY(Request.Method.GET, null,"/2016/medals.xml",true, true),
    EVENT_RESULTS(Request.Method.GET,null,"/event/{eventID}/results.xml", true , true),
    SPORTS_TYPE(Request.Method.GET, "https://olympics.mybluemix.net/config/getSportsData", null,
                true, true),
    APP_VERSION_DATA(Request.Method.GET, "https://olympics.mybluemix.net/config/getAppVersion",
            null,
            true, false);

    private int httpRequestType;
    private String relativeURL;
    private boolean isCacheable;
    private boolean needAPIKey;

    private String completeURL;

    private String apiKey = "5hkjft4mvnbzc26875u6c2zv";
    private String baseURL = "https://api.sportradar.us/oly-testing2";

//    OlympicRequestQueries(int httpRequestType, String relativeURL , boolean isCacheable, boolean needAPIKey)
//    {
//        this.httpRequestType = httpRequestType;
//        this.relativeURL = relativeURL;
//        this.isCacheable = isCacheable;
//        this.needAPIKey = needAPIKey;
//
//    }

    OlympicRequestQueries(int httpRequestType, String completeURL, String relativeURL , boolean
            isCacheable,boolean needAPIKey)
    {
        this.completeURL = completeURL;
        this.httpRequestType = httpRequestType;
        this.relativeURL = relativeURL;
        this.isCacheable = isCacheable;
        this.needAPIKey = needAPIKey;
    }

    public int getHttpRequestType() {
        return httpRequestType;
    }

    public String getURL(String urlReplacement) {
        if(!TextUtils.isEmpty(completeURL))
        {
            return completeURL;
        }
        String newBaseURL = TextUtils.isEmpty(OlympicsPrefs.getInstance(null).getBaseURL())?baseURL:OlympicsPrefs.getInstance(null).getBaseURL();
        StringBuilder finalRequestString = new StringBuilder(newBaseURL);
        if(!TextUtils.isEmpty(relativeURL)) {
            String relativeTempURL = new String(relativeURL);
            relativeTempURL = editURLIfReqd(relativeTempURL,urlReplacement);
            finalRequestString.append(relativeTempURL);
        }
        if(needAPIKey)
        {
            finalRequestString.append("?");
            String newAPIkey = TextUtils.isEmpty(OlympicsPrefs.getInstance(null).getAPIKey())?apiKey:OlympicsPrefs.getInstance(null).getAPIKey();
            finalRequestString.append("api_key=" + newAPIkey);
        }

        return finalRequestString.toString();
    }

    private String editURLIfReqd(String relativeTempURL,String urlReplacement) {
        String[] allRelativeURL = relativeTempURL.split("/");
        String stringToReplace = null;
        for(String data:allRelativeURL)
        {
            if(data.contains("{"))
            {
                stringToReplace = data;
                break;
            }
        }
        if(!TextUtils.isEmpty(stringToReplace) && !TextUtils.isEmpty(urlReplacement)) {
            relativeTempURL = relativeTempURL.replace(stringToReplace, urlReplacement);
        }
        return relativeTempURL;
    }


}
