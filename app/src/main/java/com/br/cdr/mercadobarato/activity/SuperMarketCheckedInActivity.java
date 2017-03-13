package com.br.cdr.mercadobarato.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.br.cdr.mercadobarato.R;
import com.br.cdr.mercadobarato.model.SuperMarketWrapper;
import com.google.zxing.client.android.CaptureActivity;

public class SuperMarketCheckedInActivity extends AppCompatActivity {

    private BootstrapButton scanProduct;
    private SuperMarketWrapper wrapper;
    private BootstrapButton saveProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_market_checked_in);
        scanProduct = (BootstrapButton) findViewById(R.id.btn_scan_barcode);
        saveProduct = (BootstrapButton) findViewById(R.id.btn_save_product);

        wrapper = (SuperMarketWrapper) getIntent().getExtras().get("superMarkerWrapper");
        String message = new StringBuilder().append(getResources().getString(R.string.checkedIn)).append(" ")
                .append(wrapper.getName()).append("!").append(" ").append(getResources().getString(R.string.productsUpdateMessage)).toString();
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
            } else if (resultCode == RESULT_CANCELED) {
                Log.d("RJGXM", "RESULT_CANCELED");
            }
        }
    }

}
