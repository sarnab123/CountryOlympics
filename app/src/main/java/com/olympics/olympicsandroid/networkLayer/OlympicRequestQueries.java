package com.olympics.olympicsandroid.networkLayer;

import android.text.TextUtils;

import com.android.volley.Request;

/**
 * Created by sarnab.poddar on 7/9/16.
 */
public enum OlympicRequestQueries
{

    COUNTRY_LIST(Request.Method.GET,"/organization/list.xml", true , true),
    COUNTRY_CONFIG(Request.Method.GET,"/organization/2016/{countryID}/profile.xml", true , true),
    COMPLETE_SCHEDULE(Request.Method.GET,"/2016/schedule.xml", true , true),
    MEDAL_TALLY(Request.Method.GET, "/2016/medals.xml?api_key=5hkjft4mvnbzc26875u6c2zv",true, true);


    private int httpRequestType;
    private String relativeURL;
    private boolean isCacheable;
    private boolean needAPIKey;

    private String baseURL = "https://api.sportradar.us/oly-testing2";

    private OlympicRequestQueries(int httpRequestType, String relativeURL , boolean isCacheable, boolean needAPIKey)
    {
        this.httpRequestType = httpRequestType;
        this.relativeURL = relativeURL;
        this.isCacheable = isCacheable;
        this.needAPIKey = needAPIKey;
    }

    public int getHttpRequestType() {
        return httpRequestType;
    }

    public String getURL(String urlReplacement) {
        StringBuilder finalRequestString = new StringBuilder(baseURL);
        if(!TextUtils.isEmpty(relativeURL)) {
            relativeURL = editURLIfReqd(relativeURL,urlReplacement);
            finalRequestString.append(relativeURL);
        }
        if(needAPIKey)
        {
            finalRequestString.append("?");
            finalRequestString.append("api_key=5hkjft4mvnbzc26875u6c2zv");
        }

        return finalRequestString.toString();
    }

    private String editURLIfReqd(String relativeURL,String urlReplacement) {
        String[] allRelativeURL = relativeURL.split("/");
        String stringToReplace = null;
        for(String data:allRelativeURL)
        {
            if(data.contains("{"))
            {
                stringToReplace = data;
                break;
            }
        }
        relativeURL = relativeURL.replace(stringToReplace, urlReplacement);
        return relativeURL;
    }


}
