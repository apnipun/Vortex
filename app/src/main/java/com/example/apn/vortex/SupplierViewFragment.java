package com.example.apn.vortex;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by APN on 1/26/2018.
 */

public class SupplierViewFragment extends DialogFragment {

    CircleImageView profilePic;
    TextView userName;
    String profilePicUrl,id,fname;
    Bitmap bitmap;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<SupplierAdzList> supplierAdzLists;

    String adzUrl = "http://192.168.8.100:3000/api/add/getadzdata/";



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        View rootView = inflater.inflate(R.layout.supplier_view_fragment,container);

        profilePic = (CircleImageView) rootView.findViewById(R.id.profilepic);

        userName = (TextView) rootView.findViewById(R.id.username);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewforsupplier);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));


        if (getArguments() != null) {
            id = getArguments().getString("id");
            fname = getArguments().getString("fullName");
            profilePicUrl = getArguments().getString("imgurl");
        }

        userName.setText(fname);

        this.getDialog().setTitle("Select Person Details");
        setProfilePic();
        supplierAdzLists = new ArrayList<>();
        loadRecyclerViewData();

        return rootView;
    }


    private void loadRecyclerViewData(){

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("id",id.toString());


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,adzUrl,new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray array = response.getJSONArray("adz");

                            for (int i = 0 ;i<array.length(); i++){
                                JSONObject o = array.getJSONObject(i);
                                SupplierAdzList adzItem = new SupplierAdzList(
                                        o.getString("adzname"),
                                        o.getString("priceforservice"),
                                        o.getString("adzdescription"),
                                        o.getString("contactnumbers"),
                                        o.getString("adzpicurl")
                                );
                                supplierAdzLists.add(adzItem);
                            }
                            adapter = new SupplierAdzAdapter(supplierAdzLists,SupplierViewFragment.this.getActivity());
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(SupplierViewFragment.class,"Connection Fail", Toast.LENGTH_SHORT).show();

                    }
                }

        );

        RequestQueue requestQueue = Volley.newRequestQueue(this.getActivity());
        requestQueue.add(jsonObjectRequest);

    }

    public void setProfilePic(){
        Picasso.with(getActivity()).load(profilePicUrl).into(profilePic);

    }

}
