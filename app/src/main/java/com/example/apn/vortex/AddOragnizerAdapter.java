package com.example.apn.vortex;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by APN on 12/23/2017.
 */

    public class AddOragnizerAdapter extends RecyclerView.Adapter<AddOragnizerAdapter.ViewHolder> {

    private List<AddOrganizersListItem> addOrganizersListItems;
    private Context context;

    public AddOragnizerAdapter(List<AddOrganizersListItem> addOrganizersListItems, Context context) {
        this.addOrganizersListItems = addOrganizersListItems;
        this.context = context;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_organizers,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AddOragnizerAdapter.ViewHolder holder, final int position) {
      final    AddOrganizersListItem addOrganizersListItem = addOrganizersListItems.get(position);

        holder.oraganizerName.setText(addOrganizersListItem.getOrganizerName().toString());
        holder.userType.setText(addOrganizersListItem.getUserType().toString());

        holder.deleteOrganizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOrganizersListItems.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,addOrganizersListItems.size());
               // Toast.makeText(context,"Delete Organizer",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent("custom-message2");
                // intent.putExtra("organizerImgUrl",userItemList.getUserImg());
                intent.putExtra("organizerId",addOrganizersListItem.getOrganizerId());
                intent.putExtra("userType",addOrganizersListItem.getUserType());
               // intent.putExtra("id",userItemList.getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return addOrganizersListItems.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView oraganizerName;
        TextView userType;
       CircleImageView organizerPic;
       Button deleteOrganizer;

        public ViewHolder(View itemView) {
            super(itemView);

            oraganizerName = (TextView) itemView.findViewById(R.id.organizername);
            userType = (TextView) itemView.findViewById(R.id.usertype);
           //  organizerPic = (CircleImageView)itemView.findViewById(R.id.organizerpic);
            deleteOrganizer = (Button) itemView.findViewById(R.id.deleteorganizer);

        }
    }





}

