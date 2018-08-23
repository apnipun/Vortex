package com.example.apn.vortex;

/**
 * Created by APN on 11/22/2017.
 */
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.apn.vortex.Common.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Splashscreen extends Activity {
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    String homeEvntsURL = "http://192.168.8.100:3000/api/event/geteventsdataforhome/";

    Thread splashTread;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        Common.homeEventsLists.add(new HomeEventsList("name1","https://media.istockphoto.com/photos/parrots-image-picture-id485745110?k=6&m=485745110&s=612x612&w=0&h=OpjyQaAhHiatImtSQPIdUT8gkSzrG1PQl6gEJgpbFkk=",
                "20.00","2018","description"));
        Common.homeEventsLists.add(new HomeEventsList("name2","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRxut4j98oB6CBzDKoLjvUy-mSlXvdOAprM3-vAB5uHrR2I81pz",
                "20.01","2018","description"));
        Common.homeEventsLists.add(new HomeEventsList("name3","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTlDUVa7FVSUSE0AdXYC_mMvScJPdHKlc_jzm_rn34Tz_LQwR_5","20.01","2018","description"));

        initData();



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            finish();
            startActivity(intent);
            return;
        }


        StartAnimations();
    }


    private void initData() {
        // Common.homeEventsLists.add(new HomeEventsList("Cscscs","https://engineering.unl.edu/images/staff/Kayla_Person-small.jpg","dcdcdc","dcdcd","ccdc"));
       // final ProgressDialog progressDialog = new ProgressDialog(this);
       // progressDialog.setMessage("Loading Data...");
      //  progressDialog.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,homeEvntsURL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       // progressDialog.dismiss();
                        try {
                            JSONArray array = response.getJSONArray("events");

                            for (int i = 0 ;i<array.length(); i++){
                                JSONObject o = array.getJSONObject(i);
                                HomeEventsList homeEventsList = new HomeEventsList(
                                        o.getString("eventname"),
                                        o.getString("imgurl"),
                                        o.getString("time"),
                                        o.getString("Date"),
                                        o.getString("eventDiscription")
                                );
                                Common.homeEventsLists.add(homeEventsList);
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




    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l=(LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.splash);
        iv.clearAnimation();
        iv.startAnimation(anim);

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;

                    while (waited < 3500) {
                        sleep(100);
                        waited += 100;
                    }
                    Intent intent = new Intent(Splashscreen.this,
                            MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    Splashscreen.this.finish();
                } catch (InterruptedException e) {

                } finally {
                    Splashscreen.this.finish();
                }

            }
        };
        splashTread.start();

    }

}