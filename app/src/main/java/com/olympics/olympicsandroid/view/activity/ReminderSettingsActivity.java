package com.olympics.olympicsandroid.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.olympics.olympicsandroid.R;
import com.olympics.olympicsandroid.model.presentationModel.EventReminder;
import com.olympics.olympicsandroid.networkLayer.cache.database.DBReminderHelper;
import com.olympics.olympicsandroid.utility.DateUtils;
import com.olympics.olympicsandroid.utility.LocalNotifications;

import java.util.List;

public class ReminderSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_settings);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.athlete_recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        ReminderSettingsAdapter reminderSettingsAdapter = new ReminderSettingsAdapter
                (new DBReminderHelper().getReminders());
        mRecyclerView.setAdapter(reminderSettingsAdapter);

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

    class ReminderSettingsAdapter extends RecyclerView.Adapter<ReminderSettingsAdapter.ViewHolder>
    {
        List<EventReminder> eventReminderList;

        public ReminderSettingsAdapter(List<EventReminder> eventReminderList)
        {
            this.eventReminderList = eventReminderList;
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {
            private TextView reminderNameView;
            private Switch reminderSwitch;

            public ViewHolder(View itemView) {
                super(itemView);
                reminderNameView = (TextView)itemView.findViewById(R.id.id_reminder_name);
                reminderSwitch = (Switch)itemView.findViewById(R.id.reminder_switch);
            }
        }

        @Override
        public ReminderSettingsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater inflater =
                    (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View convertView = inflater.inflate(R.layout.reminder_list_row, parent, false);
            return new ViewHolder(convertView);
        }

        @Override
        public void onBindViewHolder(ReminderSettingsAdapter.ViewHolder holder, int position)
        {
            final EventReminder eventReminder = eventReminderList.get(position);
            if (eventReminder != null) {
                holder.reminderNameView.setText(eventReminder.getDisciplineName() + "\n" +
                        eventReminder
                        .getUnitName() + "\n" + DateUtils.getUnitDateWithTime(eventReminder
                        .getUnitStartDate()));
                holder.reminderSwitch.setChecked(true);
                holder.reminderSwitch.setOnCheckedChangeListener(new CompoundButton
                        .OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (!isChecked) {
                            //Cancel reminder
                            new LocalNotifications().cancelLocalNotification(ReminderSettingsActivity
                                    .this, eventReminder.getUnitId());
                        } else {
                            //create reminder
                            new LocalNotifications().createLocalNotification(ReminderSettingsActivity
                                    .this, eventReminder);
                        }
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return eventReminderList != null ?eventReminderList.size() :0;
        }

    }
}
