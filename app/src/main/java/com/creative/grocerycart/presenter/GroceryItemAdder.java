package com.creative.grocerycart.presenter;

public interface GroceryItemAdder {
    String getItemName();

    void setItemMissingError(String message);

    void showToast(String message);

    @Deprecated
    void renderNewItems();

    void renderNewItemAtMainThread();

    void validateChangesToList();

    void disableButtonAndUpdateText();

    void enableButtonAndUpdateText();
}
