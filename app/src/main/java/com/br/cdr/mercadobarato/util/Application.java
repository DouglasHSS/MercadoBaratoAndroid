package com.br.cdr.mercadobarato.util;

import android.support.multidex.MultiDexApplication;

import com.br.cdr.mercadobarato.R;
import com.br.cdr.mercadobarato.model.Product;
import com.br.cdr.mercadobarato.model.SuperMarketWrapper;

import java.util.ArrayList;


/**
 * Created by douglas on 19/03/17.
 */

public class Application extends MultiDexApplication {

    private SuperMarketWrapper checkedSuperMarket;
    private ArrayList<Product> shoppingList;

    @Override
    public void onCreate() {
        super.onCreate();
        this.shoppingList = new ArrayList<Product>();
    }

    public SuperMarketWrapper getCheckSuperMarket() {
        return checkedSuperMarket;
    }

    public void setCheckedSuperMarket(SuperMarketWrapper checkedSuperMarket) {
        this.checkedSuperMarket = checkedSuperMarket;
    }

    public void addProductToShoppingList(Product product) {
        this.shoppingList.add(product);
    }

    public ArrayList<Product> getShoppingList() {
        return shoppingList;
    }
}