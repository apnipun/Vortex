package com.example.apn.vortex;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageEvent extends AppCompatActivity{
    Button test,chatForEvent,addSearchOrganizers;
    RecyclerView.Adapter adapter,adapterForSupplier;
    RecyclerView addOrganizersRecyclerView,addSupplierRecyclerView;
    List<AddOrganizersListItem> addOrganizersListItems,addSupplierListItem;
    String organizerId,organizer,addedorganizerImgUrl,deleteOrganizerId;
    String id ;
    String eventId,tag,eventName,userName;

    Spinner searchServiceProviderType;


    String deleteOrganizerUrl = "http://192.168.8.100:3000/api/event/deleteorganizers/";
    String selectedOrganizerUrl = "http://192.168.8.100:3000/api/event/addedorganizsers/";
    String addedOrganizersUrl = "http://192.168.8.100:3000/api/event/getaddedorganizsers/";
    String addServiceProvidersURL = "http://192.168.8.100:3000/api/add/addserviceproviers/";
    String selectServiceProviderURL = "http://192.168.8.100:3000/api/add/getaddserviceproviders/";
    String deleteSupplierURL = "http://192.168.8.100:3000/api/add/deletesupplierproviders/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_event);

        test = (Button) findViewById(R.id.test);
        chatForEvent = (Button) findViewById(R.id.chatbtn);


        Bundle bundle = getIntent().getExtras();

        id = bundle.getString("userId");
        userName = bundle.getString("userName");
        eventId = bundle.getString("eventId");
        eventName = bundle.getString("ename");
        searchServiceProviderType = (Spinner) findViewById(R.id.spcategory);
        addSearchOrganizers = (Button) findViewById(R.id.addserviceproviders);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(ManageEvent.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.spType));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchServiceProviderType.setAdapter(myAdapter);


        addOrganizersListItems = new ArrayList<>();
        addSupplierListItem = new ArrayList<>();
        addOrganizersRecyclerView = (RecyclerView) findViewById(R.id.addorganizerrecyclerView);
        addOrganizersRecyclerView.setHasFixedSize(true);
        addOrganizersRecyclerView.setNestedScrollingEnabled(false);
        addOrganizersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        addSupplierRecyclerView = (RecyclerView) findViewById(R.id.addsupplierrecyclerView);
        addSupplierRecyclerView.setHasFixedSize(true);
        addSupplierRecyclerView.setNestedScrollingEnabled(false);
        addSupplierRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,new IntentFilter("custom-message"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver2,new IntentFilter("custom-message2"));

        final FragmentManager fm = getFragmentManager();
        final SearchUsersFragment searchUsersFragment = new SearchUsersFragment();

       test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("tag","sltorg");
                bundle.putString("id",eventId.toString());
                searchUsersFragment.setArguments(bundle);
                searchUsersFragment.show(fm,"Search_users");
            }
        });

       addSearchOrganizers.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Bundle bundle = new Bundle();
               bundle.putString("tag","sltsp");
               bundle.putString("id",searchServiceProviderType.getSelectedItem().toString());
               searchUsersFragment.setArguments(bundle);
               searchUsersFragment.show(fm,"Search_Service_providers");
           }
       });


       loadAddedOrganizerRecyclerViewData();
       loadAddedSupplierRecyclerViewData();

        //Back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        chatForEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ChatForEvent.class);
                intent.putExtra("UserName",userName.toString());
                intent.putExtra("eventName",eventName.toString());
                startActivity(intent);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }


    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver2);

    }


    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //addedorganizerImgUrl = intent.getStringExtra("organizerImgurl");
            organizer = intent.getStringExtra("organizerName");
            organizerId = intent.getStringExtra("id");
            tag = intent.getStringExtra("tag");

            if(tag.equals("sltorg")){
                addOrganizersListItems.add(new AddOrganizersListItem(organizerId.toString(),organizer.toString(),"Organizer"));
                adapter = new AddOragnizerAdapter( addOrganizersListItems,ManageEvent.this);
                addOrganizersRecyclerView.setAdapter(adapter);
                String action = intent.getAction();
                if(action != null) {
                    addSelectOrganizer(id,eventId,organizerId,organizer);
                    action = null;
                }
            }else {
                addSupplierListItem.add(new AddOrganizersListItem(organizerId.toString(),organizer.toString(),searchServiceProviderType.getSelectedItem().toString()));
                adapterForSupplier = new AddOragnizerAdapter( addSupplierListItem,ManageEvent.this);
                addSupplierRecyclerView.setAdapter(adapterForSupplier);
                String action = intent.getAction();
                if(action != null) {
                    addSelecctServiceProviders(id,eventId,organizerId,organizer);
                    action = null;
                }

            }



        }


    };

    public BroadcastReceiver mMessageReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //addedorganizerImgUrl = intent.getStringExtra("organizerImgurl");
           // organizer = intent.getStringExtra("organizerName");
            deleteOrganizerId = intent.getStringExtra("organizerId");
            String key = intent.getStringExtra("userType");

            if(key.equals("Organizer")){
                deleteOrganizers(id,eventId,deleteOrganizerId);
            }else {
                deleteSuppliers(id,eventId,deleteOrganizerId);
            }


        }


    };

    public void addSelectOrganizer(String userId,String eventId,String organizerId,String organizerName){

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("userId",userId.toString());
        jsonParams.put("eventId",eventId.toString());
        jsonParams.put("organizerId",organizerId.toString());
        jsonParams.put("organizerName",organizerName.toString());



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,selectedOrganizerUrl,new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String msg = response.getString("msg");

                            if( msg.equals("success") ){
                                Toast.makeText(getApplicationContext(),"Successfully Added", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(),"Fail", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Connection Fail", Toast.LENGTH_SHORT).show();

                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);

    }

    public void addSelecctServiceProviders(String userId,String eventId,String organizerId,String organizerName){

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("userId",userId.toString());
        jsonParams.put("eventId",eventId.toString());
        jsonParams.put("organizerId",organizerId.toString());
        jsonParams.put("organizerName",organizerName.toString());



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,addServiceProvidersURL,new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String msg = response.getString("msg");

                            if( msg.equals("success") ){
                                Toast.makeText(getApplicationContext(),"Successfully Added", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(),"Fail", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Connection Fail", Toast.LENGTH_SHORT).show();

                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);

    }



    public void deleteOrganizers(String userId,String eventId,String organizerId){

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("eventId",eventId.toString());
        jsonParams.put("organizerid",organizerId.toString());




        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,deleteOrganizerUrl,new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String msg = response.getString("msg");

                            if( msg.equals("success") ){
                                Toast.makeText(getApplicationContext(),"Successfully deleted", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(),"Cannot delete ", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Connection Fail", Toast.LENGTH_SHORT).show();

                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);

    }


    public void deleteSuppliers(String userId,String eventId,String organizerId){

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("eventId",eventId.toString());
        jsonParams.put("organizerid",organizerId.toString());




        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,deleteSupplierURL,new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String msg = response.getString("msg");

                            if( msg.equals("success") ){
                                Toast.makeText(getApplicationContext(),"Successfully deleted", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(),"Cannot delete ", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Connection Fail", Toast.LENGTH_SHORT).show();

                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);

    }




    private void loadAddedOrganizerRecyclerViewData(){

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("userId",id.toString());
        jsonParams.put("eventId",eventId.toString());


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,addedOrganizersUrl,new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray array = response.getJSONArray("organizers");

                            for (int i = 0 ;i<array.length(); i++){
                                JSONObject o = array.getJSONObject(i);
                                AddOrganizersListItem addOrganizersListItem = new AddOrganizersListItem(
                                        o.getString("_id"),
                                        o.getString("firstname")+" "+o.getString("lastname"),
                                        "Organizer"
                                );
                                addOrganizersListItems.add(addOrganizersListItem);
                            }
                            adapter = new AddOragnizerAdapter(addOrganizersListItems,getApplicationContext());
                            addOrganizersRecyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Connection Fail", Toast.LENGTH_SHORT).show();

                    }
                }

        );

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);

    }


    private void loadAddedSupplierRecyclerViewData(){

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("userId",id.toString());
        jsonParams.put("eventId",eventId.toString());


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,selectServiceProviderURL,new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray array = response.getJSONArray("organizers");

                            for (int i = 0 ;i<array.length(); i++){
                                JSONObject o = array.getJSONObject(i);
                                AddOrganizersListItem addOrganizersListItem = new AddOrganizersListItem(
                                        o.getString("_id"),
                                        o.getString("firstname")+" "+o.getString("lastname"),
                                       o.getString("spCatagory")
                                );
                                addSupplierListItem.add(addOrganizersListItem);
                            }
                            adapterForSupplier = new AddOragnizerAdapter(addSupplierListItem,getApplicationContext());
                            addSupplierRecyclerView.setAdapter(adapterForSupplier);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Connection Fail", Toast.LENGTH_SHORT).show();

                    }
                }

        );

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


}

