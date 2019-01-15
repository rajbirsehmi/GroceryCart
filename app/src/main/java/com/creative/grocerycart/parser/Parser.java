package com.creative.grocerycart.parser;

import com.creative.grocerycart.list.Grocery;
import com.creative.grocerycart.populators.GroceryItem;

import java.util.List;

public class Parser {

    public static void convertList(List<com.creative.grocerycart.database.entity.GroceryItem> groceryItems) {
        Grocery.createList();
        for (com.creative.grocerycart.database.entity.GroceryItem groceryItem : groceryItems) {
            GroceryItem item = new GroceryItem();
            item.setItemId(groceryItem.getItemId());
            item.setItemName(groceryItem.getItemName());
            item.setChecked(groceryItem.isChecked());
            Grocery.addItem(item);
        }
    }
}
