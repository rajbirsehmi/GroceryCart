package com.creative.grocerycart.list;

import com.creative.grocerycart.populators.GroceryItem;

import java.util.ArrayList;

public class Grocery {
    private static ArrayList<GroceryItem> list;

    public static void createList() {
        if (list == null)
            list = new ArrayList<>();
    }

    public static ArrayList<GroceryItem> getList() {
        return list;
    }

    public static GroceryItem getItem(int position) {
        return list.get(position);
    }

    public static void addItemAtTop(GroceryItem groceryItem) {
        list.add(0, groceryItem);
    }

    public static void addItem(GroceryItem groceryItem) {
        list.add(groceryItem);
    }

    public static void removeItem(GroceryItem groceryItem) {
        list.remove(groceryItem);
    }

    public static void removeItem(int itemIndex) {
        list.remove(itemIndex);
    }

    public static void removeAllItems() {
        list.clear();
    }

    public static int getSize() {
        return list.size();
    }

    public static void moveToLast(int indexToMove) {
        GroceryItem itemToMove = new GroceryItem();
        itemToMove.setItemId(list.get(indexToMove).getItemId());
        itemToMove.setItemName(list.get(indexToMove).getItemName());
        itemToMove.setChecked(list.get(indexToMove).isChecked());
        list.remove(indexToMove);
        list.add(itemToMove);
    }
}
