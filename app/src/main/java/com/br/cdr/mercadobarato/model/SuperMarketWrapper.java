package com.br.cdr.mercadobarato.model;

import java.io.Serializable;

/**
 * Created by Rodrigojgxm on 2/19/2017.
 */

public class SuperMarketWrapper implements Serializable {

    private String id;
    private String name;
    private String mAddress;
    private Double mLat;
    private Double mLng;


    public SuperMarketWrapper() {

    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

