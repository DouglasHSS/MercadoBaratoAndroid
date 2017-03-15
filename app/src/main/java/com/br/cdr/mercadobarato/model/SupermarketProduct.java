package com.br.cdr.mercadobarato.model;

/**
 * Created by clebr on 15/03/2017.
 */

public class SupermarketProduct {

    private SuperMarket superMarket;
    private Product product;
    private String price;

    public SuperMarket getSuperMarket() {
        return superMarket;
    }

    public SupermarketProduct setSuperMarket(SuperMarket superMarket) {
        this.superMarket = superMarket;
        return this;
    }

    public Product getProduct() {
        return product;
    }

    public SupermarketProduct setProduct(Product product) {
        this.product = product;
        return this;
    }

    public String getPrice() {
        return price;
    }

    public SupermarketProduct setPrice(String price) {
        this.price = price;
        return this;
    }
}
