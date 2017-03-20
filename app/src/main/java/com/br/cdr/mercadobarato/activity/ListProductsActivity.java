package com.br.cdr.mercadobarato.activity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.br.cdr.mercadobarato.R;
import com.br.cdr.mercadobarato.model.Product;
import com.br.cdr.mercadobarato.util.Application;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListProductsActivity extends Fragment {
    private ListView mListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_list_products, container, false);
        mListView = (ListView) view.findViewById(R.id.list_view_products);

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Application application = (Application) getActivity().getApplication();
        HashMap<String, Product> shoppingList = application.getShoppingList();

        String[] listItems = new String[shoppingList.size()];
        int i = 0;
        for (Map.Entry<String, Product> entry : shoppingList.entrySet()) {
            String key = entry.getKey();
            Product value = entry.getValue();
            listItems[i] = value.getDescription();
            i++;

        }


        mListView.setAdapter(new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_list_item_1, listItems));
    }

}
