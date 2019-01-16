package com.creative.grocerycart.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.creative.grocerycart.R;
import com.creative.grocerycart.list.Grocery;
import com.creative.grocerycart.listener.GroceryStatusChangeListener;
import com.creative.grocerycart.viewholder.GroceryItemHolder;

public class GroceryAdapter extends RecyclerView.Adapter<GroceryItemHolder> {

    @NonNull
    @Override
    public GroceryItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View groceryItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.template_grocery_item, viewGroup, false);
        return new GroceryItemHolder(groceryItem);
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryItemHolder groceryItemHolder, int position) {
        groceryItemHolder.onBind(Grocery.getItem(position));
        groceryItemHolder.setGroceryStatusListener(new GroceryStatusChangeListener(position));
    }

    @Override
    public int getItemCount() {
        return Grocery.getSize();
    }
}
