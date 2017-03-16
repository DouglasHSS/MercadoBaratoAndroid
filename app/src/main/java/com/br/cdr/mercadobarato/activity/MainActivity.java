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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;


import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.br.cdr.mercadobarato.R;
import com.br.cdr.mercadobarato.model.UserWrapper;
import com.br.cdr.mercadobarato.util.Utils;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.Arrays;

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
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MultiDex.install(this);
        FirebaseApp.initializeApp(this);

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


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.loginButton);

        loginButton.setReadPermissions(Arrays.asList("email"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), R.string.cancel_login, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_SHORT).show();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser facebookUser = firebaseAuth.getCurrentUser();
                if (facebookUser != null) {
                    Intent intent = new Intent(MainActivity.this, NavigationMenuActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
            }
        };


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

    private void handleFacebookAccessToken(AccessToken accessToken) {
        progressBar.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.GONE);

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.firebase_error_login, Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
                loginButton.setVisibility(View.VISIBLE);
            }
        });
    }

    private void goMainScreen() {
        Intent intent = new Intent(MainActivity.this, NavigationMenuActivity.class);

        startActivity(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
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

