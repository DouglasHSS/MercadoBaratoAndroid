package com.br.cdr.mercadobarato.model;

import java.io.Serializable;

/**
 * Created by clebr on 11/03/2017.
 */

public class ProductWrapper implements Serializable {

    private static final long serialVersionUID = 21L;

    private String bar_code;
    private String description;
    private double price;

    public String getBar_code() {
        return bar_code;
    }

    public ProductWrapper setBar_code(String bar_code) {
        this.bar_code = bar_code;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductWrapper setDescription(String description) {
        this.description = description;
        return this;
    }

    public double getPrice() {
        return price;
    }

    public ProductWrapper setPrice(double price) {
        this.price = price;
        return this;
    }
}
