package com.devx.movie.RecyclerView;

import android.app.AlertDialog;
import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devx.movie.Models.FavouriteItem;
import com.devx.movie.Models.Movie;
import com.devx.movie.R;
import com.devx.movie.ViewModel.ItemViewModel;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> implements Filterable {
    List<Movie> data = new ArrayList<>();
    List<Movie> completeData = new ArrayList<>();
    Context context;
    ItemViewModel viewModel;
    LifecycleOwner viewLifecycleOwner;
    public FavouriteItem[] Item = new FavouriteItem[1];

    private static final String TAG = "Recycler View Adapter";

    public MovieAdapter(Context context, List<Movie> data, LifecycleOwner viewLifecycleOwner){
        this.context = context;
        this.data = data;
        this.completeData = new ArrayList<>(data);
        this.viewLifecycleOwner = viewLifecycleOwner;
    }


    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Movie> filtered = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0){
                filtered.addAll(completeData);
            }
            else{
                for(Movie movie : completeData){
                    if (movie.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filtered.add(movie);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filtered;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if(filterResults.values != null){
                data.clear();
                data.addAll((Collection<? extends Movie>) filterResults.values);
            }
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout;
        ImageView imageView;
        TextView MovieName;
        TextView MovieYear;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.movie_list);
            imageView = itemView.findViewById(R.id.image);
            MovieName = itemView.findViewById(R.id.name);
            MovieYear = itemView.findViewById(R.id.year);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "OnCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = data.get(position);
        holder.MovieName.setText(movie.getName());
        String date = movie.getDate();
        holder.MovieYear.setText(date.substring(6));
        if(movie.getImage() == null){
            holder.imageView.setImageResource(R.drawable.ic_launcher_background);
        }
        else{
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReference(movie.getImage());
            Glide.with(context)
                    .load(storageReference)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(holder.imageView);
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                Movie clicked = data.get(pos);


                //ViewModel Instance
                viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance((Application) context.getApplicationContext()).create(ItemViewModel.class);
                final FavouriteItem[] item = new FavouriteItem[1];

                //get boolean status from Room using objectID
                final boolean[] status = {false};



                //create Alert Dialog
                View infoView = LayoutInflater.from(context).inflate(R.layout.movie_info_view, null);

                //get elements from the view
                ConstraintLayout layout = infoView.findViewById(R.id.movie_info);
                ImageView imageView, favourite;
                Button trailer;
                TextView name, desc, date, rating;

                imageView = infoView.findViewById(R.id.m_image);
                favourite = infoView.findViewById(R.id.fav);
                name = infoView.findViewById(R.id.m_name);
                desc = infoView.findViewById(R.id.m_description);
                trailer = infoView.findViewById(R.id.m_trailer);
                date = infoView.findViewById(R.id.m_date);
                rating = infoView.findViewById(R.id.m_rating);

                viewModel.getItem(clicked.getId()).observe(viewLifecycleOwner, new Observer<FavouriteItem>() {
                    @Override
                    public void onChanged(FavouriteItem favouriteItem) {
                        if(favouriteItem != null){
                            favourite.setImageResource(R.drawable.fav);
                        }
                        setFavouriteItem(favouriteItem);
                    }
                });

                if(item[0] != null) {
                    status[0] = item[0].isStatus();
                }

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.AppTheme_FullScreenDialog);
                dialogBuilder.setView(infoView);
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();


                if(status[0] == true) favourite.setImageResource(R.drawable.fav);
                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                StorageReference storageReference = firebaseStorage.getReference(movie.getImage());
                Glide.with(context)
                        .load(storageReference)
                        .placeholder(R.drawable.ic_launcher_background)
                        .fitCenter()
                        .into(imageView);

                name.setText(movie.getName());
                desc.setText(movie.getDescription());
                date.setText("Release Date :" + movie.getDate());
                rating.setText("IMDB Rating :" + String.valueOf(movie.getRating()));

                favourite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!status[0]){
                            viewModel.insertItem(new FavouriteItem(clicked.getId(), true));
                            favourite.setImageResource(R.drawable.fav);
                            status[0] = true;
                        }
                        else{
                            viewModel.deleteItem(clicked.getId());
                            favourite.setImageResource(R.drawable.unfav);
                            status[0] = false;
                        }
                    }
                });


                trailer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(clicked.getLink()));
                        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(clicked.getLink()));

                        try{
                            context.startActivity(appIntent);
                        }catch (ActivityNotFoundException ex){
                            context.startActivity(webIntent);
                        }
                    }
                });


            }

        });
    }

    private void setFavouriteItem(FavouriteItem favouriteItem) {
        Item[0] = favouriteItem;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
