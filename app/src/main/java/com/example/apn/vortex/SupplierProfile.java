package com.example.apn.vortex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SupplierProfile extends AppCompatActivity {

    CircleImageView profilePic,addAdz;
    TextView userName;
    String profilePicUrl,id,fname;
    Bitmap bitmap;
  //  UserSessionManager session;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<SupplierAdzList> supplierAdzLists;

    String adzUrl = "http://192.168.8.100:3000/api/add/getadzdata/";
    String uploadImgUrl = "http://192.168.8.100:3000/api/profile/updateProfilePicture/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_profile);


            profilePic = (CircleImageView) findViewById(R.id.profilepic);
            addAdz = (CircleImageView) findViewById(R.id.addevent);
            userName = (TextView) findViewById(R.id.username);

            recyclerView = (RecyclerView) findViewById(R.id.recyclerViewforsupplier);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

           // session = new UserSessionManager(getApplicationContext());

            Bundle bundle = getIntent().getExtras();
            id = bundle.getString("id");
            fname = bundle.getString("fullName");
            profilePicUrl = bundle.getString("imgurl");

            userName.setText(fname);


            setProfilePic();
            profilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, 100);
                }
            });


            addAdz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent o = new Intent(SupplierProfile.this,CreateAdzForSupplier.class);
                    Bundle b = new Bundle();
                    b.putString("id", id.toString());
                    b.putString("fullName", fname.toString());
                    b.putString("imgurl", profilePicUrl.toString());
                    o.putExtras(b);
                    o.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(o);
                }
            });


            supplierAdzLists = new ArrayList<>();
            loadRecyclerViewData();

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 100 && resultCode == RESULT_OK && data != null) {


                Uri imageUri = data.getData();
                try {

                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                    profilePic.setImageBitmap(bitmap);

                    uploadBitmap(bitmap);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        public byte[] getFileDataFromDrawable(Bitmap bitmap) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }


    private void uploadBitmap(final Bitmap bitmap) {


        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,uploadImgUrl,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(),  obj.getString("msg"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userId",id);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("uploads[]", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };


        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }


    private void loadRecyclerViewData(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("id",id.toString());


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,adzUrl,new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
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
                            adapter = new SupplierAdzAdapter(supplierAdzLists,SupplierProfile.this);
                            recyclerView.setAdapter(adapter);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setProfilePic(){
        Picasso.with(getApplicationContext()).load(profilePicUrl).into(profilePic);

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent x =  new Intent(SupplierProfile.this,MainActivity.class);
        x.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK | android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(x);
        //finish();
    }

}

