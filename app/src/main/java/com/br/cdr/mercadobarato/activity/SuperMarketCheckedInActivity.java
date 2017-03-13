package com.br.cdr.mercadobarato.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.br.cdr.mercadobarato.R;
import com.br.cdr.mercadobarato.model.SuperMarketWrapper;
import com.br.cdr.mercadobarato.util.Utils;
import com.google.zxing.client.android.CaptureActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SuperMarketCheckedInActivity extends AppCompatActivity {

    private BootstrapButton scanProduct;
    private SuperMarketWrapper superMarketWrapper;
    private BootstrapButton saveProduct;
    private BootstrapEditText editProductCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] permissoes = new String[]{
                Manifest.permission.CAMERA
        };

        Utils.validatePermissions(this, 0, permissoes);


        setContentView(R.layout.activity_super_market_checked_in);
        scanProduct = (BootstrapButton) findViewById(R.id.btn_scan_barcode);
        saveProduct = (BootstrapButton) findViewById(R.id.btn_save_product);
        editProductCode = (BootstrapEditText) findViewById(R.id.edit_product_code);
        editProductCode.setEnabled(true);
        superMarketWrapper = (SuperMarketWrapper) getIntent().getExtras().get("superMarkerWrapper");
        String message = getResources().getString(R.string.checkedIn) + " " +
                superMarketWrapper.getName() + "! " + getResources().getString(R.string.productsUpdateMessage);
        Toast.makeText(this, message,
                Toast.LENGTH_LONG).show();


        scanProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
                intent.setAction("com.google.zxing.client.android.SCAN");
                intent.putExtra("SAVE_HISTORY", false);
                startActivityForResult(intent, 0);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                Log.d("RJGXM", "contents: " + contents);
                editProductCode.setText(contents);
                editProductCode.setEnabled(false);

            } else if (resultCode == RESULT_CANCELED) {
                Log.d("RJGXM", "RESULT_CANCELED");
            }
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

    public void verifyMarket() {

        AsyncHttpClient client = new AsyncHttpClient();

        String url = getResources().getString(R.string.mercado_barato_api) + "/supermarkets/" + superMarketWrapper.getID();

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Log.i("statusCodeS", "" + statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.i("statusCode", "" + statusCode);
            }
        });


    }

}
