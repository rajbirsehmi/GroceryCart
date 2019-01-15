package com.creative.grocerycart.model;

import android.os.AsyncTask;
import android.util.Log;

import com.creative.grocerycart.database.database.QueryOrganiser;
import com.creative.grocerycart.database.entity.GroceryItem;
import com.creative.grocerycart.list.Grocery;
import com.creative.grocerycart.presenter.GroceryItemRemover;

public class GroceryItemRemoverImpl {

    private GroceryItemRemover groceryItemRemover;

    public GroceryItemRemoverImpl(GroceryItemRemover groceryItemRemover) {
        this.groceryItemRemover = groceryItemRemover;
    }

    public void removeItem() {
        int swipedIndex = groceryItemRemover.getSwipedIndex();
        if (swipedIndex == -1)
            return;
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                GroceryItem groceryItem = new GroceryItem();
                groceryItem.setItemName(Grocery.getItem(swipedIndex).getItemName());
                groceryItem.setItemId(Grocery.getItem(swipedIndex).getItemId());
                Grocery.removeItem(swipedIndex);
                return QueryOrganiser.removeGroceryItem(groceryItem);
            }

            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
                Log.d("remove", "onPostExecute: " + result);
                if (result == 0)
                    groceryItemRemover.setError("Some Error Occured While Removing Item...");
                if (result == 1)
                    groceryItemRemover.removeSwipedItem(swipedIndex, "Item Has been removed...");
            }
        }.execute();
    }
}
