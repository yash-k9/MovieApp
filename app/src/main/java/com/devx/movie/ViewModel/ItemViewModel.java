package com.devx.movie.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.devx.movie.Models.FavouriteItem;
import com.devx.movie.Repository.FavItemRepository;

import java.util.List;

public class ItemViewModel extends AndroidViewModel {
    private FavItemRepository favRepo;

    public ItemViewModel(@NonNull Application application) {
        super(application);
        favRepo = new FavItemRepository(application);
    }

    public void insertItem(FavouriteItem favouriteItem){
        favRepo.insertItem(favouriteItem);
    }

    public void deleteItem(String favouriteItem){
        favRepo.deleteItem(favouriteItem);
    }

    public LiveData<FavouriteItem> getItem(String favouriteItem){
        return favRepo.getItem(favouriteItem);
    }

    public void updateItem(FavouriteItem favouriteItem){
        favRepo.insertItem(favouriteItem);
    }

    public LiveData<List<FavouriteItem>> getAll(){
        return favRepo.getAll();
    }
}
