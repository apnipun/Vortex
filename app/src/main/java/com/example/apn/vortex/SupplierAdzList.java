package com.example.apn.vortex;

/**
 * Created by APN on 1/22/2018.
 */

public class SupplierAdzList {


    private String adzName;
    private String priceFroService;
    private String adzDescription;
    private String contactNumber;
    private String adzUrl;

    public SupplierAdzList(String adzName, String priceFroService,String adzDescription,String contactNumber,String adzUrl) {
        this.adzName = adzName;
        this.priceFroService = priceFroService;
        this.adzDescription = adzDescription;
        this.contactNumber = contactNumber;
        this.adzUrl = adzUrl;
    }

    public String getAdzName() {
        return adzName;
    }

    public String getPriceFroService() {
        return priceFroService;
    }

    public String getAdzDescription() {
        return adzDescription;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getAdzUrl() {
        return adzUrl;
    }
}
