package com.olympics.olympicsandroid.networkLayer.controller;

import com.olympics.olympicsandroid.model.AppVersionData;
import com.olympics.olympicsandroid.model.ErrorModel;

/**
 * Created by sarnab.poddar on 7/24/16.
 */
public interface IConfigListener
{

    public void onConfigSuccess(AppVersionData appVersionData);
    public void onConfigFailure(ErrorModel errorModel);
}
