package com.br.cdr.mercadobarato.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.br.cdr.mercadobarato.R;
import com.br.cdr.mercadobarato.model.UserWrapper;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private BootstrapButton login;
    private BootstrapButton signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MultiDex.install(this);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.main_bar);
        setSupportActionBar(toolbar);

        login = (BootstrapButton) findViewById(R.id.button_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NavigationMenuActivity.class);
                startActivity(intent);
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

    }


    public UserWrapper loadUserFromJSONGson(String jsonString) {
        Gson gson = new Gson();
        UserWrapper user = gson.fromJson(jsonString, UserWrapper.class);
        return user;
    }

}

