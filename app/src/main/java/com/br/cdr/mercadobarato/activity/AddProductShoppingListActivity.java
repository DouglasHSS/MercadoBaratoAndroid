package com.br.cdr.mercadobarato.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.br.cdr.mercadobarato.R;
import com.google.zxing.client.android.CaptureActivity;

public class AddProductShoppingListActivity extends Fragment {
    private BootstrapButton scanProduct;
    private BootstrapButton addProductToList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_add_product_shopping_list, container, false);

        this.scanProduct = (BootstrapButton) view.findViewById(R.id.btn_scan_barcode);
        this.addProductToList = (BootstrapButton) view.findViewById(R.id.btn_save_product);

        this.scanProduct.setOnClickListener(this.onScanBtnClick);
        this.addProductToList.setOnClickListener(this.onAddProductToListBtnClick);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
//        getActivity().setTitle("Menu 3");
    }

    private OnClickListener onScanBtnClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            Context context = getActivity().getApplicationContext();
            Intent intent = new Intent(context, CaptureActivity.class);

            intent.setAction("com.google.zxing.client.android.SCAN");
            intent.putExtra("SAVE_HISTORY", false);
            startActivityForResult(intent, 0);
        }
    };

    private OnClickListener onAddProductToListBtnClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            /* It will save the scanned product in a ShoppingList object, which
               should be persisted in SharedPreference*/
        }
    };

}
