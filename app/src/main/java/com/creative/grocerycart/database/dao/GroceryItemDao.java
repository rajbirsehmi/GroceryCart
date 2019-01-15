package com.creative.grocerycart.database.dao;

import com.creative.grocerycart.database.entity.GroceryItem;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface GroceryItemDao {

    @Insert
    long addItem(GroceryItem item);

    @Delete
    int deleteItem(GroceryItem item);

    @Query("SELECT item_id, item_name, is_checked " +
            "FROM grocery_list " +
            "ORDER BY item_id " +
            "DESC")
    List<GroceryItem> getItems();
}