package com.br.cdr.mercadobarato.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.br.cdr.mercadobarato.R;
import com.br.cdr.mercadobarato.model.Product;
import com.br.cdr.mercadobarato.util.Application;
import com.br.cdr.mercadobarato.util.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListProductsActivity extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private ListView mListView;
    private OnListFragmentInteractionListener mListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_productitem_list, container, false);


        Application application = (Application) getActivity().getApplication();
        HashMap<String, Product> shoppingList = application.getShoppingList();

        // Set the adapter
        Log.i("recycler", "" + (view instanceof RecyclerView));
//        if (view instanceof RecyclerView) {
        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view;
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        List<Product> listItems = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, Product> entry : shoppingList.entrySet()) {
            String key = entry.getKey();
            Product value = entry.getValue();
            listItems.add(value);
            i++;

        }

        recyclerView.setAdapter(new MyProductItemRecyclerViewAdapter(listItems, mListener));
//        } else {
//
//            mListView = (ListView) view.findViewById(R.id.list_view_products);
//
//            String[] listItems2 = new String[shoppingList.size()];
//            int i = 0;
//            for (Map.Entry<String, Product> entry : shoppingList.entrySet()) {
//                String key = entry.getKey();
//                Product value = entry.getValue();
//                listItems2[i] = value.getDescription();
//                i++;
//
//            }
//
//            mListView.setAdapter(new ArrayAdapter<String>(view.getContext(),
//                    android.R.layout.simple_list_item_1, listItems2));
//        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
