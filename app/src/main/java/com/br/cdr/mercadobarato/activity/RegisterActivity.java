package com.br.cdr.mercadobarato.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class RegisterActivity extends AppCompatActivity {

    private BootstrapEditText name_field;
    private BootstrapEditText email_field;
    private BootstrapEditText password_field_register;
    private BootstrapEditText confirm_password;
    private BootstrapButton registerButton;
    private ProgressDialog loading = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        String[] permissoes = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE
        };

        if (Utils.validatePermissions(this, null, 0, permissoes)) {

            name_field = (BootstrapEditText) findViewById(R.id.name_field);
            email_field = (BootstrapEditText) findViewById(R.id.email_field);
            password_field_register = (BootstrapEditText) findViewById(R.id.password_field_register);
            confirm_password = (BootstrapEditText) findViewById(R.id.confirm_password);

            registerButton = (BootstrapButton) findViewById(R.id.button_register);
            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    loading = new ProgressDialog(v.getContext());
                    loading.setCancelable(true);
                    loading.setMessage(getResources().getString(R.string.loading));
                    loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                    validateRegister();
//                            (UserWrapper) getIntent().getSerializableExtra("json");


                }
            });


        } else {
            Toast.makeText(RegisterActivity.this, R.string.validatePermissions, Toast.LENGTH_LONG).show();

        }
    }

    private void validateRegister() {
        String nameField = name_field.getText().toString();
        String emailField = email_field.getText().toString();
        String password = password_field_register.getText().toString();
        String confirmPassword = confirm_password.getText().toString();

        if (nameField.length() == 0 || emailField.length() == 0
                || password.length() == 0 || confirmPassword.length() == 0) {
            Toast.makeText(RegisterActivity.this, R.string.validateFormLogin, Toast.LENGTH_LONG).show();

        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(RegisterActivity.this, R.string.validatePasswordEquals, Toast.LENGTH_LONG).show();
        } else {
            loading.show();

            UserWrapper user = new UserWrapper();
            user.setUsername(nameField);
            user.setPassword(password);
            user.setEmail(emailField);
            final Gson gson = new Gson();
            String userJson = gson.toJson(user, UserWrapper.class); // converte de json para UserWrapper
            Log.i("user", userJson);
            String wsText = getResources().getString(R.string.mercado_barato_api) + "users/";
            AsyncHttpClient client = new AsyncHttpClient();

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
                        Intent intent = new Intent(RegisterActivity.this, NavigationMenuActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                    super.onFailure(statusCode, headers, responseString, throwable);

                    loading.dismiss();
                    Toast.makeText(RegisterActivity.this, responseString, Toast.LENGTH_LONG).show();
                    Log.e("RJGXM", statusCode + " " + throwable.getMessage() + " " + responseString);
                }


            });
        }


    }


}
