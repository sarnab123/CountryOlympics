package com.olympics.olympicsandroid.view.fragment;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.olympics.olympicsandroid.OlympicsApplication;
import com.olympics.olympicsandroid.R;
import com.olympics.olympicsandroid.model.presentationModel.EventUnitModel;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by jui.joshi
 */
public class ExpandableListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int EVENT_DETAIL = 1;
    public static final int EVENT_RESULTS = 2;
    public static final int EVENT_HEADER = 3;

    protected static IItemClickListener itemClickListener;

    private List<Item> data;

    public ExpandableListAdapter(List<Item> data, IItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View view = null;
        Context context = parent.getContext();
        float dp = context.getResources().getDisplayMetrics().density;
        int subItemPaddingLeft = (int) (18 * dp);
        int subItemPaddingTopAndBottom = (int) (5 * dp);
        switch (type) {
            case EVENT_DETAIL:
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_event_details, parent, false);
                ListEventDetailsViewHolder eventDetails = new ListEventDetailsViewHolder(view);
                return eventDetails;

            case EVENT_HEADER:
                inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_event_header, parent, false);
                ListEventHeaderViewHolder eventHeader = new ListEventHeaderViewHolder(view);
                return eventHeader;
        }
        return null;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Item item = data.get(position);
        switch (item.type) {

            case EVENT_HEADER:
                final ListEventHeaderViewHolder viewHolder = (ListEventHeaderViewHolder) holder;
                if (!TextUtils.isEmpty(item.eventUnitModel.getParentDisciple())) {
                    viewHolder.event_title.setText(item.eventUnitModel.getParentDisciple());
                    int rid = OlympicsApplication.getAppContext().getResources()
                            .getIdentifier(item.eventUnitModel.getDisciplineAlias().toLowerCase(), "raw", OlympicsApplication.getAppContext().getPackageName());
                    try {
                        ((ListEventHeaderViewHolder) holder).sportImage.setImageBitmap(BitmapFactory.decodeStream(OlympicsApplication.getAppContext().getResources()
                                .openRawResource(rid)));
                    } catch (Exception ex) {
                        System.out.println("Exeptipn == " + ex);
                    }
                }

                break;
            case EVENT_DETAIL:
                final ListEventDetailsViewHolder eventDetailsViewHolder = (ListEventDetailsViewHolder) holder;
                byte medalType = item.eventUnitModel.getUnitMedalType();

                eventDetailsViewHolder.bind(medalType,item);
                break;


            case EVENT_RESULTS:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).type;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private static class ListEventDetailsViewHolder extends RecyclerView.ViewHolder {
        public TextView eventDescription;
        public TextView eventTime;
        public ImageView medal_image;
        public TextView eventVenue;
        public Item refferalItem;

        public ListEventDetailsViewHolder(View itemView) {
            super(itemView);
            eventDescription = (TextView) itemView.findViewById(R.id.event_description);
            eventTime = (TextView) itemView.findViewById(R.id.event_time);
            eventVenue = (TextView) itemView.findViewById(R.id.event_venue);
            medal_image = (ImageView) itemView.findViewById(R.id.id_medal_image);
        }

        public void bind(final byte medalType,final Item item) {
            refferalItem = item;
            byte spbyteName[] = new byte[0];
            try {

                spbyteName = item.eventUnitModel.getEventName().getBytes("ISO-8859-1");
                String str = new String(spbyteName);
                eventDescription.setText(str);
            } catch (Exception e) {
                e.printStackTrace();
            }

            eventTime.setText("For event start time,enter event details");
            byte spbyte[] = new byte[0];
            try {

                spbyte = item.eventUnitModel.getUnitVenue().getBytes("ISO-8859-1");
                String str = new String(spbyte);
                eventVenue.setText(str);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.handleItemClick(item);
                }
            });

            if(medalType == EventUnitModel.UNIT_MEDAL_GOLD)
            {
                medal_image.setImageDrawable(ContextCompat.getDrawable(OlympicsApplication.getAppContext(), R.drawable.gold_event));
            } else if(medalType == EventUnitModel.UNIT_MEDAL_BRONZE)
            {
                medal_image.setImageDrawable(ContextCompat.getDrawable(OlympicsApplication.getAppContext(), R.drawable.bronze_event));
            }
        }
    }

    private static class ListEventHeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView event_title;
        public ImageView sportImage;

        public ListEventHeaderViewHolder(View itemView) {
            super(itemView);
            event_title = (TextView) itemView.findViewById(R.id.id_unit_title);
            sportImage = (ImageView) itemView.findViewById(R.id.id_sports_image);
        }

    }

    public static class Item {
        public int type;
        public EventUnitModel eventUnitModel;
        public String sportsTitle;
        public List<Item> invisibleChildren;


        public Item(int type, EventUnitModel eventUnitModel) {
            this.type = type;
            this.eventUnitModel = eventUnitModel;
        }

    }
}
