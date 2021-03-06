package com.br.cdr.mercadobarato.activity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.br.cdr.mercadobarato.R;
import com.br.cdr.mercadobarato.model.Product;
import com.br.cdr.mercadobarato.util.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Product} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyProductItemRecyclerViewAdapter extends RecyclerView.Adapter<MyProductItemRecyclerViewAdapter.ViewHolder> {

    private final List<Product> mValues;
    private final OnListFragmentInteractionListener mListener;


    public MyProductItemRecyclerViewAdapter(List<Product> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_productitem, parent, false);
        return new ViewHolder(view);




    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        //  holder.mIdView.setText(mValues.get(position).getBar_code());
        holder.mContentView.setText(mValues.get(position).getDescription());

        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                holder.mItem = mValues.get(position);
                mValues.remove(position);
                notifyItemRemoved(position); // notify the adapter about the removed item
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });



    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public final Button mDeleteButton;
        public Product mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            // mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            mDeleteButton = (Button) view.findViewById(R.id.delete_btn);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }



    }


}
