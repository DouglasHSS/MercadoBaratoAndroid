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
import com.br.cdr.mercadobarato.model.ProductWrapper;
import com.br.cdr.mercadobarato.model.SuperMarket;
import com.br.cdr.mercadobarato.model.SuperMarketWrapper;
import com.br.cdr.mercadobarato.model.UserWrapper;
import com.br.cdr.mercadobarato.util.Utils;
import com.google.gson.Gson;
import com.google.zxing.client.android.CaptureActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class SuperMarketCheckedInActivity extends AppCompatActivity {

    private BootstrapButton scanProduct;
    private SuperMarketWrapper superMarketWrapper;
    private BootstrapButton saveProduct;
    private BootstrapEditText editProductCode;
    private BootstrapEditText mMarketName;
    private BootstrapEditText editProductPrice;
    private BootstrapEditText editProductdescription;

    private ProgressDialog loading = null;

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
        editProductPrice = (BootstrapEditText) findViewById(R.id.edit_product_price);
        editProductdescription = (BootstrapEditText) findViewById(R.id.edit_product_description);

        mMarketName = (BootstrapEditText) findViewById(R.id.market_name);

        mMarketName.setEnabled(true);

        editProductCode.setEnabled(true);
        superMarketWrapper = (SuperMarketWrapper) getIntent().getExtras().get("superMarkerWrapper");
        String message = getResources().getString(R.string.checkedIn) + " " +
                superMarketWrapper.getName() + "! " + getResources().getString(R.string.productsUpdateMessage);
        Toast.makeText(this, message,
                Toast.LENGTH_LONG).show();

        mMarketName.setText(superMarketWrapper.getName());
        mMarketName.setEnabled(false);

        verifyMarket();
        scanProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
                intent.setAction("com.google.zxing.client.android.SCAN");
                intent.putExtra("SAVE_HISTORY", false);
                startActivityForResult(intent, 0);

            }
        });

        saveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loading = new ProgressDialog(v.getContext());
                loading.setCancelable(true);
                loading.setMessage(getResources().getString(R.string.loading));
                loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                saveProduct();

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

        String url = getResources().getString(R.string.mercado_barato_api) + "supermarkets/" + superMarketWrapper.getID();

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);

                Log.i("statusCodeS", "" + statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject responseString) {
//                super.onFailure(statusCode, headers, responseString, throwable);
                Log.i("statusCode", "" + statusCode);
                saveMarket();

            }


        });


    }

    private void saveMarket() {

        SuperMarket superMarket = new SuperMarket();
        superMarket.setId(superMarketWrapper.getID());
        superMarket.setName(superMarketWrapper.getName());
        final Gson gson = new Gson();
        String supermarketJson = gson.toJson(superMarket, SuperMarket.class); // converte de json para UserWrapper
        Log.i("supermarketJson", supermarketJson);
        String wsText = getResources().getString(R.string.mercado_barato_api) + "supermarkets/";
        AsyncHttpClient client = new AsyncHttpClient();

        StringEntity entity = null;
        try {
            entity = new StringEntity(supermarketJson);
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (Exception e) {
        }

        client.post(this, wsText, entity, "application/json", new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);

                Log.i("response", "" + response);
                SuperMarket supermarket = gson.fromJson(String.valueOf(response), SuperMarket.class);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject responseString) {
                Log.e("RJGXM", statusCode + " " + throwable.getMessage() + " " + responseString);

            }

//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
////                    super.onFailure(statusCode, headers, responseString, throwable);
//
//                Log.e("RJGXM", statusCode + " " + throwable.getMessage() + " " + responseString);
//            }

        });
    }


    private void saveProduct() {
        loading.show();

        ProductWrapper product = new ProductWrapper();
//        superMarket.setId(superMarketWrapper.getID());
//        superMarket.setName(superMarketWrapper.getName());
        product.setBar_code(editProductCode.toString());
        product.setDescription(editProductdescription.toString());
        product.setPrice((editProductPrice.toString()));
        final Gson gson = new Gson();
        String productJson = gson.toJson(product, ProductWrapper.class); // converte de json para UserWrapper
        Log.i("productJson", productJson);
        String wsText = getResources().getString(R.string.mercado_barato_api) +
                "supermarkets/" + superMarketWrapper.getID() + "/products";
        AsyncHttpClient client = new AsyncHttpClient();

        StringEntity entity = null;
        try {
            entity = new StringEntity(productJson);
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (Exception e) {
        }

        client.post(this, wsText, entity, "application/json", new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
                loading.dismiss();

                Log.i("response", "" + response);
                ProductWrapper productWrapper = gson.fromJson(String.valueOf(response), ProductWrapper.class);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                super.onFailure(statusCode, headers, responseString, throwable);
                loading.dismiss();

                Log.e("RJGXM", statusCode + " " + throwable.getMessage() + " " + responseString);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject responseString) {
//                    super.onFailure(statusCode, headers, responseString, throwable);
                loading.dismiss();

                Log.e("RJGXM", statusCode + " " + throwable.getMessage() + " " + responseString);
            }

        });
    }


}
