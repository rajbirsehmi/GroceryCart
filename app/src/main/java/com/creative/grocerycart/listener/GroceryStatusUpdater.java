package com.creative.grocerycart.listener;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.CheckBox;

import com.creative.grocerycart.database.database.QueryOrganiser;
import com.creative.grocerycart.database.entity.GroceryItem;
import com.creative.grocerycart.list.Grocery;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class GroceryStatusUpdater implements View.OnClickListener {

    private int position;

    public GroceryStatusUpdater(int position) {
        this.position = position;
    }

    @Override
    public void onClick(View view) {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                boolean isChecked = ((CheckBox) (view)).isChecked();
                com.creative.grocerycart.populators.GroceryItem item = Grocery.getItem(position);
                item.setChecked(isChecked);
                GroceryItem mutatedItem = new GroceryItem();
                mutatedItem.setItemId(item.getItemId());
                mutatedItem.setItemName(item.getItemName());
                mutatedItem.setChecked(item.isChecked());
                Grocery.replaceItem(item, position);
                return QueryOrganiser.updateStatus(mutatedItem);
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                Intent intentUpdateStatusNotifyMainList = new Intent("update_status");
                if (Grocery.getItem(position).isChecked()) {
                    intentUpdateStatusNotifyMainList.putExtra("checked", true);
                }
                intentUpdateStatusNotifyMainList.putExtra("position", position);
                LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intentUpdateStatusNotifyMainList);
            }
        }.execute();
    }
}
