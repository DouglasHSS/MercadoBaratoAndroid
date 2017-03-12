package com.br.cdr.mercadobarato.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.br.cdr.mercadobarato.R;
import com.br.cdr.mercadobarato.model.SuperMarketWrapper;

public class SuperMarketCheckedInActivity extends AppCompatActivity {

    private BootstrapButton scanProduct;
    private SuperMarketWrapper wrapper;
    private BootstrapButton saveProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_market_checked_in);

        wrapper =(SuperMarketWrapper) getIntent().getExtras().get("superMarkerWrapper");
        String message = new StringBuilder().append(getResources().getString(R.string.checkedIn)).append(" ")
                .append(wrapper.getName()).append("!").append(" ").append(getResources().getString(R.string.productsUpdateMessage)).toString();
        Toast.makeText(this, message,
                Toast.LENGTH_LONG).show();

        scanProduct = (BootstrapButton) findViewById(R.id.button_login);





    }

}
