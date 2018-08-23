package com.example.apn.vortex;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.List;
import java.util.Map;

/**
 * Created by APN on 12/19/2017.
 */

public class SearchUsersFragment extends DialogFragment implements SearchView.OnQueryTextListener{

    String getUSerUrl ;

    String eventId,tag;
    private RecyclerView recyclerView;

    private UserItemSearchAdapter adapter;

    private List<UserItemList> userItemLists;

    SearchView searchView;

    FragmentManager manager;

    String tag2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        View rootView = inflater.inflate(R.layout.search_user_fragment,container);

        if (getArguments() != null) {
            tag = getArguments().getString("tag");
            eventId = getArguments().getString("id");
        }

        if(tag.equals("sltorg")){
          getUSerUrl  = "http://192.168.8.100:3000/api/event/users/";
          tag2 = "sltorg";

        }else {
            getUSerUrl = "http://192.168.8.100:3000/api/add/getsearchserviceproviders/";
            tag2 = "sltsp";
        }

        searchView = (SearchView) rootView.findViewById(R.id.searchview);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.useritem);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        userItemLists = new ArrayList<>();


        loadRecyclerViewData();
        this.getDialog().setTitle("Select Person");
        searchView.setOnQueryTextListener(this);

         manager = getFragmentManager();

        return rootView;
    }



    private void loadRecyclerViewData(){

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("eventId",eventId.toString());


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,getUSerUrl,new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("users");

                            for (int i = 0 ;i<array.length(); i++){
                                JSONObject o = array.getJSONObject(i);
                                final String name = o.getString("firstname") + " " + o.getString("lastname");
                                UserItemList userItemList = new UserItemList(o.getString("_id"),name,o.getString("imgurl"),tag2);
                                userItemLists.add(userItemList);
                            }
                            adapter = new UserItemSearchAdapter(userItemLists,getActivity().getApplicationContext(),manager);
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(),"Connection Fails", Toast.LENGTH_SHORT).show();
                    }
                }

        );

        RequestQueue requestQueue = Volley.newRequestQueue(this.getActivity());
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        List<UserItemList> newList = new ArrayList<>();
        for(UserItemList userItemList : userItemLists){
            String name = userItemList.getUserName().toLowerCase();
            if(name.contains(newText))
                newList.add(userItemList);
        }
        adapter.setFilter(newList);
        return true;
    }


}


