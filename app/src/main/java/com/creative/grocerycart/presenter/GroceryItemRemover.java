package com.creative.grocerycart.presenter;

public interface GroceryItemRemover {
    int getSwipedIndex();

    void setError(String message);

    void removeSwipedItem(int swipedIndex, String message);
}
