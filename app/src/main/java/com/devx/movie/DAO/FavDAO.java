package com.devx.movie.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.devx.movie.Models.FavouriteItem;

import java.util.List;

@Dao
public interface FavDAO{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItem(FavouriteItem favouriteItem);

    @Query("DELETE from FAVOURITEDB where id = :id")
    void deleteItem(String id);

    @Update
    void updateItem(FavouriteItem favouriteItem);

    @Query("select * from FavouriteDB where id = :id")
    LiveData<FavouriteItem> get(String id);

    @Query("select * from FavouriteDB")
    LiveData<List<FavouriteItem>> getAll();
}
