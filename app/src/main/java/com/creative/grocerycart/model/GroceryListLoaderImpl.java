package com.creative.grocerycart.model;

import android.os.AsyncTask;

import com.creative.grocerycart.adapter.GroceryAdapter;
import com.creative.grocerycart.database.database.QueryOrganiser;
import com.creative.grocerycart.database.entity.GroceryItem;
import com.creative.grocerycart.list.Grocery;
import com.creative.grocerycart.parser.Parser;
import com.creative.grocerycart.presenter.GroceryListLoader;

import java.util.List;

public class GroceryListLoaderImpl {

    private GroceryListLoader groceryListLoader;

    public GroceryListLoaderImpl(GroceryListLoader groceryList) {
        this.groceryListLoader = groceryList;
    }

    public void loadGroceryList() {
        new AsyncTask<Void, Void, GroceryAdapter>() {
            @Override
            protected GroceryAdapter doInBackground(Void... voids) {
                if (Grocery.getSize() != 0)
                    Grocery.removeAllItems();
                List<GroceryItem> groceryList = QueryOrganiser.getGroceryList();
                Parser.convertList(groceryList);
                return new GroceryAdapter();
            }

            @Override
            protected void onPostExecute(GroceryAdapter groceryAdapter) {
                super.onPostExecute(groceryAdapter);
                groceryListLoader.attachAdapter(groceryAdapter);
            }
        }.execute();
    }
}
