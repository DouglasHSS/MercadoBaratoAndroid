package com.br.cdr.mercadobarato.model;

import java.io.Serializable;

/**
 * Created by clebr on 11/03/2017.
 */

public class Product implements Serializable {

    private static final long serialVersionUID = 21L;

    private String bar_code;
    private String description;
//    private String price;

    public String getBar_code() {
        return bar_code;
    }

    public void setBar_code(String bar_code) {
        this.bar_code = bar_code;
    }

    public String getDescription() {
        return description;
    }

    public Product setDescription(String description) {
        this.description = description;
        return this;
    }

//    public String getPrice() {
//        return price;
//    }
//
//    public Product setPrice(String price) {
//        this.price = price;
//        return this;
//    }
}
