package com.example.apn.vortex;

/**
 * Created by APN on 12/23/2017.
 */

public class AddOrganizersListItem {

    private String organizerId;
    private String organizerName;
    private String organizerImUrl;
    private String userType;

    public AddOrganizersListItem(String organizerId,String organizerName,String userType){
        this.organizerId = organizerId;
        this.organizerName = organizerName;
        this.userType = userType;
    }


    public String getOrganizerId() {
        return organizerId;
    }

    public String getOrganizerName() {
        return organizerName;
    }
    public String getOrganizerImUrl() {
        return organizerImUrl;
    }

    public String getUserType() {
        return userType;
    }
}
