package com.olympics.olympicsandroid.networkLayer.cache;

import com.olympics.olympicsandroid.model.IResponseModel;

/**
 * Created by sarnab.poddar on 7/14/16.
 */
public interface ICacheListener
{
    public void datafromCache(IResponseModel responseModel);
}
