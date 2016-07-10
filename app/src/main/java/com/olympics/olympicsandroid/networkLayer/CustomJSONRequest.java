package com.olympics.olympicsandroid.networkLayer;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by sarnab.poddar on 7/8/16.
 */
public class CustomJSONRequest<T> extends Request<T> {
    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Map<String, String> headers;
    private final Response.Listener<T> listener;
    private RequestPolicy requestPolicy;

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url URL of the request to make
     * @param clazz Relevant class object, for Gson's reflection
     * @param headers Map of request headers
     * @param requestPolicy Custom cache requestPolicy
     */
    public CustomJSONRequest(OlympicRequestQueries requestQueries, Class<T> clazz, Map<String, String> headers,
                       Response.Listener<T> listener, Response.ErrorListener errorListener, RequestPolicy requestPolicy) {
        super(requestQueries.getHttpRequestType(), requestQueries.getURL(requestPolicy.getUrlReplacement()), errorListener);
        this.clazz = clazz;
        this.headers = headers;
        this.listener = listener;
        this.requestPolicy = requestPolicy;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));

            Cache.Entry entries = HttpHeaderParser.parseCacheHeaders(response);
            if(this.requestPolicy != null && this.requestPolicy.isForceCache())
            {
                long expireTime = System.currentTimeMillis() + this.requestPolicy.getMaxAge() * 1000;
                entries.softTtl = expireTime;
                entries.ttl = expireTime;
            }
            return Response.success(
                    gson.fromJson(json, clazz),entries);
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}
