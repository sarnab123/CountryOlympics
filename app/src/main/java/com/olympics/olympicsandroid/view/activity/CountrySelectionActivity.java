package com.olympics.olympicsandroid.view.activity;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.olympics.olympicsandroid.R;
import com.olympics.olympicsandroid.model.CountryModel;
import com.olympics.olympicsandroid.model.ErrorModel;
import com.olympics.olympicsandroid.model.IResponseModel;
import com.olympics.olympicsandroid.model.Organization;
import com.olympics.olympicsandroid.networkLayer.controller.CountryListController;
import com.olympics.olympicsandroid.networkLayer.controller.IUIListener;
import com.olympics.olympicsandroid.networkLayer.database.OlympicsPrefs;
import com.olympics.olympicsandroid.view.activity.factory.ActivityFactory;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by sarnab.poddar on 7/7/16.
 */
public class CountrySelectionActivity extends Activity implements IUIListener
{

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private CountryListAdapter countryListAdapter;

    private boolean isFirstLaunch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.country_list);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView_row);
        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        countryListAdapter = new CountryListAdapter();
        mRecyclerView.setAdapter(countryListAdapter);

        isFirstLaunch = getIntent().getBooleanExtra("first_launch",false);

        getCountryData();
    }

    private void getCountryData()
    {

        CountryListController countryListController = new CountryListController(new WeakReference<IUIListener>(this), getApplication());
        countryListController.getCountryData();

    }

    @Nullable
    @Override
    public void onSuccess(IResponseModel responseModel) {
        if(responseModel instanceof CountryModel)
        {
            CountryModel countryModel = (CountryModel) responseModel;
            countryListAdapter.setCountryModelList(countryModel.getOrganization());
            countryListAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onFailure(ErrorModel errorModel) {

    }

    class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.ViewHolder>
    {

        List<Organization> countryModelOrg;


        protected void setCountryModelList(List<Organization> countryModelList)
        {
            this.countryModelOrg = countryModelList;
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {

            private ImageView countryImage;
            private TextView countryAlias;
            private TextView countryName;

            public ViewHolder(View itemView) {
                super(itemView);

                countryImage = (ImageView)itemView.findViewById(R.id.id_country_image);
                countryAlias = (TextView)itemView.findViewById(R.id.id_country_alias);
                countryName = (TextView)itemView.findViewById(R.id.id_country_name);
            }

        }

        @Override
        public CountryListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater inflater =
                    (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View convertView = inflater.inflate(R.layout.country_selection, parent, false);
            convertView.setOnClickListener(localOnClickListener);
            return new ViewHolder(convertView);
        }

        private final View.OnClickListener localOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemPosition = mRecyclerView.getChildLayoutPosition(view);
                Organization selectedOrg = countryModelOrg.get(itemPosition);
                OlympicsPrefs.getInstance(null).setSelectedCountry(selectedOrg);
                determineNextScreen();
            }
        };


        @Override
        public void onBindViewHolder(CountryListAdapter.ViewHolder holder, int position)
        {
                Organization countryModel = countryModelOrg.get(position);
                holder.countryName.setText(countryModel.getDescription());
                holder.countryAlias.setText(countryModel.getAlias());
                int rid = getResources()
                    .getIdentifier(countryModel.getAlias().toLowerCase(), "raw", getPackageName());
            try {
                holder.countryImage.setImageBitmap(BitmapFactory.decodeStream(getResources().openRawResource(rid)));
            } catch (Exception ex)
            {
                System.out.println("Exeptipn == "+ex);
            }
        }

        @Override
        public int getItemCount() {
            return countryModelOrg != null ?countryModelOrg.size() :0;
        }
    }

    private void determineNextScreen() {
        if(isFirstLaunch)
        {
            ActivityFactory.openMainActivity(this, null);
        }
        finish();
    }
}
