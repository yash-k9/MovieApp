package com.devx.movie.Repository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.devx.movie.DAO.FavDAO;
import com.devx.movie.Models.FavouriteItem;

@Database(entities = {FavouriteItem.class}, version = 1, exportSchema = false)
public abstract class FavDatabase extends RoomDatabase {
    private static FavDatabase instance;

    public abstract FavDAO favDao();

    public static synchronized FavDatabase getInstance(Context context){
        if(instance == null){
            return  Room.databaseBuilder(context.getApplicationContext(), FavDatabase.class, "favouriteDB")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }


}
