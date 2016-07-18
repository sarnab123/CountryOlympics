package com.olympics.olympicsandroid.view.activity.eventActivities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.olympics.olympicsandroid.R;
import com.olympics.olympicsandroid.model.ErrorModel;
import com.olympics.olympicsandroid.model.IResponseModel;
import com.olympics.olympicsandroid.model.presentationModel.EventUnitModel;
import com.olympics.olympicsandroid.model.presentationModel.UnitResultsViewModel;
import com.olympics.olympicsandroid.networkLayer.cache.database.DBUnitStatusHelper;
import com.olympics.olympicsandroid.networkLayer.controller.EventResultsController;
import com.olympics.olympicsandroid.networkLayer.controller.IUIListener;

import java.lang.ref.WeakReference;

/**
 * Created by sarnab.poddar on 7/16/16.
 */
public class EventActivity extends Activity implements IUIListener
{
    private String eventID;
    private String unitID;
    private String unitName;
    private String disciplineName;
    private long eventStartTime;

    RecyclerView eventunitView;

    private EventResultsController eventResultsController;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_results);

        if(!TextUtils.isEmpty(getIntent().getStringExtra("event_id")))
        {
            eventID = getIntent().getStringExtra("event_id");
        }
        if(!TextUtils.isEmpty(getIntent().getStringExtra("event_unit_name")))
        {
            unitName = getIntent().getStringExtra("event_unit_name");
        }
        if(!TextUtils.isEmpty(getIntent().getStringExtra("discipline_name")))
        {
            disciplineName = getIntent().getStringExtra("discipline_name");
        }
        if(!TextUtils.isEmpty(getIntent().getStringExtra("event_unit_id")))
        {
            unitID = getIntent().getStringExtra("event_unit_id");
        }
            eventStartTime = getIntent().getLongExtra("event_date", 0L);
        eventunitView = (RecyclerView)findViewById(R.id.event_list);

        mLayoutManager = new LinearLayoutManager(this);
        eventunitView.setPadding(10, 10, 10, 10);
        mLayoutManager.requestSimpleAnimationsInNextLayout();

        eventunitView.setLayoutManager(mLayoutManager);
        eventunitView.setHasFixedSize(true);

        handleUnit();
    }

    private void handleUnit() {

        String unitStatus = EventUnitModel.UNIT_STATUS_CLOSED;

        if(!TextUtils.isEmpty(unitID))
        {
            DBUnitStatusHelper dbUnitStatusHelper = new DBUnitStatusHelper();
            unitStatus  = dbUnitStatusHelper.getStatusofUnit(unitID);
        }
//
//        if(!unitStatus.equalsIgnoreCase(EventUnitModel.UNIT_STATUS_CLOSED))
//        {
            if(eventResultsController == null)
            {
                eventResultsController = new EventResultsController(new WeakReference<IUIListener>(this),this);
            }
            eventResultsController.getEventResults(eventID,eventStartTime);
//        }

    }

    @Override
    public void onSuccess(IResponseModel responseModel)
    {
        UnitResultsViewModel resultsViewModel = (UnitResultsViewModel) responseModel;





    }

    @Override
    public void onFailure(ErrorModel errorModel) {

    }

    @Override
    public void handleLoadingIndicator(boolean showLoadingInd) {

    }
}
