package com.br.cdr.mercadobarato.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Entity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.br.cdr.mercadobarato.R;
import com.br.cdr.mercadobarato.model.UserWrapper;
import com.br.cdr.mercadobarato.util.Utils;
import com.br.cdr.mercadobarato.util.WSThread;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;


public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private BootstrapButton login;
    private BootstrapButton signUp;
    private BootstrapEditText login_field;
    private BootstrapEditText password_field;
    ProgressDialog loading = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MultiDex.install(this);
        setContentView(R.layout.activity_main);


        String[] permissoes = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE
        };

        if (Utils.validatePermissions(this, null, 0, permissoes)) {


            toolbar = (Toolbar) findViewById(R.id.main_bar);
            setSupportActionBar(toolbar);


            login = (BootstrapButton) findViewById(R.id.button_login);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    loading = new ProgressDialog(v.getContext());
                    loading.setCancelable(true);
                    loading.setMessage("auten");
                    loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                    validaLoginTest();
//                            (UserWrapper) getIntent().getSerializableExtra("json");


                }
            });

            signUp = (BootstrapButton) findViewById(R.id.btn_signup);
            signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            });

            password_field = (BootstrapEditText) findViewById(R.id.password_field);
            login_field = (BootstrapEditText) findViewById(R.id.login_field);
        } else {
            Toast.makeText(MainActivity.this, R.string.validatePermissions, Toast.LENGTH_LONG).show();
        }

    }

//    private void validaLogin() {
//        try {
//            String login = login_field.getText().toString();
//            String password = password_field.getText().toString();
//            login = "cleberson";
//            password = "123456";
//            if (login.length() == 0 || password.length() == 0) {
//                Toast.makeText(MainActivity.this, R.string.validateFormLogin, Toast.LENGTH_LONG).show();
//
//            } else {
//                UserWrapper user = new UserWrapper();
//                user.setUsername(login);
//                user.setPassword(password);
//                Gson gson = new Gson();
//                String userJson = gson.toJson(user, UserWrapper.class); // converte de json para UserWrapper
//                Log.i("user", userJson);
//                String wsText = getResources().getString(R.string.mercado_barato_api) + "users/login/";
//
//                String[] params = new String[]{
//                        wsText,
//                        "post",
//                        userJson
//                };
//
//                WSThread ws = new WSThread();
//
//                ws.execute(params); //Chama nova thread
//
//                if (ws.get().length() > 0) {
//                    loading.dismiss();
//
//                    user = gson.fromJson(ws.get(), UserWrapper.class);
//                    if (user != null && user.getToken() != null) {
//                        Intent intent = new Intent(this, NavigationMenuActivity.class);
//                        startActivity(intent);
//                    } else {
//                        Toast.makeText(MainActivity.this, R.string.validateLoginSenha, Toast.LENGTH_LONG).show();
//                    }
//                } else {
//                    Toast.makeText(MainActivity.this, R.string.validateLoginSenha, Toast.LENGTH_LONG).show();
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void validaLoginTest() {

        String login = login_field.getText().toString();
        String password = password_field.getText().toString();
        if (login.length() == 0 || password.length() == 0) {
            Toast.makeText(MainActivity.this, R.string.validateFormLogin, Toast.LENGTH_LONG).show();

        } else {
            loading.show();

            UserWrapper user = new UserWrapper();
            user.setUsername(login);
            user.setPassword(password);
            final Gson gson = new Gson();
            String userJson = gson.toJson(user, UserWrapper.class); // converte de json para UserWrapper
            Log.i("user", userJson);
            String wsText = getResources().getString(R.string.mercado_barato_api) + "users/login/";
            RequestParams params = new RequestParams();
            params.put("user", userJson);
            Log.i("param", params.toString());
            AsyncHttpClient client = new AsyncHttpClient();
            client.setBasicAuth(login, password);
            StringEntity entity = null;
            try {
                entity = new StringEntity(userJson);
                entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            } catch (Exception e) {
            }
            client.post(this, wsText, entity, "application/json", new JsonHttpResponseHandler() {


                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
                    loading.dismiss();

                    Log.i("responseUser", "" + response);
                    UserWrapper user = gson.fromJson(String.valueOf(response), UserWrapper.class);
//                            (UserWrapper) getIntent().getSerializableExtra("json");

                    if (user.getToken() != null) {
                        Intent intent = new Intent(MainActivity.this, NavigationMenuActivity.class);
                        startActivity(intent);
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                    super.onFailure(statusCode, headers, responseString, throwable);

                    loading.dismiss();
                    Toast.makeText(MainActivity.this, R.string.validateLoginSenha, Toast.LENGTH_LONG).show();
                    Log.e("RJGXM", statusCode + " " + throwable.getMessage() + " " + responseString);
                }


                @Override
                public void onProgress(long bytesWritten, long totalSize) {
                    super.onProgress(bytesWritten, totalSize);
                }

            });
        }
    }


}

