package com.creative.grocerycart.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.creative.grocerycart.R;
import com.creative.grocerycart.adapter.GroceryAdapter;
import com.creative.grocerycart.model.GroceryItemAdderImpl;
import com.creative.grocerycart.model.GroceryListLoaderImpl;
import com.creative.grocerycart.presenter.GroceryItemAdder;
import com.creative.grocerycart.presenter.GroceryListLoader;

public class MainActivity extends AppCompatActivity implements GroceryListLoader, GroceryItemAdder {

    private RecyclerView rvGroceryList;
    private GroceryListLoaderImpl groceryListLoader;
    private GroceryItemAdderImpl groceryItemAdder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        groceryListLoader = new GroceryListLoaderImpl(getBaseContext(), this);


        findViewById(R.id.fab_add_item).setOnClickListener((view) -> {

        });

        rvGroceryList = findViewById(R.id.rv_grocery_list);
        rvGroceryList.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        rvGroceryList.setItemAnimator(new DefaultItemAnimator());

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
        rvGroceryList.setAdapter(groceryAdapter);
    }
}
