package com.br.cdr.mercadobarato.util;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.br.cdr.mercadobarato.model.UserWrapper;
import com.google.gson.Gson;

/**
 * Created by clebr on 05/03/2017.
 */

public class WSThread extends AsyncTask<String, Void, String> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    //Popula campos do formulário
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("retorno", s);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    //Roda em background
    @Override
    protected String doInBackground(String... params) {
        try {
            Log.i("WSThread", "Thread - executa webservice.");
            String json;
            WSClient wsClient = new WSClient(params[0]);
            if (params[1].equalsIgnoreCase("post")) {
                json = wsClient.post(params[3]);

            } else {
                json = wsClient.get();
            }

            if (params[2].equalsIgnoreCase("login")) {
                validateLogin(json);
            }
            return json;
        } catch (Exception e) {
            e.printStackTrace();

            return "Problema ao chamar o webservice.";
        }
    }

    private void validateLogin(String json) {
        Gson gson = new Gson();
        UserWrapper user = gson.fromJson(json, UserWrapper.class); // converte de json para UserWrapper
        Intent intent = new Intent();
        intent.putExtra("user", user);
    }
}
