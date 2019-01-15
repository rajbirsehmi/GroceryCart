package com.creative.grocerycart.database.database;

import com.creative.grocerycart.database.entity.GroceryItem;

import java.util.List;

public class QueryOrganiser {

    public static List<GroceryItem> getGroceryList() {
        return GroceryDatabase.getInstance().groceryItemDao().getItems();
    }



}
