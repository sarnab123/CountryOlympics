package com.olympics.olympicsandroid.networkLayer.controller;

import com.olympics.olympicsandroid.model.ErrorModel;
import com.olympics.olympicsandroid.model.IResponseModel;

/**
 * Created by sarnab.poddar on 7/8/16.
 */
public interface IUIListener
{

    public void onSuccess(IResponseModel responseModel);
    public void onFailure(ErrorModel errorModel);
    public void handleLoadingIndicator(boolean showLoadingInd);

}
