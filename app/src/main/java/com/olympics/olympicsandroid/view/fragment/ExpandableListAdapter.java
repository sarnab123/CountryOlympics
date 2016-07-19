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
import com.olympics.olympicsandroid.utility.DateUtils;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by jui.joshi
 */
public class ExpandableListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int SPORTS_TITLE_HEADER = 0;
    public static final int EVENT_DETAIL = 1;
    public static final int EVENT_RESULTS = 2;
    public static final int EVENT_HEADER = 3;
    protected static IItemClickListener itemClickListener;

    private List<Item> data;

    public ExpandableListAdapter(List<Item> data , IItemClickListener itemClickListener)
    {
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

            case EVENT_HEADER:
                inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_event_header, parent, false);
                ListEventHeaderViewHolder eventHeader = new ListEventHeaderViewHolder(view);
                return eventHeader;
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

            case EVENT_HEADER:
                final ListEventHeaderViewHolder viewHolder = (ListEventHeaderViewHolder) holder;
                if (!TextUtils.isEmpty(item.eventUnitModel.getParentDisciple())) {
                    viewHolder.event_title.setText(item.eventUnitModel.getParentDisciple());
                }
                break;
            case SPORTS_TITLE_HEADER:
                final ListHeaderViewHolder itemController = (ListHeaderViewHolder) holder;
                if (!TextUtils.isEmpty(item.sportsTitle)) {
                    itemController.header_title.setText(item.sportsTitle);
                }
                break;
            case EVENT_DETAIL:
                final ListEventDetailsViewHolder eventDetailsViewHolder = (ListEventDetailsViewHolder) holder;
                eventDetailsViewHolder.bind(item);
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

        public void bind(final Item item) {
            refferalItem = item;
            eventDescription.setText(item.eventUnitModel.getEventName());
            eventStartDate.setText(DateUtils.setUpUnitDate(item.eventUnitModel.getEventStartTime
                    ()));
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
        }
    }

    private static class ListEventHeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView event_title;

        public ListEventHeaderViewHolder(View itemView) {
            super(itemView);
            event_title = (TextView) itemView.findViewById(R.id.id_event_title);
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
