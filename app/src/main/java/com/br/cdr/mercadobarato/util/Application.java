package com.br.cdr.mercadobarato.util;

import android.support.multidex.MultiDexApplication;

import com.br.cdr.mercadobarato.R;
import com.br.cdr.mercadobarato.model.Product;
import com.br.cdr.mercadobarato.model.SuperMarketWrapper;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by douglas on 19/03/17.
 */

public class Application extends MultiDexApplication {

    private SuperMarketWrapper checkedSuperMarket;
    private HashMap<String, Product> shoppingList;

    @Override
    public void onCreate() {
        super.onCreate();
        this.shoppingList = new HashMap<>();
    }

    public SuperMarketWrapper getCheckSuperMarket() {
        return checkedSuperMarket;
    }

    public void setCheckedSuperMarket(SuperMarketWrapper checkedSuperMarket) {
        this.checkedSuperMarket = checkedSuperMarket;
    }

    public void addProductToShoppingList(Product product) {
        this.shoppingList.put(product.getBar_code(), product);
    }

    public HashMap<String, Product> getShoppingList() {
        return shoppingList;
    }
}