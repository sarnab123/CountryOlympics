package com.olympics.olympicsandroid.networkLayer.parse;

import com.olympics.olympicsandroid.model.ErrorModel;

/**
 * Created by sarnab.poddar on 7/9/16.
 */
public interface IParseListener<T>
{

    public void onParseSuccess(T responseModel);
    public void onParseFailure(ErrorModel model);
}
