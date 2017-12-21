package com.example.apn.vortex;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ManageEvent extends AppCompatActivity{
    Button test,test2;
    ArrayList<String> itemList,itemListId;
    JSONArray a,b;
    ArrayAdapter<String> adapter;
    ListView lv;
    String organizerId,organizer;
    String id = "5a363162a3e0f5336ce296ad";
    String eventId = "5a3a201f24e3330eb8578bb2";

    String addOrganizerurl = "http://10.10.28.104:3000/sendnotification/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_event);

        test = (Button) findViewById(R.id.test);
        test2 = (Button) findViewById(R.id.test2);
        lv = (ListView) findViewById(R.id.organizerlist);
        itemList = new ArrayList<>();
        itemListId = new ArrayList<>();



        adapter = new ArrayAdapter<String>(ManageEvent.this,android.R.layout.simple_list_item_multiple_choice,itemList);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                SparseBooleanArray positionchecker = lv.getCheckedItemPositions();

                int count = lv.getCount();

                for(int item = count - 1;item >=0;item --){
                    if(positionchecker.get(item)){
                        adapter.remove(itemList.get(item));
                        itemListId.remove(item);
                    }
                }

                positionchecker.clear();
                adapter.notifyDataSetChanged();

                return false;
            }
        });
        lv.setAdapter(adapter);

        for(int i=0;i<itemList.size();i++){

        }


        final FragmentManager fm = getFragmentManager();
        final SearchUsersFragment searchUsersFragment = new SearchUsersFragment();

       test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchUsersFragment.show(fm,"Search_users");
            }
        });

        test2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),itemList.toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),itemListId.toString(), Toast.LENGTH_SHORT).show();
                addOraganizers();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    private void addOraganizers(){

        JSONArray jArrayInput=new JSONArray();
        JSONObject jObjectInput=new JSONObject();
        a = new JSONArray(itemList);
        b = new JSONArray(itemListId);
        try {
            jObjectInput.put("id",id);
            jObjectInput.put("eventId",eventId);
            jObjectInput.put("organizerIdList",b);
            jObjectInput.put("organizerNameList",a);
            jArrayInput.put(jObjectInput);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,addOrganizerurl,jObjectInput,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String msg = response.getString("msg");
                            if( msg.equals("success") ){
                               // Toast.makeText(getApplicationContext(),"Successfully Registered", Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            organizer = intent.getStringExtra("organizerName");
            organizerId = intent.getStringExtra("id");
            itemList.add(organizer);
            itemListId.add(organizerId);
            adapter.notifyDataSetChanged();
        }
    };
}

