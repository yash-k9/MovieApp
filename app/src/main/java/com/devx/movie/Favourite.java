package com.devx.movie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.os.Bundle;
import android.widget.Toast;

import com.devx.movie.ViewModel.ItemViewModel;

public class Favourite extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
    }
}