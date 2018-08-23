package com.example.apn.vortex;

/**
 * Created by APN on 12/17/2017.
 */

public class UserItemList {

    private String id;
    private String userName;
    private String userImg;
    private String tag;

    public String getUserImg() {
        return userImg;
    }
    public String getId() {return id;}
    public String getUserName() {
        return userName;
    }

    public String getTag() {
        return tag;
    }

    public UserItemList(String id, String userName, String userImg, String tag) {
        this.id = id;
        this.userName = userName;
        this.userImg = userImg;
        this.tag = tag;
    }


}
