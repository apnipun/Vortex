package com.example.apn.vortex;

import android.app.Activity;
import android.app.FragmentManager;
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
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by APN on 12/17/2017.
 */

public class UserItemSearchAdapter extends RecyclerView.Adapter<UserItemSearchAdapter.ViewHolder> {

    private List<UserItemList> userItemLists;
    SupplierViewFragment supplierViewFragment;




    String selectedUserInfoUrl = "http://192.168.8.100:3000/api/add/selecteduser/";


    public UserItemSearchAdapter(List<UserItemList> userItemLists, Context context,FragmentManager manager) {
        this.userItemLists = userItemLists;
        this.context = context;
        this.manager = manager;

    }
    private  FragmentManager manager;
    private Context context;

    @Override
    public UserItemSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserItemSearchAdapter.ViewHolder holder, int position) {
        final UserItemList userItemList = userItemLists.get(position);

        holder.textsearch.setText(userItemList.getUserName());

        Picasso.with(context)
                .load(userItemList.getUserImg())
               .into(holder.imageView);

       holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(context,"Selected", Toast.LENGTH_SHORT).show();
                goToSelectedUser(userItemList.getId());
            }
        });

       holder.addOrganizer.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {


              Intent intent = new Intent("custom-message");
              // intent.putExtra("organizerImgUrl",userItemList.getUserImg());
               intent.putExtra("organizerName",userItemList.getUserName());
               intent.putExtra("id",userItemList.getId());
               intent.putExtra("tag",userItemList.getTag());
               intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
               LocalBroadcastManager.getInstance(context).sendBroadcast(intent);



           }
       });

    }

    @Override
    public int getItemCount() {
        return userItemLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textsearch;
        public ImageView imageView;
        public LinearLayout linearLayout;
        public Button addOrganizer;

        public ViewHolder(View itemView) {
            super(itemView);

            textsearch = (TextView) itemView.findViewById(R.id.searchusername);
            imageView = (ImageView) itemView.findViewById(R.id.userpic);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.userselectitem);
            addOrganizer = (Button) itemView.findViewById(R.id.addorganizer);






        }
    }

    public void setFilter(List<UserItemList> newList){
        userItemLists = new ArrayList<>();
        userItemLists.addAll(newList);
        notifyDataSetChanged();
    }


    public void goToSelectedUser(String id){

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("id",id.toString());


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,selectedUserInfoUrl,new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String msg = response.getString("msg");

                            if( msg.equals("success") ){

                                Toast.makeText(context,"Successfully", Toast.LENGTH_SHORT).show();

                                String id = response.getString("id");
                                String fname = response.getString("fname");
                                String imgurl = response.getString("imgurl");
                                String lname = response.getString("lname");
                                String fullName = fname + " " + lname;

                                Intent l = new Intent(context,SupplierViewFragment.class);


                                supplierViewFragment = new SupplierViewFragment();

                                Bundle b = new Bundle();
                                b.putString("id", id.toString());
                                b.putString("fullName", fullName.toString());
                                b.putString("imgurl", imgurl.toString());
                                supplierViewFragment.setArguments(b);
                                supplierViewFragment.show(manager,"Search_user_for_service");
                            }else{
                                Toast.makeText(context,"Incorrect email or password", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,"Connection Fail", Toast.LENGTH_SHORT).show();

                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);

    }





}
