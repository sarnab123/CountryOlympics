package com.olympics.olympicsandroid.view.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.olympics.olympicsandroid.R;
import com.olympics.olympicsandroid.model.presentationModel.EventUnitModel;

import java.util.List;

/**
 * Created by anandbose on 09/06/15.
 */
public class ExpandableListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int SPORTS_TITLE_HEADER = 0;
    public static final int EVENT_DETAIL = 1;
    public static final int EVENT_RESULTS = 2;


    private List<Item> data;

    public ExpandableListAdapter(List<Item> data) {
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
            case SPORTS_TITLE_HEADER:
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_header, parent, false);
                ListHeaderViewHolder header = new ListHeaderViewHolder(view);
                return header;
            case EVENT_DETAIL:
                inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_event_details, parent, false);
                ListEventDetailsViewHolder eventDetails = new ListEventDetailsViewHolder(view);
                return eventDetails;
            case EVENT_RESULTS:
//                TextView itemTextView = new TextView(context);
//                itemTextView.setPadding(subItemPaddingLeft, subItemPaddingTopAndBottom, 0, subItemPaddingTopAndBottom);
//                itemTextView.setTextColor(0x88000000);
//                itemTextView.setLayoutParams(
//                        new ViewGroup.LayoutParams(
//                                ViewGroup.LayoutParams.MATCH_PARENT,
//                                ViewGroup.LayoutParams.WRAP_CONTENT));
//                return new RecyclerView.ViewHolder(itemTextView) {
//                };
        }
        return null;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Item item = data.get(position);
        switch (item.type) {
            case SPORTS_TITLE_HEADER:
                final ListHeaderViewHolder itemController = (ListHeaderViewHolder) holder;
                if (!TextUtils.isEmpty(item.sportsTitle)) {
                    itemController.header_title.setText(item.sportsTitle);
                }
                break;
            case EVENT_DETAIL:
                final ListEventDetailsViewHolder eventDetailsViewHolder = (ListEventDetailsViewHolder) holder;
                eventDetailsViewHolder.refferalItem = item;
                eventDetailsViewHolder.eventDescription.setText(item.eventUnitModel.getUnitName());
                eventDetailsViewHolder.eventStartDate.setText(item.eventUnitModel.getEventStartTime() +"");
                eventDetailsViewHolder.eventVenue.setText(item.eventUnitModel.getUnitVenue());
                break;
                //TODO : Expand event results data

//                if (item.invisibleChildren == null) {
//                    itemController.btn_expand_toggle.setImageResource(R.drawable.circle_minus);
//                } else {
//                    itemController.btn_expand_toggle.setImageResource(R.drawable.circle_plus);
//                }
//                itemController.btn_expand_toggle.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (item.invisibleChildren == null) {
//                            item.invisibleChildren = new ArrayList<Item>();
//                            int count = 0;
//                            int pos = data.indexOf(itemController.refferalItem);
//                            while (data.size() > pos + 1 && data.get(pos + 1).type == EVENT_DETAIL) {
//                                item.invisibleChildren.add(data.remove(pos + 1));
//                                count++;
//                            }
//                            notifyItemRangeRemoved(pos + 1, count);
//                            itemController.btn_expand_toggle.setImageResource(R.drawable.circle_plus);
//                        } else {
//                            int pos = data.indexOf(itemController.refferalItem);
//                            int index = pos + 1;
//                            for (Item i : item.invisibleChildren) {
//                                data.add(index, i);
//                                index++;
//                            }
//                            notifyItemRangeInserted(pos + 1, index - pos - 1);
//                            itemController.btn_expand_toggle.setImageResource(R.drawable.circle_minus);
//                            item.invisibleChildren = null;
//                        }
//                    }
//                });



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

    private static class ListHeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView header_title;
        public ImageView btn_expand_toggle;

        public ListHeaderViewHolder(View itemView) {
            super(itemView);
            header_title = (TextView) itemView.findViewById(R.id.header_title);
            btn_expand_toggle = (ImageView) itemView.findViewById(R.id.btn_expand_toggle);
        }
    }

    private static class ListEventDetailsViewHolder extends RecyclerView.ViewHolder {
        public TextView eventDescription;
        public TextView eventStartDate;
        public TextView eventVenue;
        public Item refferalItem;

        public ListEventDetailsViewHolder(View itemView) {
            super(itemView);
            eventDescription = (TextView) itemView.findViewById(R.id.event_description);
            eventStartDate = (TextView) itemView.findViewById(R.id.event_start_time);
            eventVenue = (TextView) itemView.findViewById(R.id.event_venue);
        }
    }

    public static class Item {
        public int type;
        public EventUnitModel eventUnitModel;
        public String sportsTitle;
        public List<Item> invisibleChildren;

        public Item() {
        }

        public Item(int type, EventUnitModel eventUnitModel) {
            this.type = type;
            this.eventUnitModel = eventUnitModel;
        }

        public Item(int type, String sportsTitle) {
            this.type = type;
            this.sportsTitle = sportsTitle;
        }
    }
}
