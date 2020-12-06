package com.devx.movie.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static final String BASE_URL = "http://192.168.0.10:8090/rest/movies/";

    private static Retrofit retrofit = new Retrofit.Builder()
                                                    .baseUrl(BASE_URL)
                                                    .addConverterFactory(GsonConverterFactory.create())
                                                    .build();

    private static MovieAPI movieAPI = retrofit.create(MovieAPI.class);

    public static Retrofit getInstance(){
       return retrofit;
    }

    public static MovieAPI getRequestAPI(){
        return movieAPI;
    }


}
