package com.olympics.olympicsandroid.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.olympics.olympicsandroid.R;
import com.olympics.olympicsandroid.model.ErrorModel;
import com.olympics.olympicsandroid.model.IResponseModel;
import com.olympics.olympicsandroid.model.MedalTally;
import com.olympics.olympicsandroid.model.MedalTallyOrganization;
import com.olympics.olympicsandroid.networkLayer.controller.IUIListener;
import com.olympics.olympicsandroid.networkLayer.controller.MedalTallyController;

import java.lang.ref.WeakReference;
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
            medalTallyListAdapter = new MedalTallyListAdapter();
            medalTallyListAdapter.setMedalTallyList(medalTallyObj.getOrganization());
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
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {
            private TextView countryRankView;
            private TextView countryAlias;
            private TextView goldCountText;
            private TextView silverCountText;
            private TextView bronzeCountText;
            private TextView totalCountText;

            public ViewHolder(View itemView) {
                super(itemView);

                countryRankView = (TextView)itemView.findViewById(R.id.id_country_rank);
                countryAlias = (TextView)itemView.findViewById(R.id.id_country_alias);
                goldCountText = (TextView)itemView.findViewById(R.id.id_gold_count);
                silverCountText = (TextView)itemView.findViewById(R.id.id_silver_count);
                bronzeCountText = (TextView)itemView.findViewById(R.id.id_bronze_count);
                totalCountText = (TextView)itemView.findViewById(R.id.id_total_count);
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
            MedalTallyOrganization medalTallyObj = medalTallyList.get(position);
            holder.countryAlias.setText(medalTallyObj.getAlias());
            holder.countryRankView.setText(String.valueOf(position + 1));
            holder.goldCountText.setText(medalTallyObj.getGold());
            holder.silverCountText.setText(medalTallyObj.getSilver());
            holder.bronzeCountText.setText(medalTallyObj.getBronze());
            holder.totalCountText.setText(medalTallyObj.getTotal());
     }

        @Override
        public int getItemCount() {
            return medalTallyList != null ?medalTallyList.size() :0;
        }
    }
}
