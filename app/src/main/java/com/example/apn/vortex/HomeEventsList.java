package com.example.apn.vortex;

/**
 * Created by APN on 1/27/2018.
 */

public class HomeEventsList {
    private String Name,ImageURL,time,date,descriptopn;

    public HomeEventsList(String name, String imageURL, String time, String date, String descriptopn) {
        this.Name = name;
        this.ImageURL = imageURL;
        this.time = time;
        this.date = date;
        this.descriptopn = descriptopn;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescriptopn() {
        return descriptopn;
    }

    public void setDescriptopn(String descriptopn) {
        this.descriptopn = descriptopn;
    }
}
