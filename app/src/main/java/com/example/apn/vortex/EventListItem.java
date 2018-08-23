package com.example.apn.vortex;

/**
 * Created by APN on 12/7/2017.
 */

public class EventListItem {

    private String userName;
    private String userId;
    private String eventId;
    private String head;
    private String desc;
    private String imgUrl;
    private String desription;
    private String time;


    public EventListItem(String userId,String userName,String eventId,String head, String desc,String imgUrl,String time,String desription) {
        this.userId = userId;
        this.userName = userName;
        this.eventId = eventId;
        this.head = head;
        this.desc = desc;
        this.imgUrl = imgUrl;
        this.time = time;
        this.desription = desription;
    }
    public String getUserId() {return userId;}

    public String getUserName() {return userName;}

    public String getEventId() {return eventId;}

    public String getHead() {
        return head;
    }

    public String getDesc() {return desc;}

    public String getImgUrl() {
        return imgUrl;
    }

    public String getDesription() {
        return desription;
    }

    public String getTime() {
        return time;
    }
}
