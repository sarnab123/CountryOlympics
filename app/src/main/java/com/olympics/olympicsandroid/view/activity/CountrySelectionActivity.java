package com.olympics.olympicsandroid.view.activity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.olympics.olympicsandroid.OlympicsApplication;
import com.olympics.olympicsandroid.R;
import com.olympics.olympicsandroid.model.CountryModel;
import com.olympics.olympicsandroid.model.ErrorModel;
import com.olympics.olympicsandroid.model.IResponseModel;
import com.olympics.olympicsandroid.model.Organization;
import com.olympics.olympicsandroid.networkLayer.cache.database.OlympicsPrefs;
import com.olympics.olympicsandroid.networkLayer.controller.CountryListController;
import com.olympics.olympicsandroid.networkLayer.controller.IUIListener;
import com.olympics.olympicsandroid.utility.UtilityMethods;
import com.olympics.olympicsandroid.view.activity.factory.ActivityFactory;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sarnab.poddar on 7/7/16.
 */
public class CountrySelectionActivity extends AppCompatActivity implements IUIListener, SearchView.OnQueryTextListener
{

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private CountryListAdapter countryListAdapter;
    private List<Organization> countryList;

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

        setActionbar();
    }

    private void setActionbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            countryList = countryModel.getOrganization();
            countryListAdapter.setCountryModelList(countryList);
            countryListAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onFailure(ErrorModel errorModel)
    {
        if(errorModel != null && !TextUtils.isEmpty(errorModel.getErrorCode()) && errorModel.getErrorCode().equalsIgnoreCase(UtilityMethods.ERROR_INTERNET))
        {
            try {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                dlgAlert.setMessage(getString(R.string.id_error_internet));
                dlgAlert.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        getCountryData();
                    }
                });
                dlgAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                dlgAlert.setCancelable(false);
                dlgAlert.create().show();
            } catch (WindowManager.BadTokenException ex) {

            }

            Bundle params = new Bundle();
            params.putString("app_error", "event_result");
            params.putString("app_screen","country_selection");

            params.putString("error_reason", errorModel.getErrorMessage());
            FirebaseAnalytics.getInstance(OlympicsApplication.getAppContext()).logEvent("error", params);

        }
        else{

            Bundle params = new Bundle();
            params.putString("app_error", "event_result");
            params.putString("app_screen","country_selection");

            if(errorModel != null && !TextUtils.isEmpty(errorModel.getErrorMessage())) {
                params.putString("error_reason", errorModel.getErrorMessage());
            }
            else{
                params.putString("error_reason", "generic_error");
            }
            FirebaseAnalytics.getInstance(OlympicsApplication.getAppContext()).logEvent("error", params);


            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage(getString(R.string.id_error_server));
            dlgAlert.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                    getCountryData();
                }
            });
            dlgAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                }
            });
            dlgAlert.setCancelable(false);
            dlgAlert.create().show();
        }

    }

    @Override
    public void handleLoadingIndicator(boolean showLoadingInd) {
    }

    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        List<Organization> filteredCountryList = new ArrayList<>();

        if (TextUtils.isEmpty(newText) && countryListAdapter != null)
        {
            countryListAdapter.setCountryModelList(countryList);
            countryListAdapter.notifyDataSetChanged();
            return true;
        }

        if(countryList != null && countryList.size() > 0) {
            for (Organization country : countryList) {
                if (!TextUtils.isEmpty(newText) && country != null && (!TextUtils.isEmpty(country.getDescription()) && country
                        .getDescription().toLowerCase().contains(newText.toLowerCase())) || (!TextUtils.isEmpty
                        (country.getAlias()) && country.getAlias().toLowerCase().contains(newText.toLowerCase()))) {
                    filteredCountryList.add(country);
                }
            }
            countryListAdapter.setCountryModelList(filteredCountryList);
            countryListAdapter.notifyDataSetChanged();
            return true;
        }
        return false;
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

                Bundle params = new Bundle();
                params.putString("app_country", selectedOrg.getDescription());
                FirebaseAnalytics.getInstance(OlympicsApplication.getAppContext()).logEvent("country", params);


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
        else{
            Intent intent=new Intent();
            intent.putExtra("COUNTRY_CODE",OlympicsPrefs.getInstance(null).getUserSelectedCountry().getAlias());
            setResult(RESULT_OK,intent);
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.country_selection, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        return true;
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

}
