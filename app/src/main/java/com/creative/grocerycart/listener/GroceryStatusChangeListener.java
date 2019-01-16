package com.creative.grocerycart.listener;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.CompoundButton;

import com.creative.grocerycart.database.database.QueryOrganiser;
import com.creative.grocerycart.database.entity.GroceryItem;
import com.creative.grocerycart.list.Grocery;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class GroceryStatusChangeListener implements CompoundButton.OnCheckedChangeListener {

    private int position;

    public GroceryStatusChangeListener(int position) {
        this.position = position;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        new AsyncTask<Void, Void, Integer>(){
            @Override
            protected Integer doInBackground(Void... voids) {
                com.creative.grocerycart.populators.GroceryItem item = Grocery.getItem(position);
                item.setChecked(isChecked);
                GroceryItem mutatedItem = new GroceryItem();
                mutatedItem.setItemId(item.getItemId());
                mutatedItem.setItemName(item.getItemName());
                mutatedItem.setChecked(item.isChecked());
                return QueryOrganiser.updateStatus(mutatedItem);
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                Intent intentUpdateStatusNotifyMainList = new Intent("update_status");
                intentUpdateStatusNotifyMainList.putExtra("position", position);
                LocalBroadcastManager.getInstance(buttonView.getContext()).sendBroadcast(intentUpdateStatusNotifyMainList);
            }
        }.execute();
    }
}
