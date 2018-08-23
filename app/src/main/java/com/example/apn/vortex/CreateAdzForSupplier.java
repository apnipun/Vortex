package com.example.apn.vortex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CreateAdzForSupplier extends AppCompatActivity {

    RequestQueue requestQueue;
    Button adzPhoto,createAdz;
    Bitmap bitmap;
    String id,imgurl,fname;

    String createventUrl = "http://192.168.8.100:3000/api/add/createadzforsupplier/";

    EditText adzName,adzDescription,priceForService,contactNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_adz_for_supplier);


            adzName = (EditText) findViewById(R.id.editText4);
            adzDescription = (EditText) findViewById(R.id.editText10);
            priceForService = (EditText) findViewById(R.id.editText8);
            contactNumbers = (EditText) findViewById(R.id.editText11);
            adzPhoto  = (Button) findViewById(R.id.addadz);
            createAdz  = (Button) findViewById(R.id.createadz);


            requestQueue = Volley.newRequestQueue(this);

            Bundle bundle = getIntent().getExtras();
            id = bundle.getString("id");
            fname = bundle.getString("fullName");
            imgurl = bundle.getString("imgurl");

            adzPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, 100);
                }
            });

            createAdz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uploadData(bitmap);
                }
            });

            //Back button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 100 && resultCode == RESULT_OK && data != null) {


                Uri imageUri = data.getData();
                try {

                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

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


    private void uploadData(final Bitmap bitmap) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating...");
        progressDialog.show();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,createventUrl,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(),  obj.getString("msg"), Toast.LENGTH_SHORT).show();

                            id = obj.getString("id");
                            fname = obj.getString("fname");
                            imgurl = obj.getString("imgurl");

                            Intent l = new Intent(getApplicationContext(),SupplierProfile.class);
                            Bundle b = new Bundle();
                            b.putString("id", id.toString());
                            b.putString("fullName", fname.toString());
                            b.putString("imgurl", imgurl.toString());
                            l.putExtras(b);
                            startActivity(l);


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
                params.put("id",id.toString());
                params.put("adzname",adzName.getText().toString());
                params.put("priceforservice",priceForService.getText().toString());
                params.put("adzdescription",adzDescription.getText().toString());
                params.put("contactnumbers",contactNumbers.getText().toString());
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("adzPic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };


        Volley.newRequestQueue(this).add(volleyMultipartRequest);

    }

    //back button operation
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent x =  new Intent(CreateAdzForSupplier.this,SupplierProfile.class);
        Bundle b = new Bundle();
        b.putString("id", id.toString());
        b.putString("fullName", fname.toString());
        b.putString("imgurl", imgurl.toString());
        x.putExtras(b);
        startActivity(x);
    }

}

