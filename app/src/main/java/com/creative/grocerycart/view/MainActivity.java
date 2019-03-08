package com.creative.grocerycart.view;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.grocerycart.R;
import com.creative.grocerycart.adapter.GroceryAdapter;
import com.creative.grocerycart.database.database.GroceryDatabase;
import com.creative.grocerycart.list.Grocery;
import com.creative.grocerycart.model.GroceryItemAdderImpl;
import com.creative.grocerycart.model.GroceryItemRemoverImpl;
import com.creative.grocerycart.model.GroceryListLoaderImpl;
import com.creative.grocerycart.postprocessing.PostProcessing;
import com.creative.grocerycart.presenter.GroceryItemAdder;
import com.creative.grocerycart.presenter.GroceryItemRemover;
import com.creative.grocerycart.presenter.GroceryListLoader;
import com.creative.grocerycart.utils.Constants;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements GroceryListLoader, GroceryItemAdder, GroceryItemRemover {

    private GroceryAdapter groceryAdapter;

    private GroceryListLoaderImpl groceryListLoader;
    private GroceryItemAdderImpl groceryItemAdder;
    private GroceryItemRemoverImpl groceryItemRemover;

    private RecyclerView rvGroceryList;
    private Dialog dialogAddItem;

    private String itemName;
    private int swipedIndex = -1;

    private boolean exitFlag;

    private ItemTouchHelper.SimpleCallback swipeToRemoveGestureCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            swipedIndex = viewHolder.getAdapterPosition();
            groceryItemRemover.removeItem();
            PostProcessing.handleDataPositions(groceryAdapter);
        }
    };

    private BroadcastReceiver receiverUpdateStatus = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("update_status")) {
                int position = intent.getIntExtra("position", -1);
                if (position == -1)
                    return;
                groceryAdapter.notifyItemChanged(position);
                if (intent.getBooleanExtra("checked", false)) {
                    Grocery.moveToLast(position);
                    groceryAdapter.notifyItemMoved(position, Grocery.getLastIndex());
                } else {
                    Grocery.moveToTop(position);
                    groceryAdapter.notifyItemMoved(position, Grocery.getFirstIndex());
                }
                PostProcessing.handleDataPositions(groceryAdapter);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GroceryDatabase.createInstance(getBaseContext());
        Grocery.createList();

        groceryListLoader = new GroceryListLoaderImpl(this);
        groceryItemAdder = new GroceryItemAdderImpl(this);
        groceryItemRemover = new GroceryItemRemoverImpl(this);

        findViewById(R.id.fab_add_item).setOnClickListener((view) -> {
            dialogAddItem = new Dialog(this);
            dialogAddItem.setContentView(R.layout.layout_item_add_dialog);
            dialogAddItem.setCancelable(false);
            EditText etItemName = dialogAddItem.findViewById(R.id.et_item_name);
            dialogAddItem.findViewById(R.id.btn_save_item).setOnClickListener((viewButton) -> {
                itemName = etItemName.getText().toString();
                groceryItemAdder.addItem();
            });
            etItemName.setOnEditorActionListener((editText, actionId, keyEvent) -> {
                itemName = etItemName.getText().toString();
                groceryItemAdder.addItem();
                return false;
            });
            dialogAddItem.findViewById(R.id.btn_dismiss).setOnClickListener((viewButton) -> {
                dialogAddItem.dismiss();
            });
            dialogAddItem.create();
            dialogAddItem.show();
            dialogAddItem.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        });

        rvGroceryList = findViewById(R.id.rv_grocery_list);
        rvGroceryList.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        rvGroceryList.setItemAnimator(new DefaultItemAnimator());
        new ItemTouchHelper(swipeToRemoveGestureCallback).attachToRecyclerView(rvGroceryList);
        groceryListLoader.loadGroceryList();
    }

    @Override
    public void onBackPressed() {
        if (exitFlag) {
            finish();
        }
        exitFlag = true;
        Toast.makeText(this, getResources().getString(R.string.exit_toast_text), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                exitFlag = false;
            }
        }, Constants.DELAY_TIME_EXIT_FLAG_RESET);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mi_exit) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void attachAdapter(GroceryAdapter groceryAdapter) {
        this.groceryAdapter = groceryAdapter;
        rvGroceryList.setAdapter(groceryAdapter);
    }

    @Override
    public String getItemName() {
        return itemName;
    }

    @Override
    public void setItemMissingError(String error) {
        ((EditText) (dialogAddItem.findViewById(R.id.et_item_name))).setError(error);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void renderNewItem() {
        groceryAdapter.notifyItemInserted(0);
        dialogAddItem.dismiss();
        PostProcessing.handleDataPositions(groceryAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GroceryDatabase.destroyInstance();
    }

    @Override
    public int getSwipedIndex() {
        return swipedIndex;
    }

    @Override
    public void setError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void removeSwipedItem(int swipedIndex, String message) {
        groceryAdapter.notifyItemRemoved(swipedIndex);
        Snackbar.make(rvGroceryList, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getBaseContext()).registerReceiver(receiverUpdateStatus, new IntentFilter("update_status"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getBaseContext()).unregisterReceiver(receiverUpdateStatus);
    }
}
