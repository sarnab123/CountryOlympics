package com.olympics.olympicsandroid.view.activity;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.olympics.olympicsandroid.R;
import com.olympics.olympicsandroid.model.ErrorModel;
import com.olympics.olympicsandroid.model.IResponseModel;
import com.olympics.olympicsandroid.model.MedalTally;
import com.olympics.olympicsandroid.model.MedalTallyOrganization;
import com.olympics.olympicsandroid.networkLayer.cache.database.OlympicsPrefs;
import com.olympics.olympicsandroid.networkLayer.controller.IUIListener;
import com.olympics.olympicsandroid.networkLayer.controller.MedalTallyController;
import com.olympics.olympicsandroid.utility.MedalTallyComparator;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
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
                Collections.sort(orgsByRanking, new MedalTallyComparator());
                for (int index = 0; index < orgsByRanking.size(); index++) {
                    MedalTallyOrganization orgByStanding = orgsByRanking.get(index);
                    orgByStanding.setRank(index + 1);
                }
            }
            medalTallyListAdapter = new MedalTallyListAdapter();
            medalTallyListAdapter.setMedalTallyList(orgsByRanking);

            mRecyclerView.setAdapter(medalTallyListAdapter);
        }
    }

    @Override
    public void onFailure(ErrorModel errorModel) {
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
            this.medalTallyList = medalTallyList;
            Collections.sort(medalTallyList, new MedalTallyComparator(OlympicsPrefs.getInstance(null)
                    .getUserSelectedCountry()));
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
            if (position == 0) {
                holder.medalRowLayout.setBackgroundResource(R.drawable.medal_first_row_border);
                holder.dividerView.setVisibility(View.GONE);
            }

            MedalTallyOrganization medalTallyObj = medalTallyList.get(position);
            holder.countryAlias.setText(medalTallyObj.getCountryName());
            holder.countryRankView.setText(Html.fromHtml("<b>" + getResources().getString(R.string
                    .rank_str, + medalTallyObj.getRank() + "</b>", getResources().getString(R.string
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
        }

        @Override
        public int getItemCount() {
            return medalTallyList != null ?medalTallyList.size() :0;
        }
    }
}
