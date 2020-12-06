package com.devx.movie.Fragments;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.devx.movie.Models.FavouriteItem;
import com.devx.movie.Models.Movie;
import com.devx.movie.R;
import com.devx.movie.RecyclerView.CustomAdapter;
import com.devx.movie.Retrofit.MovieAPI;
import com.devx.movie.Retrofit.ServiceGenerator;
import com.devx.movie.ViewInterface.CustomAdapterClick;
import com.devx.movie.ViewModel.ItemViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FavFragment extends Fragment implements LifecycleOwner, CustomAdapterClick {

    ItemViewModel viewModel;
    Context context;
    View view;
    ListView listView;
    ViewGroup viewGroup;
    CustomAdapter adapter = null;
    StringBuilder sb;
    public List<Movie> load = new ArrayList<>();

    private static final String TAG = "Favourite Fragment";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fav_fragment, container, false);
        listView = view.findViewById(R.id.list_view);
        viewGroup = container;
        context = view.getContext();
        sb = new StringBuilder();
        setHasOptionsMenu(true);
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance((Application) context.getApplicationContext()).create(ItemViewModel.class);
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(MovieAPI.BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        MovieAPI movieAPI = retrofit.create(MovieAPI.class);
        MovieAPI movieAPI = ServiceGenerator.getRequestAPI();

        viewModel.getAll().observe((AppCompatActivity)context, new Observer<List<FavouriteItem>>() {

            ArrayList<Movie> movies = new ArrayList<>();

            @Override
            public void onChanged(List<FavouriteItem> favouriteItems) {
                if(favouriteItems.size() != 0){
                    view.findViewById(R.id.list_view).setVisibility(View.VISIBLE);
                }
                for(int i = 0; i < favouriteItems.size(); i++){
                    Call<Movie> call = movieAPI.getById(favouriteItems.get(i).getId());
                    call.enqueue(new Callback<Movie>() {
                        @Override
                        public void onResponse(Call<Movie> call, Response<Movie> response) {
                            Movie movie = response.body();
                            if(movie != null){
                                viewModelCallback(movie);
                            }
                        }

                        @Override
                        public void onFailure(Call<Movie> call, Throwable t) {
                            Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            private void viewModelCallback(Movie s){
                movies.add(s);
                set(s);
            }
        });
        return view;
    }

    private void set(Movie s) {
        load.add(s);
        Log.d(TAG, ""+load.size());
        initListView();
        adapter.notifyDataSetChanged();
    }

    private void initListView() {
        if(adapter == null){
            adapter = new CustomAdapter(load, view.getContext(), this);
        }

        TextView emptyText = view.findViewById(R.id.textView);
        listView.setEmptyView(emptyText);
        listView.setAdapter(adapter);

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(context, ""+load.get(position).getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongItemClick(int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you sure?");
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                viewModel.deleteItem(load.get(position).getId());
                load.clear();
                adapter.notifyDataSetChanged();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
