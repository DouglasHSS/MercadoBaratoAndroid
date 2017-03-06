package com.br.cdr.mercadobarato.util;


import com.br.cdr.mercadobarato.model.SuperMarketWrapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rodrigojgxm on 2/19/2017.
 */

public class GooglePlacesJsonParser {
    public static List<SuperMarketWrapper> parse(String data) {

        List<SuperMarketWrapper> superMarketWrapperList = new ArrayList<SuperMarketWrapper>();

        try {
            JSONObject jObj = new JSONObject(data);
            JSONArray marketList = jObj.getJSONArray("results");
            JSONObject market;
            JSONObject geometry;
            JSONObject location;
            Double lat;
            Double lng;

            for (int i = 0; i<marketList.length();i++){
                SuperMarketWrapper superMarketWrapper = new SuperMarketWrapper();
                market = marketList.getJSONObject(i);
                superMarketWrapper.setName(market.getString("name"));
                superMarketWrapper.setID(market.getString("place_id"));
                superMarketWrapper.setAddress(market.getString("vicinity"));
                geometry  = market.getJSONObject("geometry");
                location = geometry.getJSONObject("location");
                lat = location.getDouble(("lat"));
                lng = location.getDouble(("lng"));
                superMarketWrapper.setLat(lat);
                superMarketWrapper.setLng(lng);

                superMarketWrapperList.add(superMarketWrapper);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return superMarketWrapperList;
    }
}
