package com.example.apn.vortex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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

public class OrganizerRegister extends AppCompatActivity {

    EditText fname;
    EditText lname;
    EditText remail;
    EditText password;
    EditText rePassword;
    Button register;
    RequestQueue requestQueue;

    String url = "http://10.10.18.125:3000/register/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_register);

        requestQueue = Volley.newRequestQueue(this);
        fname = (EditText) findViewById(R.id.fname);
        lname = (EditText) findViewById(R.id.lname);
        remail = (EditText) findViewById(R.id.regEmail);
        password = (EditText) findViewById(R.id.regPassword);
        rePassword = (EditText) findViewById(R.id.reEnPassword);
        register = (Button)findViewById(R.id.register);

        setClearErrorsListeners();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();

            }
        });
    }
    // register class
    public void register(){
        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("fname",fname.getText().toString());
        jsonParams.put("lname",lname.getText().toString());
        jsonParams.put("email",remail.getText().toString());
        jsonParams.put("password",password.getText().toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,url,new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String msg = response.getString("msg");
                            if( msg.equals("success") ){
                                Toast.makeText(getApplicationContext(),"Successfully Registered", Toast.LENGTH_SHORT).show();
                                Intent o = new Intent(OrganizerRegister.this,MainActivity.class);
                                startActivity(o);
                            }else
                            {
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

    private void setClearErrorsListeners() {
        fname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fname.setError(null);
            }
        });

        lname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lname.setError(null);
            }
        });

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                password.setError(null);
            }
        });

        rePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rePassword.setError(null);
            }
        });

        remail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remail.setError(null);
            }
        });
    }
    // validations
    private void attemptRegister() {

        boolean okfname = isFnameEmpty();
        if (!okfname)
            return;

        boolean oklname = isLnameEmpty();
        if (!oklname)
            return;

        boolean okEmail = isValidEmail();
        if (!okEmail)
            return;

        boolean okPassword = isValidPassword();
        if (!okPassword)
            return;

        boolean all = okEmail && okPassword && okfname && oklname;

        if (all) {
            register();
        }
    }

    private boolean isFnameEmpty(){
        String vfname = fname.getText().toString();
        if (TextUtils.isEmpty(vfname)) {
            fname.setError(getString(R.string.error_field_required));
            fname.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isLnameEmpty(){
        String vlname = lname.getText().toString();
        if (TextUtils.isEmpty(vlname)) {
            lname.setError(getString(R.string.error_field_required));
            lname.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isValidEmail() {
        String email = remail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            remail.setError(getString(R.string.error_field_required));
            remail.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            remail.setError(getString(R.string.error_invalid_email));
            remail.requestFocus();
            return false;
        }

        return true;
    }

    private boolean isValidPassword() {
        String p1 = password.getText().toString();
        String p2 = rePassword.getText().toString();

        if (TextUtils.isEmpty(p1)) {
            password.setError(getString(R.string.error_field_required));
            password.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(p2)) {
            rePassword.setError(getString(R.string.error_field_required));
            rePassword.requestFocus();
            return false;
        } else if (!p1.equals(p2)) {
            rePassword.setError(getString(R.string.error_password_is_not_matching));
            rePassword.requestFocus();
            return false;
        }

        return true;
    }
}
