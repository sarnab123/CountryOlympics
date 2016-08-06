package com.olympics.olympicsandroid.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.olympics.olympicsandroid.OlympicsApplication;
import com.olympics.olympicsandroid.R;
import com.olympics.olympicsandroid.model.ErrorModel;
import com.olympics.olympicsandroid.model.IResponseModel;
import com.olympics.olympicsandroid.model.MedalTally;
import com.olympics.olympicsandroid.model.MedalTallyOrganization;
import com.olympics.olympicsandroid.networkLayer.cache.database.OlympicsPrefs;
import com.olympics.olympicsandroid.networkLayer.controller.IUIListener;
import com.olympics.olympicsandroid.networkLayer.controller.MedalTallyController;
import com.olympics.olympicsandroid.utility.MedalTallyComparator;
import com.olympics.olympicsandroid.utility.UtilityMethods;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MedalTallyActivity extends AppCompatActivity implements NavigationView
        .OnNavigationItemSelectedListener, IUIListener {

    private MedalTallyListAdapter medalTallyListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medal_tally);

        //Request for MedalTally Data through controller
        MedalTallyController medalTallyController = new MedalTallyController(new
                WeakReference<IUIListener>(this), getApplication());
        medalTallyController.getMedalTallyData();

        //Setup Actionbar
        setActionBar();
    }

    private void setActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(IResponseModel responseModel) {
        if(responseModel instanceof MedalTally)
        {
            MedalTally medalTallyObj = (MedalTally) responseModel;
            RecyclerView mRecyclerView = (RecyclerView)findViewById(R.id.medaltally_recyclerView);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,
                    false);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);

            //Set up the rankings for countries based on medal total.
            List<MedalTallyOrganization> orgsByRanking = new ArrayList<>(medalTallyObj
                    .getOrganization());
            if (orgsByRanking != null) {
                MedalTallyComparator comparator = new MedalTallyComparator();
                Collections.sort(orgsByRanking, comparator);
                populateRank(orgsByRanking, comparator);
            }
            medalTallyListAdapter = new MedalTallyListAdapter();
            medalTallyListAdapter.setMedalTallyList(orgsByRanking);

            mRecyclerView.setAdapter(medalTallyListAdapter);

            //Updated timestamp
            TextView updatedTimestampView = (TextView)findViewById(R.id
                    .medaltally_timestamp);
            updatedTimestampView.setText(getString(R.string.medaltally_update_timestamp,
                    OlympicsPrefs.getInstance(null)
                    .getMedalTallyRefreshTime()));
            updatedTimestampView.setVisibility(View.VISIBLE);
        }
    }

    private void populateRank(List<MedalTallyOrganization> orgsByRanking, Comparator comparator) {
        int index = 0;
        while (index < orgsByRanking.size()) {
            int rank = index + 1;
            orgsByRanking.get(index).setRank(rank);
            while (index + 1 < orgsByRanking.size()) {
                int compare = comparator.compare(orgsByRanking.get(index), orgsByRanking.get
                        (index+1));
                if (compare == 0) {
                    orgsByRanking.get(index+1).setRank(rank);
                    index++;
                } else {
                    break;
                }
            }
            index++;
        }
    }

    @Override
    public void onFailure(ErrorModel errorModel)
    {
        if(errorModel != null  && !TextUtils.isEmpty(errorModel.getErrorMessage())) {
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


    class MedalTallyListAdapter extends RecyclerView.Adapter<MedalTallyListAdapter.ViewHolder>
    {
        List<MedalTallyOrganization> medalTallyList;

        protected void setMedalTallyList(List<MedalTallyOrganization> medalTallyList)
        {

            Collections.sort(medalTallyList, new MedalTallyComparator(OlympicsPrefs.getInstance(null)
                    .getUserSelectedCountry()));
            this.medalTallyList = medalTallyList;

        }

        private boolean isCountryWithMedals() {
            try {
                if (medalTallyList != null && !medalTallyList.isEmpty() &&
                        Integer.parseInt(medalTallyList.get(0).getTotal()) > 0) {
                    Toast.makeText(OlympicsApplication.getAppContext(), getString(R.string.country_with_medal_msg),
                            Toast.LENGTH_LONG).show();

                    return true;
                }
            } catch (Exception ex) {
                return false;
            }
            return false;
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {
            private RelativeLayout medalRowLayout;
            private View dividerView;
            private ImageView countryImageView;
            private TextView countryRankView;
            private TextView countryAlias;
            private TextView goldCountText;
            private TextView silverCountText;
            private TextView bronzeCountText;

            public ViewHolder(View itemView) {
                super(itemView);


                medalRowLayout = (RelativeLayout)itemView.findViewById(R.id.medal_row_layout);
                dividerView = (View)itemView.findViewById(R.id.divider_view);
                countryRankView = (TextView)itemView.findViewById(R.id.id_country_rank);
                countryAlias = (TextView)itemView.findViewById(R.id.id_country_name);
                goldCountText = (TextView)itemView.findViewById(R.id.id_gold_count);
                silverCountText = (TextView)itemView.findViewById(R.id.id_silver_count);
                bronzeCountText = (TextView)itemView.findViewById(R.id.id_bronze_count);
                countryImageView = (ImageView)itemView.findViewById(R.id.id_country_image);

            }
        }

        @Override
        public MedalTallyListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater inflater =
                    (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View convertView = inflater.inflate(R.layout.medal_list_row, parent, false);
            return new ViewHolder(convertView);
        }

        @Override
        public void onBindViewHolder(MedalTallyListAdapter.ViewHolder holder, int position)
        {
            //Change row for selected country
            if (medalTallyList.get(position).getCountryName().equalsIgnoreCase(OlympicsPrefs
                    .getInstance(null).getUserSelectedCountry().getDescription())) {
                if (isCountryWithMedals()) {
                    holder.medalRowLayout.setBackgroundResource(R.drawable
                            .medal_first_row_border_enable);
                } else {
                    holder.medalRowLayout.setBackgroundResource(R.drawable.medal_first_row_border_disable);
                }
                holder.dividerView.setVisibility(View.GONE);
            } else {
                holder.medalRowLayout.setBackgroundResource(0);
                holder.dividerView.setVisibility(View.VISIBLE);
            }

            final MedalTallyOrganization medalTallyObj = medalTallyList.get(position);
            holder.countryAlias.setText(medalTallyObj.getCountryName());
            holder.countryRankView.setText(Html.fromHtml("<b>" + getResources().getString(R.string
                    .rank_str, +medalTallyObj.getRank() + "</b>", getResources().getString(R.string
                    .medal_total_str), medalTallyObj.getTotal())));
            holder.goldCountText.setText(medalTallyObj.getGold());
            holder.silverCountText.setText(medalTallyObj.getSilver());
            holder.bronzeCountText.setText(medalTallyObj.getBronze());

            int rid = getResources()
                    .getIdentifier(medalTallyObj.getAlias().toLowerCase(), "raw", getPackageName());
            try {
                holder.countryImageView.setImageBitmap(BitmapFactory.decodeStream(getResources()
                        .openRawResource(rid)));
            } catch (Exception ex)
            {
                System.out.println("Exeptipn == "+ex);
            }

            if (holder.medalRowLayout != null) {
                holder.medalRowLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Open Medaltally breakdown page for selected country if country has won at
                        // least one medal
                        if (medalTallyObj != null && medalTallyObj.getId().equalsIgnoreCase
                                (OlympicsPrefs.getInstance(null).getUserSelectedCountry().getId()
                                )) {
                            if (!TextUtils.isEmpty(medalTallyObj.getTotal()) && !medalTallyObj
                                    .getTotal().equalsIgnoreCase("0")) {
                                Intent intent = new Intent(new Intent(MedalTallyActivity.this,
                                        MedalTallyBreakDownActivity.class));
                                intent.putExtra(UtilityMethods.EXTRA_SELECTED_COUNTRY,
                                        medalTallyObj.getId());
                                startActivity(intent);
                            } else {
                                Toast.makeText(OlympicsApplication.getAppContext(),
                                        getString(R.string.no_medal_won_msg), Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return medalTallyList != null ?medalTallyList.size() :0;
        }
    }
}
