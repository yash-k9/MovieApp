package com.devx.movie.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devx.movie.Models.Movie;
import com.devx.movie.R;
import com.devx.movie.RecyclerView.MovieAdapter;
import com.devx.movie.Retrofit.MovieAPI;
import com.devx.movie.Retrofit.ServiceGenerator;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment implements LifecycleOwner {
    View view;
    TextView error;
    RecyclerView  recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Retrofit retrofit;
    MovieAdapter adapter;
    Context context;
    ProgressBar progressBar;
    List<Movie> data = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        context = view.getContext();
        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        error = view.findViewById(R.id.error);
        setHasOptionsMenu(true);

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if(!isConnected){
            progressBar.setVisibility(View.INVISIBLE);
            error.setText(R.string.no_internet);
            error.setVisibility(View.VISIBLE);
        }

        recyclerView = view.findViewById(R.id.recycler);
        getData();
        return view;
    }



    private void getData() {
        //Get data from retrofit
        MovieAPI movieApi = ServiceGenerator.getRequestAPI();
        Call<List<Movie>> call = movieApi.getMovieList();
        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                progressBar.setVisibility(View.INVISIBLE);
                setList(response.body());
                initRecycler();
            }


            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                error.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setList(List<Movie> data1) {
        data = data1;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.search_icon);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(adapter != null) adapter.getFilter().filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    public void initRecycler(){
        //LayoutManager
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        //Adapter
        adapter = new MovieAdapter(context, data, getViewLifecycleOwner());
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
    }
}
