package com.br.cdr.mercadobarato.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;


public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private BootstrapButton login;
    private BootstrapButton signUp;
    private BootstrapEditText login_field;
    private BootstrapEditText password_field;
    private ProgressDialog loading = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MultiDex.install(this);
        setContentView(R.layout.activity_main);


        String[] permissoes = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE
        };

        Utils.validatePermissions(this, 0, permissoes);

        // Usuário aceitou a permissão!
        toolbar = (Toolbar) findViewById(R.id.main_bar);
        setSupportActionBar(toolbar);


        login = (BootstrapButton) findViewById(R.id.button_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loading = new ProgressDialog(v.getContext());
                loading.setCancelable(true);
                loading.setMessage(getResources().getString(R.string.loading));
                loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                validaLogin();
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
    }

    private void validaLogin() {

        String login = login_field.getText().toString();
        String password = password_field.getText().toString();
        login = "cleberson";
        password = "123456";


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
            AsyncHttpClient client = new AsyncHttpClient();
//            client.setBasicAuth(login, password);
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

                    if (user.getToken() != null) {
                        Intent intent = new Intent(MainActivity.this, NavigationMenuActivity.class);
                        intent.putExtra("user", user);
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

            });
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                // Alguma permissão foi negada, agora é com você :-)
                Utils.alertAndFinish(this);
                return;
            }
        }

    }
}

