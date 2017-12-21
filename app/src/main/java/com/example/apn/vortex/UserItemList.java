package com.example.apn.vortex;

/**
 * Created by APN on 12/17/2017.
 */

public class UserItemList {

    private String id;
    private String userName;
    private String userImg;

    public String getUserImg() {
        return userImg;
    }
    public String getId() {return id;}
    public String getUserName() {
        return userName;
    }

    public UserItemList(String id,String userName,String userImg) {
        this.id = id;
        this.userName = userName;
        this.userImg = userImg;
    }


}
