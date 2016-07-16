package com.olympics.olympicsandroid.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.olympics.olympicsandroid.R;
import com.olympics.olympicsandroid.model.presentationModel.DateSportsModel;
import com.olympics.olympicsandroid.model.presentationModel.EventUnitModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sarnab.poddar on 7/7/16.
 */
public class EventListFragment extends Fragment
{
    private static final String DISPLAY_DATA = "display_data";
    private DateSportsModel mDateSportsModel;

    public EventListFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static EventListFragment newInstance(DateSportsModel dateSportsModel) {
        EventListFragment fragment = new EventListFragment();
        Bundle args = new Bundle();
        args.putSerializable(DISPLAY_DATA, dateSportsModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_olympics, container, false);

        RecyclerView recyclerview = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,
                false));
        this.mDateSportsModel = (DateSportsModel) getArguments().getSerializable(DISPLAY_DATA);
        recyclerview.setAdapter(new ExpandableListAdapter(getData(), createItemClickListener()));

      return rootView;
    }

    private IItemClickListener createItemClickListener() {
        return new IItemClickListener() {
            @Override
            public void handleItemClick(ExpandableListAdapter.Item itemClicked) {

            }
        };
    }


    private List<ExpandableListAdapter.Item> getData() {
        List<ExpandableListAdapter.Item> data = new ArrayList<>();
        if (mDateSportsModel != null && mDateSportsModel.getAllSportsForDate() != null) {
            for (Map.Entry<String, DateSportsModel.SportsEventsUnits> entry : mDateSportsModel.getAllSportsForDate().entrySet()) {


                if (entry.getValue() != null) {
                    DateSportsModel.SportsEventsUnits sportsEventsUnit = entry.getValue();
                    if (sportsEventsUnit != null &&  sportsEventsUnit.getEventUnits() != null) {
                        String sportsDiscipline = null;
                        for (EventUnitModel eventUnitModel : sportsEventsUnit.getEventUnits()) {
                            if(sportsDiscipline == null || !sportsDiscipline.equalsIgnoreCase(eventUnitModel.getParentDisciple()))
                            {
                                data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.EVENT_HEADER, eventUnitModel));
                            }
                            data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.EVENT_DETAIL, eventUnitModel));
                        }
                    }
                }
            }
        }
        return data;
    }

}
