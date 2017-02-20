package com.br.cdr.mercadobarato.model;

/**
 * Created by Rodrigojgxm on 2/19/2017.
 */

public class SuperMarketWrapper {

    private String mID;
    private String mName;
    private String mAddress;
    private Double mLat;
    private Double mLng;


    public SuperMarketWrapper() {

    }

    public String getID() {
        return mID;
    }

    public void setID(String mID) {
        this.mID = mID;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public Double getLat() {
        return mLat;
    }

    public void setLat(Double mLat) {
        this.mLat = mLat;
    }

    public Double getLng() {
        return mLng;
    }

    public void setLng(Double mLng) {
        this.mLng = mLng;
    }


}

