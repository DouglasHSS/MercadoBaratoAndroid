package com.br.cdr.mercadobarato.activity;

import android.Manifest;
import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private BootstrapButton login;
    private BootstrapButton signUp;
    private BootstrapEditText login_field;
    private BootstrapEditText password_field;
    private UserWrapper user;


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
                    validaLogin();
                    
                    user = (UserWrapper) getIntent().getSerializableExtra("json");

                    if (user.getToken() != null) {
                        Intent intent = new Intent(MainActivity.this, NavigationMenuActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, R.string.validateLoginSenha, Toast.LENGTH_LONG).show();
                    }
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

    private void validaLogin() {
        try {
            String login = login_field.getText().toString();
            String password = password_field.getText().toString();
            user = new UserWrapper();
            if (login.length() == 0 || password.length() == 0) {
                Toast.makeText(MainActivity.this, R.string.validateFormLogin, Toast.LENGTH_LONG).show();

            } else {

                user.setUsername(login);
                user.setPassword(password);
                Gson gson = new Gson();
                String userJson = gson.toJson(user, UserWrapper.class); // converte de json para UserWrapper
                Log.i("user", userJson);
                String wsText = getResources().getString(R.string.mercado_barato_api) + "users/login/";

                String[] params = new String[]{
                        wsText,
                        "post",
                        userJson
                };


                WSThread ws = new WSThread();

                ws.execute(params); //Chama nova thread
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

