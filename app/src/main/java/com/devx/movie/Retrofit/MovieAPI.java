package com.devx.movie.Retrofit;

import com.devx.movie.Models.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MovieAPI {
    //final String BASE_URL = "http://10.0.2.2:8090/rest/movies/";
    final String BASE_URL = "http://192.168.0.10:8090/rest/movies/";

    @GET("getAll")
    Call<List<Movie>> getMovieList();

    @GET("{val}")
    Call<Movie> getById(@Path("val") String Id);
}
