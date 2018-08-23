package com.example.apn.vortex;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreateEvents extends AppCompatActivity implements View.OnClickListener {
    //UserSessionManager session;
    RequestQueue requestQueue;
    EditText eventDate,eventTime;
    Button themePhoto,createEvent;
    Bitmap bitmap;
    String id,imgurl,fname,addeventName,password;

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();

    String createventUrl = "http://192.168.8.100:3000/api/event/createvent2/";

    private int mYear, mMonth, mDay, mHour, mMinute;

    Spinner eventtype;

    String etype;

    EditText eventName, eventLocation, eventDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_events);



        eventName = (EditText) findViewById(R.id.editText3);
        eventtype = (Spinner) findViewById(R.id.editText);
        eventLocation = (EditText) findViewById(R.id.editText7);
        eventDescription = (EditText) findViewById(R.id.editText2);
        themePhoto  = (Button) findViewById(R.id.add);
        createEvent  = (Button) findViewById(R.id.create);
        eventDate = (EditText) findViewById(R.id.editText5);
        eventTime = (EditText) findViewById(R.id.editText6);

      //  session = new UserSessionManager(getApplicationContext());
        requestQueue = Volley.newRequestQueue(this);


        eventDate.setOnClickListener(this);
        eventTime.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        fname = bundle.getString("fullName");
        imgurl = bundle.getString("imgurl");
        addeventName = bundle.getString("eventName");
        password = bundle.getString("password");

        eventName.setText(addeventName);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(CreateEvents.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.eventType));
                myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                eventtype.setAdapter(myAdapter);

        etype = eventtype.getSelectedItem().toString();

       themePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });

       createEvent.setOnClickListener(new View.OnClickListener() {
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

                            Intent l = new Intent(getApplicationContext(),Profile.class);
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
                params.put("ename",eventName.getText().toString());
                params.put("edate",eventDate.getText().toString());
                params.put("etime",eventTime.getText().toString());
                params.put("eventType",etype.toString());
                params.put("location",eventLocation.getText().toString());
                params.put("edescription",eventDescription.getText().toString());
                params.put("password",password.toString());
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

        {
            Map<String,Object> map = new HashMap<String,Object>();
            map.put(eventName.getText().toString(),"");
            root.updateChildren(map);

        }
    }


    @Override
    public void onClick(View v) {

        if (v == eventDate) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            eventDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == eventTime) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            eventTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }

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
        Intent x =  new Intent(CreateEvents.this,Profile.class);
        Bundle b = new Bundle();
        b.putString("id", id.toString());
        b.putString("fullName", fname.toString());
        b.putString("imgurl", imgurl.toString());
        x.putExtras(b);
        startActivity(x);
    }
}