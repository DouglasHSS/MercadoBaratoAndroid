package com.br.cdr.mercadobarato;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.beardedhen.androidbootstrap.BootstrapButton;

public class HomeActivity extends AppCompatActivity {
    private ImageButton map_button;
    private ImageButton products;
    private ImageButton chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        chart  = (ImageButton) findViewById(R.id.button_cart);
        chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,AddProductShoppingListActivity.class);
                startActivity(intent);
            }
        });

        products  = (ImageButton) findViewById(R.id.button_products);
        products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,AddProductActivity.class);
                startActivity(intent);
            }
        });

        map_button  = (ImageButton) findViewById(R.id.button_map);
        map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,MapsActivity.class);
                startActivity(intent);
            }
        });


    }
}
