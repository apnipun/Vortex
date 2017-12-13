package com.example.apn.vortex;

/**
 * Created by APN on 12/7/2017.
 */

public class EventListItem {

    private String head;
    private String desc;
    private String imgUrl;

    public EventListItem(String head, String desc,String imgUrl) {
        this.head = head;
        this.desc = desc;
        this.imgUrl = imgUrl;
    }

    public String getHead() {
        return head;
    }

    public String getDesc() {

        return desc;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}
