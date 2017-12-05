package com.example.apn.vortex;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by APN on 12/5/2017.
 */


import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserSessionManager {


    SharedPreferences pref;


    Editor editor;

    Context _context;


    int PRIVATE_MODE = 0;

    private static final String PREFER_NAME = "Voetexstore";

    private static final String IS_USER_LOGIN = "IsUserLoggedIn";


    public static final String KEY_PASSWORD = "password";


    public static final String KEY_EMAIL = "email";


    public UserSessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void createUserLoginSession(String email, String password){

        editor.putBoolean(IS_USER_LOGIN, true);


        editor.putString(KEY_EMAIL, email);


        editor.putString(KEY_PASSWORD, password);


        editor.commit();
    }


    public boolean checkLogin(){

        if(this.isUserLoggedIn()){
            return true;
        }
        return false;
    }


    public HashMap<String, String> getUserDetails(){


        HashMap<String, String> user = new HashMap<String, String>();


        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));

        return user;
    }

    public void logoutUser(){

        editor.clear();
        editor.commit();

    }

    public boolean isUserLoggedIn(){
        return pref.getBoolean(IS_USER_LOGIN, false);
    }
}
