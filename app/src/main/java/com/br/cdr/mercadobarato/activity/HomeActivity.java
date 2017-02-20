package com.br.cdr.mercadobarato.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.br.cdr.mercadobarato.R;
import com.br.cdr.mercadobarato.util.Utils;

public class HomeActivity extends Fragment {
    private ImageButton products;
    private ImageButton chart;
    private ImageButton info;
    private ImageButton list;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home);
//
//
//    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments

        View view = inflater.inflate(R.layout.activity_home, container, false);
        buttons(view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
//        getActivity().setTitle("Menu 3");
    }


    public void buttons(View view) {
//        Utils util = new Utils();
        chart = (ImageButton) view.findViewById(R.id.button_cart);
        chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(HomeActivity.this, AddProductShoppingListActivity.class);
//                startActivity(intent);
                Utils.openFragment(AddProductShoppingListActivity.class, getFragmentManager());
            }
        });

        products = (ImageButton) view.findViewById(R.id.button_products);
        products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(HomeActivity.this, AddProductActivity.class);
//                startActivity(intent);
                Utils.openFragment(AddProductActivity.class, getFragmentManager());

            }
        });

        list = (ImageButton) view.findViewById(R.id.button_listProducts);
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(HomeActivity.this, ListProductsActivity.class);
//                startActivity(intent);
                Utils.openFragment(ListProductsActivity.class, getFragmentManager());

            }
        });

        info = (ImageButton) view.findViewById(R.id.app_info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(HomeActivity.this, InfoActivity.class);
//                startActivity(intent);
//                utils.openFragment(new InfoActivity());
                Utils.openFragment(InfoActivity.class, getFragmentManager());

            }
        });
    }
}
