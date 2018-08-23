package com.example.apn.vortex;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by APN on 12/7/2017.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<EventListItem> eventListItems;

    public EventAdapter(List<EventListItem> eventListItems, Context context) {
        this.eventListItems = eventListItems;
        this.context = context;
    }

    private Context context;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_itme,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final EventListItem eventListItem = eventListItems.get(position);

        holder.textViewHead.setText(eventListItem.getHead());
        holder.textViewdesc.setText(eventListItem.getDesc());
        holder.description.setText(eventListItem.getDesription());
        holder.time.setText(eventListItem.getTime());

        Picasso.with(context)
                .load(eventListItem.getImgUrl())
                .into(holder.imageView);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent l = new Intent(context,ManageEvent.class);
                Bundle b = new Bundle();
                   b.putString("userId", eventListItem.getUserId().toString());
                b.putString("userName", eventListItem.getUserName().toString());
                b.putString("eventId", eventListItem.getEventId().toString());
                b.putString("ename", eventListItem.getHead().toString());
               // b.putString("description", eventListItem.getDesription().toString());
               // b.putString("eventImgurl", eventListItem.getImgUrl().toString());
                //b.putString("time", eventListItem.getTime().toString());
                l.putExtras(b);
                context.startActivity(l);
            }
        });

    }

    @Override
    public int getItemCount() {
        return eventListItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewHead;
        public TextView textViewdesc;
        public TextView description;
        public TextView time;
        public ImageView imageView;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewHead = (TextView) itemView.findViewById(R.id.textViewHeader);
            textViewdesc = (TextView) itemView.findViewById(R.id.textViewDesc);
            description = (TextView) itemView.findViewById(R.id.descripton);
            time = (TextView) itemView.findViewById(R.id.time);
            imageView = (ImageView) itemView.findViewById(R.id.eventthemephoto);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.layoutevent);
        }
    }
}
