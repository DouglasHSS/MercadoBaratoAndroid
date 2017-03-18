package com.br.cdr.mercadobarato.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.br.cdr.mercadobarato.R;
import com.br.cdr.mercadobarato.model.Product;
import com.br.cdr.mercadobarato.model.SuperMarket;
import com.br.cdr.mercadobarato.model.SuperMarketWrapper;
import com.br.cdr.mercadobarato.model.SupermarketProduct;
import com.br.cdr.mercadobarato.util.Utils;
import com.google.gson.Gson;
import com.google.zxing.client.android.CaptureActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.IOException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class SuperMarketCheckedInActivity extends AppCompatActivity {

    private final String ENCODING = "application/json; charset=utf-8";
    private BootstrapButton scanProduct;
    private SuperMarketWrapper superMarketWrapper;
    private BootstrapButton saveProduct;
    private BootstrapEditText editProductCode;
    private BootstrapEditText mMarketName;
    private BootstrapEditText editProductPrice;
    private BootstrapEditText editProductdescription;
    private SuperMarket superMarket;

    private boolean haveProduct = false;


    private ProgressDialog loading = null;
    private String barCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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

                if (haveProduct) {
                    barCode = editProductCode.getText().toString();
                    Log.d("barCode", "barCode: " + barCode);

                    updateProduct(barCode);
                } else {
                    saveProduct();
                }

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
                verifyProduct(contents);
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

        superMarket = new SuperMarket();
        superMarket.setId(superMarketWrapper.getID());
        superMarket.setName(superMarketWrapper.getName());
        final Gson gson = new Gson();
        String supermarketJson = gson.toJson(superMarket, SuperMarket.class);
        Log.i("supermarketJson", supermarketJson);
        String url = getResources().getString(R.string.mercado_barato_api) + "supermarkets/";
        AsyncHttpClient client = new AsyncHttpClient();

        StringEntity entity = getStringEntity(supermarketJson);

        client.post(this, url, entity, ENCODING, new JsonHttpResponseHandler() {


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
        });
    }


    private void saveProduct() {
        loading.show();

        SupermarketProduct supermarketProduct = getSupermarketProduct();

        final Gson gson = new Gson();
        String productJson = gson.toJson(supermarketProduct, SupermarketProduct.class);
        Log.i("productJson", productJson);
        String url = getResources().getString(R.string.mercado_barato_api) + "supermarkets/" + superMarketWrapper.getID() + "/products/";
        AsyncHttpClient client = new AsyncHttpClient();

        StringEntity entity = getStringEntity(productJson);

        Log.i("urlProduto", url);

        client.post(this, url, entity, ENCODING,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
                        loading.dismiss();
                        Log.i("response", "" + response);
                        Product product = gson.fromJson(String.valueOf(response), Product.class);

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


    private void verifyProduct(String barCode) {
        String url = getResources().getString(R.string.mercado_barato_api) + "supermarkets/" + superMarketWrapper.getID() + "/products/" + barCode;
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
                haveProduct = true;
                Gson gson = new Gson();
                SupermarketProduct supermarketProduct = gson.fromJson(String.valueOf(response), SupermarketProduct.class);
                editProductdescription.setEnabled(false);
                editProductdescription.setText(supermarketProduct.getProduct().getDescription());
                Log.i("statusCodeS", "" + statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable
                    throwable, JSONObject responseString) {
                haveProduct = false;
                Log.i("statusCode", "" + statusCode);

            }
        });
    }

    private void updateProduct(String barCode) {
        loading.show();

        SupermarketProduct supermarketProduct = getSupermarketProduct();

        final Gson gson = new Gson();
        String productJson = gson.toJson(supermarketProduct, SupermarketProduct.class); // converte de json para UserWrapper
        Log.i("productJson", productJson);
        String url = getResources().getString(R.string.mercado_barato_api) + "supermarkets/"
                + superMarketWrapper.getID() + "/products/" + barCode + "/";
        AsyncHttpClient client = new AsyncHttpClient();
        StringEntity entity = getStringEntity(productJson);

        Log.i("urlProduto", url);

        client.put(this, url, entity, ENCODING,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        loading.dismiss();

                        Log.i("response", "" + response);
                        Product product = gson.fromJson(String.valueOf(response), Product.class);

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
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

    @Nullable
    private StringEntity getStringEntity(String json) {
        StringEntity entity = null;
        try {
            entity = new StringEntity(json);
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_ENCODING, "UTF-8"));
        } catch (Exception e) {
        }
        return entity;
    }

    @NonNull
    private SupermarketProduct getSupermarketProduct() {
        Product product = new Product();
        product.setBar_code(editProductCode.getText().toString());
        product.setDescription(editProductdescription.getText().toString());

        SupermarketProduct supermarketProduct = new SupermarketProduct();
        supermarketProduct.setProduct(product);
        supermarketProduct.setSuperMarket(superMarket);
        supermarketProduct.setPrice(editProductPrice.getText().toString());
        return supermarketProduct;
    }

}
