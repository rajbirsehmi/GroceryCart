package com.creative.grocerycart.database.entity;

import org.jetbrains.annotations.NotNull;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "grocery_list")
public class GroceryItem {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id")
    @NotNull
    private int itemId;

    @ColumnInfo(name = "item_name")
    @NotNull
    private String itemName;

    @ColumnInfo(name = "is_checked")
    private boolean isChecked = false;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
