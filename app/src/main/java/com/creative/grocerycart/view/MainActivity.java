package com.creative.grocerycart.view;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.creative.grocerycart.R;
import com.creative.grocerycart.adapter.GroceryAdapter;
import com.creative.grocerycart.database.database.GroceryDatabase;
import com.creative.grocerycart.model.GroceryItemAdderImpl;
import com.creative.grocerycart.model.GroceryItemRemoverImpl;
import com.creative.grocerycart.model.GroceryListLoaderImpl;
import com.creative.grocerycart.presenter.GroceryItemAdder;
import com.creative.grocerycart.presenter.GroceryItemRemover;
import com.creative.grocerycart.presenter.GroceryListLoader;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements GroceryListLoader, GroceryItemAdder, GroceryItemRemover {

    private GroceryAdapter groceryAdapter;

    private GroceryListLoaderImpl groceryListLoader;
    private GroceryItemAdderImpl groceryItemAdder;
    private GroceryItemRemoverImpl groceryItemRemover;

    private RecyclerView rvGroceryList;
    private Dialog dialogAddItem;

    private String itemName;
    private int swipedIndex = -1;

    private ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            swipedIndex = viewHolder.getAdapterPosition();
            groceryItemRemover.removeItem();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GroceryDatabase.createInstance(getBaseContext());

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
        new ItemTouchHelper(simpleItemTouchCallback).attachToRecyclerView(rvGroceryList);
        groceryListLoader.loadGroceryList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
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
}