package com.devx.movie.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.devx.movie.Models.Movie;
import com.devx.movie.R;
import com.devx.movie.ViewInterface.CustomAdapterClick;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    List<Movie> data;
    Context context;
    CustomAdapterClick customAdapterClick;

    public CustomAdapter(List<Movie> data, Context context, CustomAdapterClick customAdapterClick){
        this.data = data;
        this.context = context;
        this.customAdapterClick = customAdapterClick;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Movie movie = data.get(i);
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.list_view_fav_fragment, viewGroup, false);
        }
        ImageView imageView = view.findViewById(R.id.fav_image);
        TextView tv = view.findViewById(R.id.fav_name);
        ConstraintLayout layout = view.findViewById(R.id.list_view);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(movie.getImage());
        Glide.with(context)
                .load(storageReference)
                .placeholder(R.drawable.ic_launcher_background)
                .centerCrop()
                .into(imageView);
        tv.setText(movie.getName());

        layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                customAdapterClick.onLongItemClick(i);
                return false;
            }
        });

        return view;
    }
}
