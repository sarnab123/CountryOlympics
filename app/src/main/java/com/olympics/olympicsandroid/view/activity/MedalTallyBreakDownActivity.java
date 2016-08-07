package com.olympics.olympicsandroid.view.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import com.olympics.olympicsandroid.R;
import com.olympics.olympicsandroid.model.ErrorModel;
import com.olympics.olympicsandroid.model.IResponseModel;
import com.olympics.olympicsandroid.model.MedalTallyBreakdown;
import com.olympics.olympicsandroid.model.MedalTallyCompetitor;
import com.olympics.olympicsandroid.model.MedalTallyEvent;
import com.olympics.olympicsandroid.model.presentationModel.MedalTallyByOrgModel;
import com.olympics.olympicsandroid.networkLayer.controller.IUIListener;
import com.olympics.olympicsandroid.networkLayer.controller.MedalTallyBreakdownController;
import com.olympics.olympicsandroid.utility.UtilityMethods;
import com.olympics.olympicsandroid.view.fragment.MedalBreakdownAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedalTallyBreakDownActivity extends AppCompatActivity implements NavigationView
        .OnNavigationItemSelectedListener, IUIListener {

    private MedalBreakdownAdapter medalTallyListAdapter;
    private static final String GOLD_MEDAL = "gold";
    private static final String SILVER_MEDAL = "silver";
    private static final String BRONZE_MEDAL = "bronze";
    private static final String TEAM_EVENT = "team";
    private static final String INDIVIDUAL_EVENT = "individual";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medal_tally_breakdown);

        //Request for MedalTally Data through controller
        MedalTallyBreakdownController medalTallyBreakdownController = new
                MedalTallyBreakdownController(new WeakReference<IUIListener>(this),
                getApplication());
        medalTallyBreakdownController.getMedalTallyData(getIntent().getStringExtra(UtilityMethods
                .EXTRA_SELECTED_COUNTRY));


        //Setup Actionbar
        setActionBar();
    }

    private void setActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(IResponseModel responseModel) {
        if (responseModel instanceof MedalTallyBreakdown) {
            RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.medaltally_recyclerView);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);

            medalTallyListAdapter = new MedalBreakdownAdapter();
            medalTallyListAdapter.setMedalBreakdown(getMedalbreakdownList((MedalTallyBreakdown)
                    responseModel));
            mRecyclerView.setAdapter(medalTallyListAdapter);
        }
    }

    /**
     * This method retrieves medaltallybreakdown map.
     * setup medaltally breakdown list with - playername as header and medaldata as details.
     *
     * @param medalTallyBreakdown
     * @return
     */
    private List<MedalBreakdownAdapter.MedalBreakdownData> getMedalbreakdownList
    (MedalTallyBreakdown medalTallyBreakdown) {


        Map<String, List<MedalTallyByOrgModel>> medalTallyByOrgModelMap =
                populateMedalTallyByOrgList(medalTallyBreakdown.getOrganization()
                        .getMedalTallyEvents());

        List<MedalBreakdownAdapter.MedalBreakdownData> medalBreakdownDataList = new ArrayList<>();
        for (Map.Entry<String, List<MedalTallyByOrgModel>> entry : medalTallyByOrgModelMap
                .entrySet()) {
            medalBreakdownDataList.add(new MedalBreakdownAdapter.MedalBreakdownData
                    (MedalBreakdownAdapter.TYPE_PLAYER_NAME, entry.getKey()));
            if (entry.getValue() != null) {
                for (MedalTallyByOrgModel medalTallyByOrgModel : (List<MedalTallyByOrgModel>)
                        entry.getValue()) {
                    medalBreakdownDataList.add(new MedalBreakdownAdapter.MedalBreakdownData
                            (MedalBreakdownAdapter.TYPE_MEDAL_DATA, medalTallyByOrgModel));
                }
            }
        }
        return medalBreakdownDataList;
    }

    /**
     * This method populates medaltally breakdown map
     * with playername as key and medaldata list as value.
     * @param medalTallyEvents
     * @return
     */
    private Map<String, List<MedalTallyByOrgModel>> populateMedalTallyByOrgList
            (List<MedalTallyEvent> medalTallyEvents) {

        Map<String, List<MedalTallyByOrgModel>> medalTallyByOrgModelMap = new HashMap<>();

        if (medalTallyEvents != null && !medalTallyEvents.isEmpty()) {
            for (MedalTallyEvent medalTallyEvent : medalTallyEvents) {
                if (medalTallyEvent.getCompetitor() != null && !medalTallyEvent.getCompetitor()
                        .isEmpty()) {
                    List<MedalTallyCompetitor> medalTallyCompetitors = medalTallyEvent
                            .getCompetitor();

                    for (MedalTallyCompetitor competitor : medalTallyCompetitors) {
                        MedalTallyByOrgModel medalTallyByOrg = new MedalTallyByOrgModel();
                        medalTallyByOrg.setAthleteName(getMedalBreakdownMapKey(competitor));
                        medalTallyByOrg.setSportsName(medalTallyEvent.getDescription() + " - " +
                                medalTallyEvent.getSport().getDescription());
                        medalTallyByOrg.setGoldCount((!TextUtils.isEmpty(competitor.getMedal
                                ()) && competitor.getMedal().equalsIgnoreCase(GOLD_MEDAL)) ? 1 : 0);
                        medalTallyByOrg.setSilverCount(((!TextUtils.isEmpty(competitor.getMedal
                                ()) && competitor.getMedal().equalsIgnoreCase(SILVER_MEDAL)) ? 1 : 0));
                        medalTallyByOrg.setBronzeCount(((!TextUtils.isEmpty(competitor.getMedal
                                ()) && competitor.getMedal().equalsIgnoreCase(BRONZE_MEDAL)) ? 1 : 0));

                        if (medalTallyByOrgModelMap.containsKey(getMedalBreakdownMapKey
                                (competitor))) {

                            List<MedalTallyByOrgModel> medalTallyByOrgModelList =
                                    medalTallyByOrgModelMap.get(getMedalBreakdownMapKey
                                            (competitor));
                            medalTallyByOrgModelList.add(medalTallyByOrg);
                        } else {
                            List<MedalTallyByOrgModel> medalTallyByOrgModelList = new ArrayList<>();
                            medalTallyByOrgModelList.add(medalTallyByOrg);
                            medalTallyByOrgModelMap.put(getMedalBreakdownMapKey(competitor),
                                    medalTallyByOrgModelList);
                        }
                    }
                }
            }
        }
        return medalTallyByOrgModelMap;
    }

    private String getMedalBreakdownMapKey(MedalTallyCompetitor competitor) {

        if(!TextUtils.isEmpty(competitor.getType())) {
            if (competitor.getType().equalsIgnoreCase(TEAM_EVENT)) {
                return competitor.getDescription();
            } else if (competitor.getType().equalsIgnoreCase(INDIVIDUAL_EVENT)){
                return competitor.getFirst_name() + " " +
                        competitor.getLast_name();
            }
        }
        return "";
    }

    @Override
    public void onFailure(ErrorModel errorModel) {
        if (errorModel != null && !TextUtils.isEmpty(errorModel.getErrorMessage())) {
            Toast.makeText(this, errorModel.getErrorMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void handleLoadingIndicator(boolean showLoadingInd) {

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

}
