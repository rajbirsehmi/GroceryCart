package com.creative.grocerycart.database.database;

import com.creative.grocerycart.database.entity.GroceryItem;

import java.util.List;

public class QueryOrganiser {

    public static List<GroceryItem> getGroceryList() {
        return GroceryDatabase.getInstance().groceryItemDao().getItems();
    }


    public static int addGroceryItem(GroceryItem groceryItem) {
        return (int) GroceryDatabase.getInstance().groceryItemDao().addItem(groceryItem);
    }

    public static int removeGroceryItem(GroceryItem item) {
        return GroceryDatabase.getInstance().groceryItemDao().deleteItem(item);
    }
}
