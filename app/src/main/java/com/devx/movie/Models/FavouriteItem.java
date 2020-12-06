package com.devx.movie.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "FavouriteDB")
public class FavouriteItem {
    @NonNull
    @PrimaryKey
    String id;
    boolean status;

    public  FavouriteItem(){
    }

    @Ignore
    public FavouriteItem(String id, boolean status) {
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
