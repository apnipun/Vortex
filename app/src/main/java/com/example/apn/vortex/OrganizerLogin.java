package com.example.apn.vortex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Map;

public class OrganizerLogin extends AppCompatActivity {

    EditText emailInput;
    EditText pswrdInput;
    Button login;
    Button register;
    RequestQueue requestQueue;

    String url = "http://192.168.1.100:3000/login/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_login);


        emailInput = (EditText)findViewById(R.id.email);
        pswrdInput = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.button);
        requestQueue = Volley.newRequestQueue(this);
        register = (Button) findViewById(R.id.loginRegister);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent r = new Intent(OrganizerLogin.this,OrganizerRegister.class);
                startActivity(r);

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

        //Back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
 //login class
    public void userLogin(){


        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("email",emailInput.getText().toString());
        jsonParams.put("password",pswrdInput.getText().toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,url,new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String msg = response.getString("msg");

                            if( msg.equals("success") ){

                                String id = response.getString("id");
                                String fname = response.getString("fname");
                                String imgurl = response.getString("imgurl");
                                Toast.makeText(getApplicationContext(),"Login successfully", Toast.LENGTH_SHORT).show();
                                Intent l = new Intent(OrganizerLogin.this,Profile.class);
                                Bundle b = new Bundle();
                                b.putString("id", id.toString());
                                b.putString("fname", fname.toString());
                                b.putString("imgurl", imgurl.toString());
                                l.putExtras(b);
                                startActivity(l);
                            }else{
                                Toast.makeText(getApplicationContext(),"Incorrect email or password", Toast.LENGTH_SHORT).show();
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


        requestQueue.add(jsonObjectRequest);
    }

    //Back button Operation
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
