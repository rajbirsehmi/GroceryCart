package com.creative.grocerycart.model;

import android.os.AsyncTask;

import com.creative.grocerycart.database.database.QueryOrganiser;
import com.creative.grocerycart.database.entity.GroceryItem;
import com.creative.grocerycart.list.Grocery;
import com.creative.grocerycart.presenter.GroceryItemAdder;
import com.creative.grocerycart.utils.Constants;

import java.util.StringTokenizer;

public class GroceryItemAdderImpl {

    private GroceryItemAdder groceryItemAdder;

    public GroceryItemAdderImpl(GroceryItemAdder groceryItemAdder) {
        this.groceryItemAdder = groceryItemAdder;
    }

    public void addItem() {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                groceryItemAdder.disableButtonAndUpdateText();
                String itemName = groceryItemAdder.getItemName();
                if (itemName.trim().isEmpty())
                    return Constants.ERROR_CODE_MISSING_ITEM_NAME;

                StringTokenizer items = new StringTokenizer(itemName, ",");

                while (items.hasMoreTokens()) {
                    String item = items.nextToken().trim();
                    GroceryItem groceryItem = new GroceryItem();
                    groceryItem.setItemName(item);

                    int newlyCreatedItemId = QueryOrganiser.addGroceryItem(groceryItem);

                    com.creative.grocerycart.populators.GroceryItem tempItem = new com.creative.grocerycart.populators.GroceryItem();
                    tempItem.setChecked(false);
                    tempItem.setItemName(item);
                    tempItem.setItemId(newlyCreatedItemId);
                    Grocery.addItemAtTop(tempItem);
                    groceryItemAdder.renderNewItemAtMainThread();

                    //  this is not very good solution
                    //  to hold the thread for some time
                    //  for main execute the animation logic.
                    //  will investigate further for better
                    //  solution
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return Constants.ERROR_CODE_SOME_ERROR_OCCURED;
                    }
                }
                groceryItemAdder.enableButtonAndUpdateText();
                return Constants.SUCCESS_CODE;
            }

            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
                if (result == Constants.ERROR_CODE_MISSING_ITEM_NAME)
                    groceryItemAdder.setItemMissingError("Item Name is Missing...");
                if (result == Constants.ERROR_CODE_SOME_ERROR_OCCURED)
                    groceryItemAdder.showToast("Some Error Occured While Adding Item...");
                if (result == Constants.SUCCESS_CODE)
                    groceryItemAdder.validateChangesToList();
            }
        }.execute();
    }
}
