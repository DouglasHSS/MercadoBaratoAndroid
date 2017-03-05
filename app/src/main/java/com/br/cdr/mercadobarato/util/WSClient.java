package com.br.cdr.mercadobarato.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/**
 * Created by clebr on 04/03/2017.
 */

public class WSClient {

    private static final int JSON_CONNECTION_TIMEOUT = 3000;
    private static final int JSON_SOCKET_TIMEOUT = 5000;

    private String urlWS;

    public WSClient(String urlWS) {
        this.urlWS = urlWS;
    }

    //POST
    public String post(String json) throws IOException {

        URL url = new URL(urlWS);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");

        connection.setRequestProperty("Content-type", "application/json");
        connection.setRequestProperty("Accept", "application/json");

        connection.setDoOutput(true);


        PrintStream printStream = new PrintStream(connection.getOutputStream());
        printStream.println(json);

        connection.connect();

        String jsonDeResposta = new Scanner(connection.getInputStream()).next();

        Log.i("json", jsonDeResposta);

        return jsonDeResposta;
    }

    //GET
    public String get() {
        String jsonDeResposta = "";

        try {

            URL url = new URL(urlWS);
            Log.i("get", urlWS);

            URLConnection urlConnection = url.openConnection();
            InputStream in = urlConnection.getInputStream();


//            jsonDeResposta = new Scanner(in).next();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder result = new StringBuilder();
            String line;
            Log.i("json", reader.readLine());
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            Log.i("get-", result.toString());

            jsonDeResposta = result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonDeResposta;
    }
}
