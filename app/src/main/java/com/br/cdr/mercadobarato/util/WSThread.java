//package com.br.cdr.mercadobarato.util;
//
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.util.Log;
//
///**
// * Created by clebr on 05/03/2017.
// */
//
//public class WSThread extends AsyncTask<String, Void, String> {
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//    }
//
//    @Override
//    protected void onPostExecute(String json) {
//        super.onPostExecute(json);
//        Log.i("retorno", json);
//
//        if (json.length() > 0) {
//            new Intent().putExtra("json", json);
//        }
//    }
//
//    @Override
//    protected void onProgressUpdate(Void... values) {
//        super.onProgressUpdate(values);
//    }
//
//    //Roda em background
//    @Override
//    protected String doInBackground(String... params) {
//        try {
//            Log.i("WSThread", "Thread - executa webservice.");
//            String json;
//            WSClient wsClient = new WSClient(params[0]);
//            if (params[1].equalsIgnoreCase("post")) {
//                json = wsClient.post(params[2]);
//
//            } else {
//                json = wsClient.get();
//            }
//
//            return json;
//        } catch (Exception e) {
//            e.printStackTrace();
//
//            return "Problema ao chamar o webservice.";
//        }
//    }
//
//}
