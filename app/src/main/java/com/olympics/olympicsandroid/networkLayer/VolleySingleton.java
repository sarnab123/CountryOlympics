package com.olympics.olympicsandroid.networkLayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by sarnab.poddar on 7/8/16.
 */
public class VolleySingleton
{

    private static VolleySingleton instance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;

    public static synchronized VolleySingleton getInstance(Context mCtx)
    {
        if(instance == null)
        {
            instance = new VolleySingleton(mCtx);
        }

        return instance;
    }

    private VolleySingleton(Context mCtx)
    {
        this.mCtx = mCtx;


        mRequestQueue = generateRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    private RequestQueue generateRequestQueue()
    {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public RequestQueue getmRequestQueue() {
        return mRequestQueue;
    }

    public synchronized  <T> void addToRequestQueue(Request<T> req) {
        generateRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

}
