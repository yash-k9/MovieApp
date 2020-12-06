package com.devx.movie.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.devx.movie.DAO.FavDAO;
import com.devx.movie.Models.FavouriteItem;

import java.util.List;

public class FavItemRepository {
    public static LiveData<FavouriteItem> Item = null;
    private FavDAO favDAO;
    private LiveData<List<FavouriteItem>> liveData;


    public FavItemRepository(Application application){
        FavDatabase favDatabase = FavDatabase.getInstance(application);
        favDAO = favDatabase.favDao();
        liveData = favDAO.getAll();
    }

    public void insertItem(FavouriteItem favouriteItem){
        new insertItemAsyn(favDAO).execute(favouriteItem);
    }


    private static class insertItemAsyn extends AsyncTask<FavouriteItem, Void, Void> {
        private FavDAO Dao;

        public insertItemAsyn(FavDAO Dao) {
            this.Dao = Dao;
        }

        @Override
        protected Void doInBackground(FavouriteItem... favouriteItems) {
            Dao.insertItem(favouriteItems[0]);
            return null;
        }
    }

    public void deleteItem(String favouriteItem){
        new deleteAsyn(favDAO).execute(favouriteItem);
    }


    private static class deleteAsyn extends AsyncTask<String, Void, Void>{
        private FavDAO favDAO;
        public deleteAsyn(FavDAO favDAO) {
            this.favDAO = favDAO;
        }

        @Override
        protected Void doInBackground(String... favouriteItems) {
            favDAO.deleteItem(favouriteItems[0]);
            return null;
        }
    }

    public void updateItem(FavouriteItem favouriteItem){
        new updateAsyn(favDAO).execute(favouriteItem);
    }


    private static class updateAsyn extends AsyncTask<FavouriteItem, Void, Void>{
        private FavDAO favDAO;
        public updateAsyn(FavDAO favDAO) {
            this.favDAO = favDAO;
        }

        @Override
        protected Void doInBackground(FavouriteItem... favouriteItems) {
            favDAO.updateItem(favouriteItems[0]);
            return null;
        }
    }

    public LiveData<FavouriteItem> getItem(String favouriteItemId){
        Item = favDAO.get(favouriteItemId);
        return Item;
    }

//    private class getAsyn extends AsyncTask<String, Void, FavouriteItem> {
//        private FavDAO favDAO;
//        public getAsyn(FavDAO favDAO) {
//            this.favDAO = favDAO;
//        }
//
//        @Override
//        protected FavouriteItem doInBackground(String... strings) {
//            return favDAO.get(strings[0]);
//        }
//
//
//        @Override
//        protected void onPostExecute(FavouriteItem favouriteItem) {
//            super.onPostExecute(favouriteItem);
//            FavItemRepository.setItem(favouriteItem);
//        }
//    }

//    private static void setItem(FavouriteItem favouriteItem) {
//        Item = favouriteItem;
//    }


    public LiveData<List<FavouriteItem>> getAll(){
        return liveData;
    }
}
