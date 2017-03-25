package com.br.cdr.mercadobarato.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.br.cdr.mercadobarato.R;
import com.br.cdr.mercadobarato.model.Product;
import com.br.cdr.mercadobarato.util.Application;
import com.br.cdr.mercadobarato.util.Utils;
import com.google.gson.Gson;
import com.google.zxing.client.android.CaptureActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class AddProductShoppingListActivity extends Fragment {
    private BootstrapButton scanProduct;
    private BootstrapButton addProductToList;
    private BootstrapEditText editProductCode;
    private BootstrapEditText editProductDescription;
    private static int SCAN_CODE_REQUEST = 1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_add_product_shopping_list, container, false);

        String[] permissoes = new String[]{
                Manifest.permission.CAMERA
        };

        Utils.validatePermissions(getActivity(), 0, permissoes);


        scanProduct = (BootstrapButton) view.findViewById(R.id.btn_scan_barcode);
        addProductToList = (BootstrapButton) view.findViewById(R.id.btn_save_product);
        editProductCode = (BootstrapEditText) view.findViewById(R.id.edit_product_code);
        editProductDescription = (BootstrapEditText) view.findViewById(R.id.edit_product_description);

        editProductDescription.setEnabled(false);
        editProductCode.setEnabled(false);

        scanProduct.setOnClickListener(this.onScanBtnClick);
        addProductToList.setOnClickListener(this.onAddProductToListBtnClick);

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                // Alguma permissão foi negada, agora é com você :-)
                Utils.alertAndFinish(getActivity());
                return;
            }
        }

    }


    private OnClickListener onScanBtnClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            Context context = getActivity().getApplicationContext();
            Intent intent = new Intent(context, CaptureActivity.class);

            intent.setAction("com.google.zxing.client.android.SCAN");
            intent.putExtra("SAVE_HISTORY", false);
            startActivityForResult(intent, SCAN_CODE_REQUEST);
        }
    };


    private OnClickListener onAddProductToListBtnClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            Application app = (Application) getActivity().getApplication();

            Product product = new Product();
            product.setBar_code(editProductCode.getText().toString());
            product.setDescription(editProductDescription.getText().toString());

            app.addProductToShoppingList(product);

            String added_product = getResources().getString(R.string.product_was_added);
            Toast.makeText(getActivity(), added_product, Toast.LENGTH_LONG).show();
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SCAN_CODE_REQUEST & resultCode == Activity.RESULT_OK) {
            String barCode = data.getStringExtra("SCAN_RESULT");
            editProductCode.setText(barCode);
            fetchProduct(barCode);
        }

    }


    private void fetchProduct(String barCode) {
        String host = getResources().getString(R.string.mercado_barato_api);
        String url = String.format("%sproducts/%s/", host, barCode);

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Gson gson = new Gson();
                Product product = gson.fromJson(String.valueOf(response), Product.class);
                editProductDescription.setText(product.getDescription());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, JSONObject responseString) {
                String product_not_found_msg = getResources().getString(R.string.product_not_found);
                Toast.makeText(getActivity(), product_not_found_msg, Toast.LENGTH_LONG).show();
            }
        });
    }

}
