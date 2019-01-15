package com.creative.grocerycart.database.database;

import android.content.Context;

import com.creative.grocerycart.database.dao.GroceryItemDao;
import com.creative.grocerycart.database.entity.GroceryItem;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {GroceryItem.class}, version = 1)
public abstract class GroceryDatabase extends RoomDatabase {

    private static GroceryDatabase instance;

    public static void createInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, GroceryDatabase.class, "grocery_database").build();
        }
    }

    public static GroceryDatabase getInstance() {
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }

    public abstract GroceryItemDao groceryItemDao();
}